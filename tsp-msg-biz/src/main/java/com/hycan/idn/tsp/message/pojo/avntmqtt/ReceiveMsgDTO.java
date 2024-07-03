package com.hycan.idn.tsp.message.pojo.avntmqtt;

import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity.ForwardConfig;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 协议信息
 *
 * @author liangliang
 * @datetime 2022/12/26 16:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMsgDTO extends SendMsgDTO {

    /**
     * 协议类型
     */
    private String protocol;

    /**
     * 发送状态id
     */
    private String sendStateId;

    /**
     * 车型号
     */
    private String serial;

    /**
     * 转发目标
     */
    private ForwardConfig forwardConfig;

    /**
     * 消息发送次数
     */
    private Integer sendTimes;

}
