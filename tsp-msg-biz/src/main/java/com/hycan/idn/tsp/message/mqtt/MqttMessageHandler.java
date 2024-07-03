package com.hycan.idn.tsp.message.mqtt;

import com.hycan.idn.tsp.common.core.util.SpringContextHolder;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.constant.MsgActionStatusConstants;
import com.hycan.idn.tsp.message.event.ChangeStatusEvent;
import com.hycan.idn.tsp.message.pojo.avntmqtt.SysNoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 订阅MQTT Broker消息处理器
 *
 * @author shichongying
 * @datetime 2023年 02月 27日 10:14
 */
@Slf4j
@Component
public class MqttMessageHandler {

    private static final Pattern SYS_CONNECT_TOPIC = Pattern.compile("^\\$SYS/broker/(.+)/clients/(.+)/(connected|disconnected)$");

    private static final int SYS_TOPIC_CLIENT_ID_INDEX = 2;

    private static final int SYS_TOPIC_TYPE_INDEX = 3;

    private static final String DISCONNECT = "disconnected";

    private static final String CONNECT = "connected";

    public void handleMessage(Message<?> message) {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        byte[] data = (byte[]) message.getPayload();

        if (!StringUtils.hasText(topic) || data.length <= 0) {
            log.error("MQTT消息格式错误! Topic=[{}], Payload=[{}]", topic, data);
            return;
        }

        // 系统主题-上下线消息
        if (isSysNotice(topic)) {
            handleSysMessage(topic);
        }
    }

    /**
     * 系统主题-MQTT客户端上下线逻辑
     */
    private void handleSysMessage(String topic) {
        SysNoticeDTO sysNotice = getSysNotice(topic);
        if (Objects.isNull(sysNotice)) {
            return;
        }

        /*
        clientId三种规则：
        1）远峰硬件+系统埋点上报：avnt_LMWT31S50N1S000411
        2）德赛消息中心：avnt_LMWT31S50N1S00041_2
        3) 德赛应用埋点：cdc_LMWT31S50N1S00041_2
        此处只处理车机消息中心的状态改变
         */
        String clientId = sysNotice.getClientId();
        if (!(clientId.startsWith(CommonConstants.AVNT) && clientId.endsWith("_2"))) {
            return;
        }

        String vin = clientId.substring(5, clientId.length() - 2);
        if (CONNECT.equals(sysNotice.getType())) {
            SpringContextHolder.publishEvent(
                    new ChangeStatusEvent(this, vin, MsgActionStatusConstants.AVNT_STATUS_CONNECTED));
        } else if (DISCONNECT.equals(sysNotice.getType())) {
            SpringContextHolder.publishEvent(
                    new ChangeStatusEvent(this, vin, MsgActionStatusConstants.AVNT_STATUS_DISCONNECTED));
        }
    }

    private boolean isSysNotice(String topic) {
        return SYS_CONNECT_TOPIC.matcher(topic).matches();
    }

    private SysNoticeDTO getSysNotice(String topic) {
        Matcher matcher = SYS_CONNECT_TOPIC.matcher(topic);
        if (!matcher.matches()) {
            return null;
        }

        String clientId = matcher.group(SYS_TOPIC_CLIENT_ID_INDEX);
        String type = matcher.group(SYS_TOPIC_TYPE_INDEX);
        return SysNoticeDTO.of(type, clientId);
    }
}
