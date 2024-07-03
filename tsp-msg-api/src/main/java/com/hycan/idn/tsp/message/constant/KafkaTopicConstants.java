package com.hycan.idn.tsp.message.constant;

/**
 * kafka主题常量
 *
 * @author liangliang
 * @date 2022/12/26
 */
public interface KafkaTopicConstants {

    /**
     * 发送消息主题
     */
    String SEND_MSG_TOPIC = "tsp.message-center.send-message";

    /**
     * 用户上下线主题
     */
    String USER_LOGIN_TOPIC = "topic.avnt.login.vin.message";

    /**
     * mqtt连接状态
     */
    String CDC_MQTT_CONNECT = "tsp.msg.cdc.mqtt.status";
}
