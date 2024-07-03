package com.hycan.idn.tsp.message.entity.mongo;

import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.pojo.vehiclebase.VehicleBaseInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 发送详情表
 *
 * @author Liuyingjie
 * @datetime 2022/8/23 17:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "task_exec_record")
@CompoundIndexes({
        @CompoundIndex(name = "base_task_id", def = "{'base_task_id':1}"),
        @CompoundIndex(name = "status", def = "{'status':1}"),
        @CompoundIndex(name = "vin", def = "{'vin':1}"),
})
public class TaskExecRecordEntity {

    public static final String COLLECTION_NAME = "task_exec_record";
    public static final String BASE_TASK_ID = "base_task_id";
    public static final String VIN = "vin";
    public static final String SERIAL = "serial";
    public static final String SERIAL_ID = "serial_id";
    public static final String MT = "mt";
    public static final String MT_ID = "mt_id";
    public static final String STATUS = "status";
    public static final String MSG_ID = "msg_id";
    public static final String SEND_TIME = "send_time";
    public static final String ENABLE_TIME = "enable_time";
    public static final String DISABLE_TIME = "disable_time";
    public static final String RECORD_TIME = "record_time";
    public static final String COUNT = "count";

    // @formatter:off

    /** ID */
    @Id
    private String id;

    /** VIN码 */
    @Indexed
    @Field(name = "vin")
    private String vin;

    /*** 基础任务ID */
    @Indexed
    @Field(name = "base_task_id")
    private Long baseTaskId;

    /** 消息ID */
    @Field(name = "msg_id")
    private String msgId;

    /** 推送时间 */
    @Field(name = "send_time")
    private LocalDateTime sendTime;

    /** 状态(SUCCESS:成功、FAILURE:失败、UNSENT:未推送、SENDING:推送中) */
    @Indexed
    @Field(name = "status")
    private String status;

    /** 车系 */
    @Field(name = "serial")
    private String serial;

    /** 车系ID */
    @Field(name = "serial_id")
    private String serialId;

    /** 车型 */
    @Field(name = "mt")
    private String mt;

    /** 车型ID */
    @Field(name = "mt_id")
    private Long mtId;

    /** 任务启用时间 */
    @Field(name = "enable_time")
    private LocalDateTime enableTime;

    /** 任务失效时间 */
    @Field(name = "disable_time")
    private LocalDateTime disableTime;

    /** 生成时间 */
    @Field(name = "record_time")
    private LocalDateTime recordTime;

    // @formatter:on

    public static TaskExecRecordEntity of(Long baseTaskId, LocalDate startDate, LocalDate endDate, VehicleBaseInfoDTO vehInfo) {
        TaskExecRecordEntity entity = new TaskExecRecordEntity();
        entity.setBaseTaskId(baseTaskId);
        entity.setRecordTime(LocalDateTime.now());
        entity.setStatus(MsgSendStatusConstant.UNSENT);
        entity.setEnableTime(LocalDateTime.of(startDate, LocalTime.MIN));
        entity.setDisableTime(LocalDateTime.of(endDate, LocalTime.MAX));
        entity.setVin(vehInfo.getVin());
        entity.setSerialId(vehInfo.getSerialId());
        entity.setSerial(vehInfo.getSerial());
        entity.setMt(vehInfo.getMt());
        entity.setMtId(vehInfo.getMtId());
        return entity;
    }

    public static TaskExecRecordEntity of(Long baseTaskId, LocalDateTime startTime, LocalDateTime endTime, VehicleBaseInfoDTO vehInfo) {
        TaskExecRecordEntity entity = new TaskExecRecordEntity();
        entity.setBaseTaskId(baseTaskId);
        entity.setRecordTime(LocalDateTime.now());
        entity.setStatus(MsgSendStatusConstant.UNSENT);
        entity.setEnableTime(startTime);
        entity.setDisableTime(endTime);
        entity.setVin(vehInfo.getVin());
        entity.setSerialId(vehInfo.getSerialId());
        entity.setSerial(vehInfo.getSerial());
        entity.setMt(vehInfo.getMt());
        entity.setMtId(vehInfo.getMtId());
        return entity;
    }
}
