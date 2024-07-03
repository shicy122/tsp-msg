package com.hycan.idn.tsp.message.pojo.publishtask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 萌宠播报消息 Payload DTO
 *
 * @author Liuyingjie
 * @datetime 2022/9/4 23:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastTaskPayloadDTO {
    // @formatter:off

    /** 消息标题 */
    private String title;

    /** 消息类型(holiday_msg:节假日消息) */
    private String type;

    /** 消息内容 */
    private String content;

    // @formatter:on
}
