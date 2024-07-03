package com.hycan.idn.tsp.message.feign;

import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.message.constant.ServiceNameConstants;
import com.hycan.idn.tsp.message.pojo.MqttTopicsRspDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 获取MQTT Topic列表Feign接口
 * @BelongsProject: tsp-message-center-service
 * @BelongsPackage: com.hycan.idn.tsp.message.feign
 * @Author: liangwenqi
 * @CreateTime: 2022-06-16 14:32
 * @Version: 1.0
 */
@FeignClient(contextId = "remoteMqttService", value = ServiceNameConstants.TSP_MESSAGE_CENTER_SERVICE)
public interface RemoteMsgMqttService {

    /**
     * 获取MQTT Topic列表
     * @param token JWT Token
     * @param from SecurityConstants.FROM_IN
     * @return MQTT Topic列表
     */
    @GetMapping("/tsp-api/msg-center-svc/v1/mqtt-topics")
    R<MqttTopicsRspDTO> getMqttTopics(@RequestParam("clientId") String clientId,
                                      @RequestParam("token") String token,
                                      @RequestHeader(SecurityConstants.FROM)String from);
}
