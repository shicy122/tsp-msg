package com.hycan.idn.tsp.message.pojo.avntmqtt;

import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Liuyingjie
 * @datetime 2022/7/6 15:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendRabbitMsgDTO {

    /**
     * 业务类型
     */
    private String bussType;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 消息唯一id
     */
    private String id;
    /**
     * 发送时间
     */
    private Long sendTime;
    /**
     * 消息上报时间，统一使用毫秒时间戳
     */
    private Long reportTime;
    /**
     * 消息内容
     */
    private Object payload;

    public static SendRabbitMsgDTO of(SendMsgDTO sendMsgDTO) {
        SendRabbitMsgDTO msgDTO = new SendRabbitMsgDTO();
        msgDTO.setId(sendMsgDTO.getMsgId());
        msgDTO.setUid(sendMsgDTO.getUid());
        msgDTO.setBussType(sendMsgDTO.getBussType());
        msgDTO.setPayload(sendMsgDTO.getPayload());
        msgDTO.setSendTime(System.currentTimeMillis());
        msgDTO.setReportTime(sendMsgDTO.getReportTime());
        return msgDTO;
    }
}
