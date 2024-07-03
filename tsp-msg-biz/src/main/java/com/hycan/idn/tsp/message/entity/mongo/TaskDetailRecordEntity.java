package com.hycan.idn.tsp.message.entity.mongo;

import com.hycan.idn.tsp.message.constant.BussTypeConstants;
import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.pojo.publishtask.BroadcastTaskPayloadDTO;
import com.hycan.idn.tsp.message.pojo.publishtask.BroadcastTaskPublishedDTO;
import com.hycan.idn.tsp.message.pojo.publishtask.SceneCardTaskPayloadDTO;
import com.hycan.idn.tsp.message.pojo.publishtask.SceneCardTaskPublishedDTO;
import com.hycan.idn.tsp.message.pojo.taskrelation.QueryTaskResourceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 任务(萌宠播报/场景卡片)详情记录
 *
 * @author liangliang
 * @datetime 2022/08/08 16:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "task_detail_record")
@CompoundIndexes({
        @CompoundIndex(name = "base_task_id_1_exec_policy_1_status_1", def = "{'base_task_id':1,'exec_policy':1,'status':1}")
})
public class TaskDetailRecordEntity {

    public static final String BASE_TASK_ID = "base_task_id";
    public static final String BUSS_TYPE = "buss_type";
    public static final String PAYLOAD = "payload";
    public static final String EXEC_POLICY = "exec_policy";
    public static final String STATUS = "status";
    public static final String RECORD_TIME = "record_time";

    private static final String HOLIDAY_MSG = "holiday_msg";

    // @formatter:off

    /** id */
    @Id
    private String id;

    /** 基础任务ID */
    @Indexed(unique = true)
    @Field(name = "base_task_id")
    private Long baseTaskId;

    /** 业务类型 */
    @Field(name = "buss_type")
    private String bussType;

    /** 消息内容结构体 */
    private Object payload;

    /** 消息状态(启用:ENABLE、禁用:FORBIDDEN) */
    @Field(name = "status")
    private String status;

    /** 执行策略(AVNT_ONLINE:车辆上电、USER_LOGIN:账号登陆) */
    @Field(name = "exec_policy")
    private String execPolicy;

    /** 生成时间 */
    @Field(name = "record_time")
    private LocalDateTime recordTime;

    // @formatter:on

    public static TaskDetailRecordEntity ofBroadcastTask(BroadcastTaskPublishedDTO task) {
        TaskDetailRecordEntity entity = new TaskDetailRecordEntity();

        entity.setBaseTaskId(task.getBaseTaskId());
        entity.setStatus(BizDataStatusConstants.ENABLE);
        entity.setExecPolicy(task.getExecPolicy());
        entity.setBussType(BussTypeConstants.DATA_MSG);
        entity.setRecordTime(LocalDateTime.now());

        BroadcastTaskPayloadDTO broadcastTaskPayloadDTO = new BroadcastTaskPayloadDTO();
        broadcastTaskPayloadDTO.setTitle(task.getMsgTitle());
        broadcastTaskPayloadDTO.setType(HOLIDAY_MSG);
        broadcastTaskPayloadDTO.setContent(task.getMsgContent());

        entity.setPayload(broadcastTaskPayloadDTO);

        return entity;
    }

    public static TaskDetailRecordEntity ofSceneCardTask(SceneCardTaskPublishedDTO task, QueryTaskResourceDTO taskResource) {
        TaskDetailRecordEntity entity = new TaskDetailRecordEntity();
        entity.setBaseTaskId(task.getBaseTaskId());
        entity.setStatus(BizDataStatusConstants.ENABLE);
        entity.setExecPolicy(task.getExecPolicy());
        entity.setBussType(BussTypeConstants.IMAGE_TEXT_MSG);
        entity.setRecordTime(LocalDateTime.now());

        SceneCardTaskPayloadDTO sceneCardTaskPayloadDTO = new SceneCardTaskPayloadDTO();
        sceneCardTaskPayloadDTO.setTitle(task.getMsgTitle());
        sceneCardTaskPayloadDTO.setContent(task.getMsgContent());
        sceneCardTaskPayloadDTO.setPicUrl(taskResource.getPictureUrl());
        sceneCardTaskPayloadDTO.setDetail(taskResource.getResourceUrl());
        sceneCardTaskPayloadDTO.setType(taskResource.getResourceType().toLowerCase(Locale.US));

        sceneCardTaskPayloadDTO.setActions(task.getActions());

        entity.setPayload(sceneCardTaskPayloadDTO);

        return entity;
    }
}
