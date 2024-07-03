package com.hycan.idn.tsp.message.rabbitmq;

import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.service.MsgSendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Liuyingjie
 * @datetime 2022/9/22 16:10
 */
@Slf4j
@Component
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

    @Resource
    private MsgSendRecordService msgSendRecordService;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (Objects.isNull(correlationData)) {
            return;
        }

        String msgId = correlationData.getId();
        if (ack) {
            log.info("RabbitMQ消息发送成功: 消息ID=[{}]", msgId);
            msgSendRecordService.addBlockingQueue(msgId, MsgSendStatusConstant.SUCCESS);
        } else {
            log.info("RabbitMQ消息发送失败: 消息ID=[{}], 原因=[{}]", msgId, cause);
            msgSendRecordService.addBlockingQueue(msgId, MsgSendStatusConstant.FAILURE);
        }
    }
}
