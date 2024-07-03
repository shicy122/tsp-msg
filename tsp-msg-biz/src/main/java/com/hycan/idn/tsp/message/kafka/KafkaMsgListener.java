package com.hycan.idn.tsp.message.kafka;

import com.hycan.idn.common.core.util.JsonUtil;
import com.hycan.idn.tsp.message.constant.KafkaTopicConstants;
import com.hycan.idn.tsp.message.facade.SendMsgFacade;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * kafka 消息消费端
 *
 * @author Liuyingjie
 * @datetime 2022/6/20 13:55
 */
@Slf4j
@Component
public class KafkaMsgListener implements BatchAcknowledgingMessageListener<String, String> {

    @Resource
    private SendMsgFacade sendMsgFacade;

    /**
     * kafka消费者
     */
    @Override
    @KafkaListener(topics = KafkaTopicConstants.SEND_MSG_TOPIC, concurrency = "3")
    public void onMessage(@NonNull List<ConsumerRecord<String, String>> records, @NonNull Acknowledgment ack) {
        try {
            for (final ConsumerRecord<String, String> record : records) {
                sendMsgFacade.publish(JsonUtil.readValue(record.value(), SendMsgDTO.class));
            }
        } catch (Exception e) {
            log.error("处理Kafka消息失败, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
        } finally {
            ack.acknowledge();
        }
    }

}
