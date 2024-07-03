package com.hycan.idn.tsp.message.job;

import com.hycan.idn.tsp.message.entity.mongo.MsgSendRecordEntity;
import com.hycan.idn.tsp.message.facade.SendMsgFacade;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.repository.mongo.MsgSendRecordRepository;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.Objects;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-28 17:38
 */
@Slf4j
@Component
public class RetrySendMsgJobHandler {

    @Resource
    private MsgSendRecordRepository msgSendRecordRepository;

    @Resource
    private SendMsgFacade sendMsgFacade;

    @XxlJob("retrySendFailureMsgHandler")
    public void retrySendFailureMsgHandler() {
        log.debug("定时任务: 重发失败消息, 开始执行");
        try {
            Page<MsgSendRecordEntity> pageEntities = msgSendRecordRepository.findFailureMsg(Pageable.ofSize(500));
            if (Objects.isNull(pageEntities) || CollectionUtils.isEmpty(pageEntities.getContent())) {
                return;
            }
            pageEntities.getContent().forEach(entity -> {
                Long reportTime = entity.getRecordTime().atZone(ZoneId.systemDefault()).toEpochSecond();
                sendMsgFacade.republish(SendMsgDTO.of(entity.getVin(), entity.getBussType(), entity.getPayload(), reportTime));
            });

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 重发失败消息, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 重发失败消息, 结束执行");
    }
}
