package com.hycan.idn.tsp.message.event.listener;

import com.hycan.idn.common.core.util.JsonUtil;
import com.hycan.idn.tsp.message.config.RabbitMqConfig;
import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity.ForwardConfig;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.pojo.avntmqtt.SendRabbitMsgDTO;
import com.hycan.idn.tsp.message.event.SendMsgEvent;
import com.hycan.idn.tsp.message.rabbitmq.RabbitBlockedListener;
import com.hycan.idn.tsp.message.service.MsgSendRecordService;
import com.hycan.idn.tsp.message.rabbitmq.RabbitConfirmCallback;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.hycan.idn.tsp.message.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送消息到RabbitMQ监听器
 *
 * @author shichongying
 */
@Slf4j
@Component
public class SendMsg2RabbitListener {

    @Resource
    private RabbitConfirmCallback rabbitmqCallbackListener;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMqConfig rabbitMqConfig;

    @Resource
    private MsgSendRecordService msgSendRecordService;

    /**
     * 处理监听事件
     *
     * @param event 事件
     */
    @Async(ThreadPoolUtil.MSG_RABBITMQ_THREAD)
    @EventListener(value = SendMsgEvent.class, condition = "#event.forwardConfig.protocol=='RABBITMQ'")
    public void handling(SendMsgEvent event) {
        try {
            SendMsgDTO sendMsgDTO = event.getSendMsgDTO();
            ForwardConfig forwardConfig = event.getForwardConfig();

            publish(sendMsgDTO, forwardConfig);
        } catch (Exception e) {
            log.error("RabbitMQ消息监听事件异常, 异常详情={}", ExceptionUtil.getBriefStackTrace(e));
        }
    }

    public void publish(SendMsgDTO msgDTO, ForwardConfig forwardConfig) {
        if (RabbitBlockedListener.isBlocked) {
            log.error("RabbitMQ服务端目前为阻塞状态, 消息发送失败, 消息ID=[{}]", msgDTO.getMsgId());
            return;
        }
        try {
            rabbitTemplate.setConfirmCallback(rabbitmqCallbackListener);
            rabbitTemplate.convertAndSend(
                    rabbitMqConfig.getMsgBizExchange(),
                    forwardConfig.getRoutingKey(),
                    JsonUtil.writeValueAsString(SendRabbitMsgDTO.of(msgDTO)),
                    new CorrelationData(msgDTO.getMsgId()));
        } catch (Exception e) {
            log.error("发送RabbitMQ消息失败, 异常详情={}", ExceptionUtil.getBriefStackTrace(e));
            msgSendRecordService.addBlockingQueue(msgDTO.getMsgId(), MsgSendStatusConstant.FAILURE);
        }
    }
}
