package com.hycan.idn.tsp.message.pojo.publishtask;

import lombok.Data;

/**
 * 场景卡片消息 Payload DTO
 *
 * @author Shadow
 * @datetime 2024-03-21 15:25
 */
@Data
public class SceneCardTaskPayloadDTO {
    // @formatter:off

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;

    /** 配图URL */
    private String picUrl;

    /** 图标 */
    private String icon;

    /** 消息类型(IMAGE_TEXT_MSG) */
    private String type;

    /** 消息细节 */
    private String detail;

    /** 触发动作 */
    private String actions;

    // @formatter:on
}
