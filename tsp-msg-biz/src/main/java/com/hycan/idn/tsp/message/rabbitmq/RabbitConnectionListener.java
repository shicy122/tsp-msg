package com.hycan.idn.tsp.message.rabbitmq;

import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Liuyingjie
 * @datetime 2022/10/14 16:22
 */
@Slf4j
@Component
public class RabbitConnectionListener implements ConnectionListener {
    /**
     * rabbitmq callbcak侦听器
     */
    @Resource
    private RabbitBlockedListener blockedListener;

    @Override
    public void onCreate(Connection connection) {
        log.info("=====onCreate={}", connection);
        connection.addBlockedListener(blockedListener);
    }

    @Override
    public void onClose(Connection connection) {
        log.info("=====onClose={}", connection);
    }

    @Override
    public void onShutDown(ShutdownSignalException signal) {
        log.info("=====onShutDown={}", signal);
    }
}
