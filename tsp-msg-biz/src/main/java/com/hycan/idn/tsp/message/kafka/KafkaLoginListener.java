package com.hycan.idn.tsp.message.kafka;

import com.hycan.idn.tsp.common.core.util.GSON;
import com.hycan.idn.tsp.common.core.util.SpringContextHolder;
import com.hycan.idn.tsp.message.constant.KafkaTopicConstants;
import com.hycan.idn.tsp.message.constant.MsgActionStatusConstants;
import com.hycan.idn.tsp.message.event.ChangeStatusEvent;
import com.hycan.idn.tsp.message.pojo.avntmqtt.UserLoginDto;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * kafka 账号登录消息消费端
 *
 * @author Liuyingjie
 * @datetime 2022/6/20 13:55
 */
@Slf4j
@Component
public class KafkaLoginListener implements BatchAcknowledgingMessageListener<String, String> {

    private static final Integer USER_LOGIN_STATUS_ONLINE = 0;

    /**
     * kafka消费者
     */
    @Override
    @KafkaListener(topics = KafkaTopicConstants.USER_LOGIN_TOPIC, concurrency = "3")
    public void onMessage(List<ConsumerRecord<String, String>> list, Acknowledgment ack) {
        try {
            for (final ConsumerRecord<String, String> record : list) {
                final UserLoginDto userLoginDto = GSON.parseObject(record.value(), UserLoginDto.class);
                log.info("用户上下线状态{}",userLoginDto);

                String vin = userLoginDto.getVin();
                if (USER_LOGIN_STATUS_ONLINE.equals(userLoginDto.getLogin())) {
                    SpringContextHolder.publishEvent(
                            new ChangeStatusEvent(this, vin, MsgActionStatusConstants.USER_LOGIN_STATUS_ONLINE));
                } else {
                    SpringContextHolder.publishEvent(
                            new ChangeStatusEvent(this, vin, MsgActionStatusConstants.USER_LOGIN_STATUS_ONLINE));
                }
            }
        } catch (Exception e) {
            log.error("处理Kafka消息失败, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
        } finally {
            ack.acknowledge();
        }
    }
}
