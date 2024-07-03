package com.hycan.idn.tsp.message.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 * @author shichongying
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rabbitmq.config")
public class RabbitMqConfig {

    /**
     * 交换机
     */
    private String msgBizExchange;

    /**
     * VCP队列
     */
    private String msgBizVCPQueue;
    /**
     * DMS队列
     */
    private String msgBizDMSQueue;

    /**
     * VCP  routingKey
     */
    private String msgBizVCPRoutingKey;
    /**
     * DMS routingKey
     */
    private String msgBizDMSRoutingKey;
    /**
     * 转发两个队列 routingKey
     */
    private String msgBizMsgRoutingKey;

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建topic交换机
     */
    @Bean
    public Exchange msgBizExchange() {
        return new TopicExchange(msgBizExchange, true, false);
    }

    /**
     * 创建VCP队列
     */
    @Bean
    public Queue msgBizVCPQueue() {
        return new Queue(msgBizVCPQueue, true, false, false);
    }

    /**
     * 绑定VCP队列与交换机 VCP  routingKey
     */
    @Bean
    public Binding msgBizVCPQueueBinding() {
        return new Binding(msgBizVCPQueue, Binding.DestinationType.QUEUE,
                msgBizExchange, msgBizVCPRoutingKey, null);
    }

    /**
     * 绑定VCP队列与交换机 通用  routingKey
     */
    @Bean
    public Binding msgBizVCPQueueBinding2() {
            return new Binding(msgBizVCPQueue, Binding.DestinationType.QUEUE,
                    msgBizExchange, msgBizMsgRoutingKey, null);
    }

    /**
     * 创建DMS队列
     */
    @Bean
    public Queue msgBizDMSQueue() {
        return new Queue(msgBizDMSQueue, true, false, false);
    }

    /**
     * 绑定DMS队列与交换机 DMS  routingKey
     */
    @Bean
    public Binding msgBizDMSQueueBinding() {
        return new Binding(msgBizDMSQueue, Binding.DestinationType.QUEUE,
                msgBizExchange, msgBizDMSRoutingKey,null);
    }

    /**
     * 绑定DMS队列与交换机 通用  routingKey
     */
    @Bean
    public Binding msgBizDMSQueueBinding2() {
        return new Binding(msgBizDMSQueue, Binding.DestinationType.QUEUE,
                msgBizExchange, msgBizMsgRoutingKey,null);
    }
}
