package com.hycan.idn.tsp.message.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取MQTT Topic列表接口响应DTO
 * @BelongsProject: tsp-message-center-service
 * @BelongsPackage: com.hycan.idn.tsp.message.dto.response
 * @Author: liangwenqi
 * @CreateTime: 2022-06-09  15:59
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttTopicsRspDTO {

    /**
     * 推送Topic列表(上行)
     */
    private List<MqttTopicDTO> pubTopics;

    /**
     * 订阅Topic列表(下行)
     */
    private List<MqttTopicDTO> subTopics;

    @Data
    public static class MqttTopicDTO {
        /**
         * 业务类型值
         */
        private String bussType;

        /**
         * MQTT主题
         */
        private String topic;

        /**
         * 消息质量（0：最多一次，1：最少一次，2：精确一次）
         */
        private Integer qos;
    }
}
