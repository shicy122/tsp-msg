package com.hycan.idn.tsp.message.mqtt;

import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.facade.MqttxRemoteFacade;
import com.hycan.idn.tsp.message.service.MsgSendRecordService;
import com.hycan.idn.tsp.message.service.TaskExecRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttMessageSentEvent;
import org.springframework.integration.mqtt.event.MqttProtocolErrorEvent;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MQTT Broker回调监听器
 *
 * @author shichongying
 * @datetime 2023年 02月 26日 9:45
 */
@Slf4j
@Component
public class MqttCallbackListener {

    @Resource
    private MqttxRemoteFacade mqttxRemoteFacade;

    @Resource
    private MsgSendRecordService msgSendRecordService;

    @Resource
    private TaskExecRecordService taskExecRecordService;

    /**
     * mqtt连接失败或者订阅失败时, 触发MqttConnectionFailedEvent事件
     */
    @EventListener(MqttConnectionFailedEvent.class)
    public void mqttConnectionFailedEvent(MqttConnectionFailedEvent event) {
        log.error("MQTT连接错误: 发生时间=[{}], 错误源=[{}], 错误详情={}", LocalDateTime.now(), event.getSource(), event.getCause().getMessage());
        if (event.getSource() instanceof MqttPahoMessageHandler) {
            MqttPahoMessageHandler handler = (MqttPahoMessageHandler) event.getSource();
            String pubPwd = mqttxRemoteFacade.searchEncryptPwd(MqttOutboundAdapter.PUB_CLIENT_ID);
            handler.getConnectionInfo().setPassword(pubPwd.toCharArray());
        } else if (event.getSource() instanceof MqttPahoMessageDrivenChannelAdapter) {
            MqttPahoMessageDrivenChannelAdapter adapter = (MqttPahoMessageDrivenChannelAdapter) event.getSource();
            String subPwd = mqttxRemoteFacade.searchEncryptPwd(MqttInboundAdapter.SUB_CLIENT_ID);
            adapter.getConnectionInfo().setPassword(subPwd.toCharArray());
        }
    }

    /**
     * mqtt事件回调 修改信息发送状态
     */
    @EventListener(classes = MqttMessageSentEvent.class)
    public void listenerAction(MqttMessageSentEvent mqttMessageSentEvent) {
        Object msgId = mqttMessageSentEvent.getMessage().getHeaders().get("msgId");
        if (Objects.nonNull(msgId)) {
            log.info("MQTT消息发送成功: 消息ID=[{}]", msgId);
            msgSendRecordService.addBlockingQueue(msgId.toString(), MsgSendStatusConstant.SUCCESS);
            taskExecRecordService.addBlockingQueue(msgId.toString(), MsgSendStatusConstant.SUCCESS);
        }
    }

    /**
     * 客户端交互期间发生 MQTT 错误
     */
    @EventListener(MqttProtocolErrorEvent.class)
    public void mqttProtocolErrorEvent(MqttProtocolErrorEvent event) {
        log.error("MQTT交互错误: 发生时间=[{}], 错误源=[{}], 错误详情=[{}]", LocalDateTime.now(), event.getSource(), event.getCause().getMessage());
    }
}
