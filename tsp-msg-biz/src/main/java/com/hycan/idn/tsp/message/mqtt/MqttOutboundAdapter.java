package com.hycan.idn.tsp.message.mqtt;

import com.hycan.idn.tsp.message.config.MqttConfig;
import com.hycan.idn.tsp.message.facade.MqttxRemoteFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * mqtt出站配置
 *
 * @author liangliang
 * @date 2022/12/26
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class MqttOutboundAdapter {

    public static final String PUB_CLIENT_ID = "T_MSG_PUB_" + System.currentTimeMillis();

    private final MqttConfig mqttConfig;

    private final MqttxRemoteFacade mqttxRemoteFacade;

    /**
     * mqtt出站Handler
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConfig.OUTBOUND_CHANNEL)
    public MessageHandler mqttOutbound() {

        final MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(PUB_CLIENT_ID, mqttOutboundClientFactory());
        messageHandler.setDefaultQos(1);
        //如果设置成true，发送消息时将不会阻塞。
        messageHandler.setAsync(true);
        // 消息发送和传输完成会有异步的通知回调
        messageHandler.setAsyncEvents(true);
        return messageHandler;
    }

    /**
     * mqtt出站通道
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = MqttConfig.OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 注册MQTT客户端工厂, 注意发布者和订阅者的客户端ID不能重复
     */
    @Bean
    public MqttPahoClientFactory mqttOutboundClientFactory() {
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setServerURIs(mqttConfig.getUrl());
        options.setUserName(mqttConfig.getUsername());
        String pwd = mqttxRemoteFacade.searchEncryptPwd(PUB_CLIENT_ID);
        options.setPassword(pwd.toCharArray());
        options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        factory.setConnectionOptions(options);
        return factory;
    }
}
