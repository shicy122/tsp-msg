package com.hycan.idn.tsp.message.job;

import com.hycan.idn.tsp.message.repository.mysql.BaseTaskInfoMapper;
import com.hycan.idn.tsp.message.repository.mysql.HolidayConfigMapper;
import com.hycan.idn.tsp.message.service.TaskDetailRecordService;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改时间过期的数据状态
 * 1、节假日配置过期，将状态修改为失效
 * 2、萌宠播报/场景卡片任务过期，将状态修改为已结束
 *
 * @author Liuyingjie
 * @datetime 2022/8/26 00:03
 */
@Slf4j
@Component
public class StatusExpireJobHandler {

    @Resource
    private HolidayConfigMapper holidayConfigMapper;

    @Resource
    private BaseTaskInfoMapper baseTaskInfoMapper;

    @Resource
    private TaskDetailRecordService taskDetailRecordService;

    /**
     * 将节假日配置表中，截止时间 < 当前时间 的配置状态，修改为失效状态
     */
    @XxlJob("holidayConfigStatusJobHandler")
    public void holidayConfigStatusJobHandler() {
        log.debug("定时任务: 节假日配置过期, 开始执行");
        try {
            holidayConfigMapper.updateStatusDisableByEndDate();

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 节假日配置过期, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 节假日配置过期, 结束执行");
    }

    /**
     * 将基础任务表中，结束时间 < 当前时间 的任务状态，修改为已结束
     */
    @XxlJob("baseTaskStatusJobHandler")
    public void baseTaskStatusJobHandler() {
        log.debug("定时任务: 萌宠播报/场景卡片任务停止, 开始执行");
        try {
            baseTaskInfoMapper.updateStatusFinishedByFinishTime();

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 萌宠播报/场景卡片任务停止, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 萌宠播报/场景卡片任务停止, 结束执行");
    }

    /**
     * 根据已结束任务的ID，修改任务详情表中的状态为禁用
     */
    @XxlJob("taskDetailStatusJobHandler")
    public void taskDetailStatusJobHandler() {
        log.debug("定时任务: 任务详情禁用, 开始执行");
        try {
            List<Long> finishedBaseTaskId = baseTaskInfoMapper.findStatusFinishedByFinishTime(LocalDateTime.now().minusHours(25));

            taskDetailRecordService.updateDetailRecordForbidden(finishedBaseTaskId);

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 任务详情禁用, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 任务详情禁用, 结束执行");
    }
}
