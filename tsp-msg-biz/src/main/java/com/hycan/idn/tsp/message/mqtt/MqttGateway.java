package com.hycan.idn.tsp.message.mqtt;

import com.hycan.idn.tsp.message.config.MqttConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * mqtt网关
 *
 * @author liangliang
 * @date 2022/12/26
 */
@Component
@MessagingGateway(defaultRequestChannel = MqttConfig.OUTBOUND_CHANNEL)
public interface MqttGateway {

    /**
     * 发送消息到MQTT Broker
     * @param topic
     * @param qos 对消息处理的几种机制。
     * @param payload json串
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    @Header(MqttHeaders.QOS) int qos, String payload,
                    @Header("msgId") String msgId );
}
