package com.hycan.idn.tsp.message.constant;

/**
 * redis常量
 *
 * @author liangliang
 * @datetime 2022/12/26
 */
public interface RedisKeyConstants {

    /**
     * 布隆过滤器key
     */
    String BLOOM_FILTER_NAME = "tsp:msg:bloom:msg_id";

    /**
     * CDC连接状态KEY(注意：此处只以德赛消息中心的连接状态为准)
     */
    String CDC_STATUS_KEY = "tsp:msg:cdc:status:";
}
