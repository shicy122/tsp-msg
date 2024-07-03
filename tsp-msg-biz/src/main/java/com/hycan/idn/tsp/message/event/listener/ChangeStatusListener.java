package com.hycan.idn.tsp.message.event.listener;

import com.hycan.idn.common.core.util.JsonUtil;
import com.hycan.idn.tsp.message.constant.KafkaTopicConstants;
import com.hycan.idn.tsp.message.constant.MsgActionStatusConstants;
import com.hycan.idn.tsp.message.constant.RedisKeyConstants;
import com.hycan.idn.tsp.message.entity.mongo.TaskDetailRecordEntity;
import com.hycan.idn.tsp.message.entity.mongo.TaskExecRecordEntity;
import com.hycan.idn.tsp.message.event.ChangeStatusEvent;
import com.hycan.idn.tsp.message.facade.SendMsgFacade;
import com.hycan.idn.tsp.message.pojo.CdcMqttStatus;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.repository.mongo.TaskDetailRecordRepository;
import com.hycan.idn.tsp.message.repository.mongo.TaskExecRecordRepository;
import com.hycan.idn.tsp.message.service.TaskExecRecordService;
import com.hycan.idn.tsp.message.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.hycan.idn.tsp.message.constant.MsgActionStatusConstants.*;
import static com.hycan.idn.tsp.message.pojo.CdcMqttStatus.OFFLINE;
import static com.hycan.idn.tsp.message.pojo.CdcMqttStatus.ONLINE;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-25 14:55
 */
@Slf4j
@Component
public class ChangeStatusListener {

    public static final String AVNT_ONLINE = "AVNT_ONLINE";
    public static final String USER_LOGIN = "USER_LOGIN";

    @Resource
    private TaskExecRecordRepository taskExecRecordRepository;

    @Resource
    private TaskDetailRecordRepository taskDetailRecordRepository;

    @Resource
    private SendMsgFacade sendMsgFacade;

    @Resource
    private TaskExecRecordService taskExecRecordService;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Async
    @EventListener(value = ChangeStatusEvent.class)
    public void handling(ChangeStatusEvent event) {
        String vin = event.getVin();
        String type = event.getType();

        switch (type) {
            case AVNT_STATUS_DISCONNECTED:
                log.info("CDC MQTT连接断开, VIN=[{}]", vin);
                redisUtil.hashSet(RedisKeyConstants.CDC_STATUS_KEY + vin, AVNT_ONLINE, OFFLINE);
                kafkaTemplate.send(KafkaTopicConstants.CDC_MQTT_CONNECT, vin,
                                JsonUtil.writeValueAsString(CdcMqttStatus.of(vin, OFFLINE)));
                break;
            case AVNT_STATUS_CONNECTED:
                log.info("CDC MQTT连接成功, VIN=[{}]", vin);
                redisUtil.hashSet(RedisKeyConstants.CDC_STATUS_KEY + vin, AVNT_ONLINE, ONLINE);
                sendMsg(vin, AVNT_ONLINE);
                kafkaTemplate.send(KafkaTopicConstants.CDC_MQTT_CONNECT, vin,
                        JsonUtil.writeValueAsString(CdcMqttStatus.of(vin, ONLINE)));
                break;
            case USER_LOGIN_STATUS_ONLINE:
                log.info("CDC 用户账号登录, VIN=[{}]", vin);
                redisUtil.hashSet(RedisKeyConstants.CDC_STATUS_KEY + vin, USER_LOGIN, ONLINE);
                sendMsg(vin, USER_LOGIN);
                break;
            case MsgActionStatusConstants.USER_LOGIN_STATUS_OFFLINE:
                redisUtil.hashSet(RedisKeyConstants.CDC_STATUS_KEY + vin, USER_LOGIN, OFFLINE);
                log.info("CDC 用户账号登出, VIN=[{}]", vin);
                break;
        }
    }

    public void sendMsg(String vin, String execPolicy) {
        List<TaskExecRecordEntity> execRecordList = taskExecRecordRepository.findUnsentTasksByVin(vin, LocalDateTime.now());
        if (CollectionUtils.isEmpty(execRecordList)) {
            return;
        }

        execRecordList.forEach(execRecord -> {
            TaskDetailRecordEntity detailRecord = taskDetailRecordRepository.findDetailRecord(execRecord.getBaseTaskId(), execPolicy);
            if (detailRecord == null) {
                return;
            }

            SendMsgDTO sendMsgDTO = SendMsgDTO.of(vin, detailRecord.getBussType(), detailRecord.getPayload());
            sendMsgFacade.publish(sendMsgDTO);

            taskExecRecordService.updateTaskExecRecord(execRecord.getId(), sendMsgDTO.getMsgId());
        });
    }
}
