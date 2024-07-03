package com.hycan.idn.tsp.message.job;

import com.hycan.idn.tsp.message.cache.VehBaseInfoCache;
import com.hycan.idn.tsp.message.constant.TaskExecRateConstants;
import com.hycan.idn.tsp.message.entity.mongo.TaskDetailRecordEntity;
import com.hycan.idn.tsp.message.entity.mongo.TaskExecRecordEntity;
import com.hycan.idn.tsp.message.facade.UserAccountFeignFacade;
import com.hycan.idn.tsp.message.pojo.publishtask.BroadcastTaskPublishedDTO;
import com.hycan.idn.tsp.message.pojo.publishtask.SceneCardTaskPublishedDTO;
import com.hycan.idn.tsp.message.pojo.taskrelation.QueryTaskResourceDTO;
import com.hycan.idn.tsp.message.pojo.vehiclebase.VehicleBaseInfoDTO;
import com.hycan.idn.tsp.message.repository.mysql.BaseTaskInfoMapper;
import com.hycan.idn.tsp.message.repository.mysql.BroadcastTaskMapper;
import com.hycan.idn.tsp.message.repository.mysql.SceneCardTaskMapper;
import com.hycan.idn.tsp.message.service.TaskRelationService;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 初始化任务详情和任务执行记录定时器
 * 1、获取定时任务执行时间，对应发布中的萌宠播报任务，转换成任务详情和任务执行记录
 * 2、获取定时任务执行时间，对应发布中的场景卡片任务，转换成任务详情和任务执行记录
 * 3、如果当前存在发送中的生日问候任务，则将当天生日车主对应的VIN加入任务执行记录
 *
 * @author liangliang
 * @datetime 2022/08/12 13:58
 */
@Slf4j
@Component
public class InitTaskRecordJobHandler {

    @Resource
    private VehBaseInfoCache vehBaseInfoCache;

    @Resource
    private BroadcastTaskMapper broadcastTaskMapper;

    @Resource
    private SceneCardTaskMapper sceneCardTaskMapper;

    @Resource
    private TaskRelationService taskRelationService;

    @Resource
    private UserAccountFeignFacade userAccountFeignFacade;

