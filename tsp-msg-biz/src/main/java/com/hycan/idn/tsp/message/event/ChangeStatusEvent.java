package com.hycan.idn.tsp.message.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/***
 * 发送消息事件
 * @author shichongying
 */
@Getter
@Setter
public class ChangeStatusEvent extends ApplicationEvent {

    private String type;
    private String vin;

    /**
     * 构造函数
     *
     * @param source 源
     */
    public ChangeStatusEvent(Object source, String vin, String type) {
        super(source);

        this.type = type;
        this.vin = vin;
    }
}
