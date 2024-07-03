package com.hycan.idn.tsp.message.job;

import com.hycan.idn.tsp.message.constant.RedisKeyConstants;
import com.hycan.idn.tsp.message.entity.mongo.TaskDetailRecordEntity;
import com.hycan.idn.tsp.message.entity.mongo.TaskExecRecordEntity;
import com.hycan.idn.tsp.message.facade.SendMsgFacade;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.repository.mongo.TaskDetailRecordRepository;
import com.hycan.idn.tsp.message.repository.mongo.TaskExecRecordRepository;
import com.hycan.idn.tsp.message.service.TaskExecRecordService;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.hycan.idn.tsp.message.utils.RedisUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.hycan.idn.tsp.message.pojo.CdcMqttStatus.ONLINE;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-05-15 17:59
 */
@Slf4j
@Component
public class PushTaskMsgJobHandler {

    @Resource
    private TaskExecRecordRepository taskExecRecordRepository;

    @Resource
    private TaskDetailRecordRepository taskDetailRecordRepository;

    @Resource
    private SendMsgFacade sendMsgFacade;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TaskExecRecordService taskExecRecordService;

    @XxlJob("pushTaskMsgJobHandler")
    public void pushTaskMsgJobHandler() {
        log.debug("定时任务: 推送萌宠播报/场景卡片消息, 开始执行");

        try {
            //获取消息任务
            Page<TaskExecRecordEntity> page = taskExecRecordRepository.findUnsentTasksPage(LocalDateTime.now(), Pageable.ofSize(500));
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getContent())) {
                return;
            }

            page.getContent().forEach(execRecord -> {
                TaskDetailRecordEntity detailRecord = taskDetailRecordRepository.findDetailRecord(execRecord.getBaseTaskId());
                if (detailRecord == null) {
                    return;
                }

                String execPolicy = detailRecord.getExecPolicy();

                Integer status = (Integer) redisUtil.hashGet(RedisKeyConstants.CDC_STATUS_KEY + execRecord.getVin(), execPolicy);
                if (!ONLINE.equals(status)) {
                    return;
                }

                SendMsgDTO sendMsgDTO = SendMsgDTO.of(execRecord.getVin(), detailRecord.getBussType(), detailRecord.getPayload());
                sendMsgFacade.publish(sendMsgDTO);

                taskExecRecordService.updateTaskExecRecord(execRecord.getId(), sendMsgDTO.getMsgId());
            });

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 推送萌宠播报/场景卡片消息, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 推送萌宠播报/场景卡片消息, 结束执行");
    }

}
