package com.hycan.idn.tsp.message.event.listener;

import com.hycan.idn.common.core.util.JsonUtil;
import com.hycan.idn.tsp.message.cache.PayloadSecretCache;
import com.hycan.idn.tsp.message.cache.VehBaseInfoCache;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity.ForwardConfig;
import com.hycan.idn.tsp.message.mqtt.MqttGateway;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.pojo.avntmqtt.SendMqttMsgDTO;
import com.hycan.idn.tsp.message.event.SendMsgEvent;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.service.MsgSendRecordService;
import com.hycan.idn.tsp.message.service.TaskExecRecordService;
import com.hycan.idn.tsp.message.utils.AESUtil;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.hycan.idn.tsp.message.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送消息到MQTT监听器
 *
 * @author shichongying
 */
@Slf4j
@Component
public class SendMsg2MqttListener {

    @Resource
    private MsgSendRecordService msgSendRecordService;

    @Resource
    private TaskExecRecordService taskExecRecordService;

    @Resource
    private MqttGateway mqttGateway;

    @Resource
    private VehBaseInfoCache vehBaseInfoCache;

    @Resource
    private PayloadSecretCache payloadSecretCache;

    /**
     * 处理监听事件
     *
     * @param event 事件
     */
    @Async(ThreadPoolUtil.MSG_MQTT_THREAD)
    @EventListener(value = SendMsgEvent.class, condition = "#event.forwardConfig.protocol=='MQTT'")
    public void handling(SendMsgEvent event) {
        try {
            SendMsgDTO sendMsgDTO = event.getSendMsgDTO();
            ForwardConfig forwardConfig = event.getForwardConfig();

            publish(sendMsgDTO, forwardConfig);
        } catch (Exception e) {
            log.error("MQTT消息监听事件异常,异常详情={}", ExceptionUtil.getExceptionStackTrace(e, 30));
        }
    }

    private void publish(SendMsgDTO sendMsgDTO, ForwardConfig forwardConfig) {
        try {
            String payload = buildPayload(sendMsgDTO);
            if (StringUtils.isBlank(payload)) {
                throw new MsgBusinessException(sendMsgDTO.getMsgId() + "数据加密数据为空");
            }

            String serial = vehBaseInfoCache.getSerialByVin(sendMsgDTO.getVin());
            String topic = forwardConfig.getTopic()
                    .replace(CommonConstants.SERIAL_PLACEHOLDER, serial)
                    .replace(CommonConstants.VIN_PLACEHOLDER, sendMsgDTO.getVin());
            mqttGateway.sendToMqtt(topic, forwardConfig.getQos(), payload, sendMsgDTO.getMsgId());
        } catch (Exception e) {
            log.error("MQTT消息发送失败: 消息ID=[{}], 原因=[{}]",
                    sendMsgDTO.getMsgId(), ExceptionUtil.getBriefStackTrace(e));
            msgSendRecordService.addBlockingQueue(sendMsgDTO.getMsgId(), MsgSendStatusConstant.FAILURE);
            taskExecRecordService.addBlockingQueue(sendMsgDTO.getMsgId(), MsgSendStatusConstant.FAILURE);
        }
    }

    private String buildPayload(SendMsgDTO sendMsgDTO) {
        try {
            SendMqttMsgDTO sendMqttMsgDTO = SendMqttMsgDTO.of(sendMsgDTO);
            String mqttSecretKey = payloadSecretCache.getPayloadSecret(sendMsgDTO.getVin());
            return AESUtil.encrypt(mqttSecretKey, JsonUtil.writeValueAsString(sendMqttMsgDTO));
        } catch (Exception e) {
            log.warn("发送MQTT消息到CDC, 加密Payload失败, 消息ID=[{}], 异常详情=[{}]",
                    sendMsgDTO.getMsgId(), ExceptionUtil.getBriefStackTrace(e));
        }
        return null;
    }
}