    @Resource
    private BaseTaskInfoMapper baseTaskInfoMapper;

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 发布场景卡片任务处理器
     */
    @XxlJob("publishSceneCardTaskHandler")
    public void publishSceneCardTaskHandler() {
        log.debug("定时任务: 场景卡片任务初始化, 开始执行");
        try {
            List<SceneCardTaskPublishedDTO> taskList = sceneCardTaskMapper.findPublishedTask();
            if (CollectionUtils.isEmpty(taskList)) {
                return;
            }

            for (SceneCardTaskPublishedDTO task : taskList) {
                QueryTaskResourceDTO taskResource = taskRelationService.getTaskResourceByBaseTaskId(task.getBaseTaskId());
                upsertTaskDetailRecord(TaskDetailRecordEntity.ofSceneCardTask(task, taskResource));

                batchUpsertTaskExecRecords(buildSceneCardTaskExec(task));

                baseTaskInfoMapper.updateTaskStatusSending(task.getBaseTaskId());
            }

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 场景卡片任务初始化, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 场景卡片任务初始化, 结束执行");
    }

    /**
     * 发布萌宠播报任务处理器
     */
    @XxlJob("publishBroadcastTaskHandler")
    public void publishBroadcastTaskHandler() {
        log.debug("定时任务: 萌宠播报任务初始化, 开始执行");
        try {
            List<BroadcastTaskPublishedDTO> taskList = broadcastTaskMapper.findPublishedTask();
            if (CollectionUtils.isEmpty(taskList)) {
                return;
            }

            for (BroadcastTaskPublishedDTO task : taskList) {
                upsertTaskDetailRecord(TaskDetailRecordEntity.ofBroadcastTask(task));

                batchUpsertTaskExecRecords(buildBroadcastTaskExec(task));

                baseTaskInfoMapper.updateTaskStatusSending(task.getBaseTaskId());
            }
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error("定时任务: 萌宠播报任务初始化, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 萌宠播报任务初始化, 结束执行");
    }


    @XxlJob("updateBirthdayTaskHandler")
    public void updateBirthdayTaskHandler() {
        log.debug("定时任务: 更新萌宠播报-生日问候车辆, 开始执行");
        try {
            List<Long> taskList = broadcastTaskMapper.findSendingBirthdayTask();
            if (CollectionUtils.isEmpty(taskList)) {
                return;
            }

            LocalDate currentDate = LocalDate.now();
            List<TaskExecRecordEntity> execRecordEntityList = new ArrayList<>();
            for (Long baseTaskId : taskList) {
                List<String> vinList = userAccountFeignFacade.getBirthdayVins();
                Map<String, VehicleBaseInfoDTO> vehInfoMap = vehBaseInfoCache.getVehInfoByVins(vinList);
                for (String vin : vinList) {
                    execRecordEntityList.add(TaskExecRecordEntity.of(baseTaskId, currentDate, currentDate, vehInfoMap.get(vin)));
                }
            }

            batchUpsertTaskExecRecords(execRecordEntityList);
        } catch (Exception e) {
            log.error("定时任务: 更新萌宠播报-生日问候车辆, 执行异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            XxlJobHelper.handleFail(e.getMessage());
        }
        log.debug("定时任务: 更新萌宠播报-生日问候车辆, 结束执行");
    }

    private List<TaskExecRecordEntity> buildSceneCardTaskExec(SceneCardTaskPublishedDTO task) {
        List<TaskExecRecordEntity> execRecordEntityList = new ArrayList<>();

        List<String> vinList = taskRelationService.getVinsByBaseTaskIdAndScope(task.getBaseTaskId(), task.getScope());
        Map<String, VehicleBaseInfoDTO> vehInfoMap = vehBaseInfoCache.getVehInfoByVins(vinList);

        for (String vin : vinList) {
            LocalDateTime startDate = task.getStartTime();
            LocalDateTime endDate = task.getEndTime();

            LocalDateTime currentDate = startDate;
            while (currentDate.isBefore(endDate)) {
                execRecordEntityList.add(TaskExecRecordEntity.of(task.getBaseTaskId(), currentDate, endDate, vehInfoMap.get(vin)));
                currentDate = currentDate.plusDays(1);
            }
        }

        return execRecordEntityList;
    }

    private List<TaskExecRecordEntity> buildBroadcastTaskExec(BroadcastTaskPublishedDTO task) {
        List<TaskExecRecordEntity> execRecordEntityList = new ArrayList<>();

        List<String> vinList = taskRelationService.getVinsByBaseTaskIdAndScope(task.getBaseTaskId(), task.getScope());
        Map<String, VehicleBaseInfoDTO> vehInfoMap = vehBaseInfoCache.getVehInfoByVins(vinList);

        for (String vin : vinList) {
            LocalDate startDate = task.getStartDate();
            LocalDate endDate = task.getEndDate();
            if (TaskExecRateConstants.EVERY_DAY.equals(task.getExecRate())) {
                LocalDate currentDate = startDate;
                while (currentDate.isBefore(endDate)) {
                    execRecordEntityList.add(TaskExecRecordEntity.of(task.getBaseTaskId(), currentDate, currentDate, vehInfoMap.get(vin)));
                    currentDate = currentDate.plusDays(1);
                }
                continue;
            }
            execRecordEntityList.add(TaskExecRecordEntity.of(task.getBaseTaskId(), startDate, endDate, vehInfoMap.get(vin)));
        }

        return execRecordEntityList;
    }

    private void upsertTaskDetailRecord(TaskDetailRecordEntity entity) {
        Query query = Query.query(Criteria.where(TaskDetailRecordEntity.BASE_TASK_ID).is(entity.getBaseTaskId()));
        Update update = Update.update(TaskDetailRecordEntity.RECORD_TIME, entity.getRecordTime())
                .setOnInsert(TaskDetailRecordEntity.BASE_TASK_ID, entity.getBaseTaskId())
                .set(TaskDetailRecordEntity.PAYLOAD, entity.getPayload())
                .set(TaskDetailRecordEntity.EXEC_POLICY, entity.getExecPolicy())
                .set(TaskDetailRecordEntity.STATUS, entity.getStatus())
                .set(TaskDetailRecordEntity.BUSS_TYPE, entity.getBussType());
        mongoTemplate.upsert(query, update, TaskDetailRecordEntity.class);
    }

    private void batchUpsertTaskExecRecords(List<TaskExecRecordEntity> taskExecRecordEntityList) {
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TaskExecRecordEntity.class);
        taskExecRecordEntityList.forEach(entity -> {
            Query query = Query.query(Criteria.where(TaskExecRecordEntity.BASE_TASK_ID).is(entity.getBaseTaskId())
                    .and(TaskExecRecordEntity.VIN).is(entity.getVin()));
            Update update = Update.update(TaskExecRecordEntity.RECORD_TIME, entity.getRecordTime())
                    .set(TaskExecRecordEntity.MSG_ID, entity.getMsgId())
                    .setOnInsert(TaskExecRecordEntity.BASE_TASK_ID, entity.getBaseTaskId())
                    .set(TaskExecRecordEntity.SERIAL_ID, entity.getSerialId())
                    .set(TaskExecRecordEntity.SEND_TIME, entity.getSendTime())
                    .set(TaskExecRecordEntity.STATUS, entity.getStatus())
                    .set(TaskExecRecordEntity.SERIAL, entity.getSerial())
                    .set(TaskExecRecordEntity.SERIAL_ID, entity.getSerialId())
                    .set(TaskExecRecordEntity.MT, entity.getMt())
                    .set(TaskExecRecordEntity.MT_ID, entity.getMtId())
                    .set(TaskExecRecordEntity.ENABLE_TIME, entity.getEnableTime())
                    .set(TaskExecRecordEntity.DISABLE_TIME, entity.getDisableTime())
                    .setOnInsert(TaskExecRecordEntity.VIN, entity.getVin());
            operations.upsert(query, update);
        });

        operations.execute();
    }
}
