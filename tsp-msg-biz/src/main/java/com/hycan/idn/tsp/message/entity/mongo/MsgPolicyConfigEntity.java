package com.hycan.idn.tsp.message.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 消息发送策略配置
 *
 * @author liangliang
 * @datetime 2022/12/26 9:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "msg_policy_config")
@CompoundIndexes({
        @CompoundIndex(name = "buss_type_serials", def = "{'buss_type':1,'serials':1}")
})
public class MsgPolicyConfigEntity {
    // @formatter:off

    /** id */
    @Id
    private String id;

    /** 业务类型值 */
    @Field(name = "buss_type")
    private String bussType;

    /** 执行策略 */
    @Field(name = "exec_policy")
    private List<String> execPolicy;

    /** 车型编号列表 */
    @Field(name = "serials")
    private List<String> serials;

    /** 转发配置策略 */
    @Field(name = "forward_configs")
    private List<ForwardConfig> forwardConfigs;

    /** 描述 */
    @Field(name = "desc")
    private String desc;

    // @formatter:off

    @Data
    public static class ForwardConfig {
        // @formatter:on

        /** 协议类型 */
        @Field(name = "protocol")
        private String protocol;

        /** MQTT主题 */
        @Field(name = "topic")
        private String topic;

        /** MQTT消息质量(0:最多一次、1:最少一次、2:精确一次) */
        @Field(name = "qos")
        private Integer qos;

        /** 上下行(up、down) */
        @Field(name = "direction")
        private String direction;

        /** 转发目标(ACP/AVNT) */
        @Field(name = "target")
        private String target;

        /** rabbitmq routingKey */
        @Field(name = "routingKey")
        private String routingKey;

        // @formatter:off
    }

}


