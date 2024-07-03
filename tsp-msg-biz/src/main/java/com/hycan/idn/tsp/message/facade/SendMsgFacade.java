package com.hycan.idn.tsp.message.facade;

import com.hycan.idn.tsp.common.core.util.SpringContextHolder;
import com.hycan.idn.tsp.message.cache.PolicyConfigCache;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity.ForwardConfig;
import com.hycan.idn.tsp.message.event.SendMsgEvent;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.service.MsgSendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-28 15:27
 */
@Slf4j
@Component
public class SendMsgFacade {

    private static final String DIRECTION_UP = "up";

    @Resource
    private RedissonFacade redissonFacade;

    @Resource
    private PolicyConfigCache policyConfigCache;

    @Resource
    private MsgSendRecordService msgSendRecordService;

    public void publish(SendMsgDTO sendMsgDTO) {
        publish0(sendMsgDTO, false);
    }

    public void republish(SendMsgDTO sendMsgDTO) {
        publish0(sendMsgDTO, true);
    }

    private void publish0(SendMsgDTO sendMsgDTO, boolean isRetry) {
        if (!validData(sendMsgDTO)) {
            return;
        }

        String bussType = sendMsgDTO.getBussType();
        String vin = sendMsgDTO.getVin();
        String msgId = sendMsgDTO.getMsgId();

        List<ForwardConfig> forwardConfigList = policyConfigCache.getForwardConfigList(bussType, vin);
        if (CollectionUtils.isEmpty(forwardConfigList)) {
            return;
        }

        // 将消息添加到队列暂存
        if (!isRetry) {
            msgSendRecordService.addBlockingQueue(sendMsgDTO);
        }

        log.info("开始处理消息, 业务类型={}、VIN={}、消息ID={}", bussType, vin, msgId);
        for (ForwardConfig forwardConfig : forwardConfigList) {
            if (DIRECTION_UP.equals(forwardConfig.getDirection())) {
                continue;
            }

            // 推送消息到MQTT/RabbitMQ
            SpringContextHolder.publishEvent(new SendMsgEvent(this, sendMsgDTO, forwardConfig));
        }
    }

    /**
     * 校验数据
     */
    public boolean validData(SendMsgDTO sendMsgDTO) {
        // 判断关键参数是否为空
        if (Objects.isNull(sendMsgDTO.getVin()) || Objects.isNull(sendMsgDTO.getBussType())
                || Objects.isNull(sendMsgDTO.getMsgId()) || Objects.isNull(sendMsgDTO.getReportTime())) {
            log.warn("消息处理异常: 关键参数不能为空! VIN=[{}]、业务类型=[{}]、 消息ID=[{}]、上报时间=[{}]",
                    sendMsgDTO.getVin(), sendMsgDTO.getBussType(), sendMsgDTO.getMsgId(), sendMsgDTO.getReportTime());
            return false;
        }

        // 校验消息ID是否重复
        if (redissonFacade.isDuplicateMsg(sendMsgDTO.getMsgId(), sendMsgDTO.getBussType())) {
            log.warn("消息处理异常: 消息重复! 业务类型={}、消息ID={}!", sendMsgDTO.getBussType(), sendMsgDTO.getMsgId());
            return false;
        }

        return true;
    }
}
