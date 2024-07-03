package com.hycan.idn.tsp.message.entity.mongo;

import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
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


/**
 * 消息内容表
 *
 * @author liangliang
 * @datetime 2022/12/26 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "msg_send_record")
@CompoundIndexes({
        @CompoundIndex(name = "msg_id_buss_type", def = "{'msg_id':1, 'buss_type':1}"),
        @CompoundIndex(name = "status_fail_times", def = "{'status':1, 'fail_times':1}")
})
public class MsgSendRecordEntity {

    public static final String COLLECTION_NAME = "msg_send_record";

    public static final String MSG_ID = "msg_id";
    public static final String STATUS = "status";
    public static final String FAIL_TIMES = "fail_times";
    public static final String PAYLOAD = "payload";
    public static final String RECORD_TIME = "record_time";
    public static final String BUS_TYPE = "buss_type";
    public static final String VIN = "vin";
    public static final String REPORT_TIME = "report_time";

    // @formatter:off

    /** id */
    @Id
    private String id;

    /** 唯一消息ID */
    @Indexed(unique = true)
    @Field(name = "msg_id")
    private String msgId;

    /** 业务类型值  */
    @Field(name = "buss_type")
    private String bussType;

    /** vin车架号 */
    @Field(name = "vin")
    private String vin;

    /** 消息发送状态（SUCCESS:成功、FAILURE:失败） */
    @Field(name = "status")
    private String status;

    /** 消息内容 */
    @Field(name = "payload")
    private Object payload;

    /** 失败次数 */
    @Field(name = "fail_times")
    private Integer failTimes = 0;

    /** 消息上报时间，统一使用毫秒时间戳 */
    @Field(name = "report_time")
    private Long reportTime;

    /** 记录时间 */
    @Indexed(name = "deleteAt", expireAfterSeconds = 90 * 24 * 60 * 60)
    @Field(name = "record_time")
    private LocalDateTime recordTime;

    // @formatter:off

    public static MsgSendRecordEntity of(SendMsgDTO sendMsgDTO) {
        MsgSendRecordEntity entity = new MsgSendRecordEntity();
        entity.setMsgId(sendMsgDTO.getMsgId());
        entity.setBussType(sendMsgDTO.getBussType());
        entity.setVin(sendMsgDTO.getVin());
        entity.setStatus(MsgSendStatusConstant.SENDING);
        entity.setFailTimes(0);
        entity.setPayload(sendMsgDTO.getPayload());
        entity.setReportTime(sendMsgDTO.getReportTime());
        entity.setRecordTime(LocalDateTime.now());
        return entity;
    }
}
