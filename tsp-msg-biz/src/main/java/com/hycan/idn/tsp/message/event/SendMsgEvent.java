package com.hycan.idn.tsp.message.event;

import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity.ForwardConfig;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/***
 * 发送消息事件
 * @author shichongying
 */
@Getter
@Setter
public class SendMsgEvent extends ApplicationEvent {

    private SendMsgDTO sendMsgDTO;

    private ForwardConfig forwardConfig;

    /**
     * 构造函数
     *
     * @param source 源
     */
    public SendMsgEvent(Object source, SendMsgDTO sendMsgDTO, ForwardConfig forwardConfig) {
        super(source);
        this.sendMsgDTO = sendMsgDTO;
        this.forwardConfig = forwardConfig;
    }
}
