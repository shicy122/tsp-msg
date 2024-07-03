package com.hycan.idn.tsp.message.constant;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-22 13:49
 */
public interface TaskStatusConstants {
    /**
     * 已创建
     */
    String CREATED = "CREATED";

    /**
     * 已发布
     */
    String PUBLISHED = "PUBLISHED";

    /**
     * 已结束
     */
    String FINISHED = "FINISHED";

    /**
     * 推送中
     */
    String SENDING = "SENDING";

    /**
     * 已撤回
     */
    String CANCELED = "CANCELED";
}
