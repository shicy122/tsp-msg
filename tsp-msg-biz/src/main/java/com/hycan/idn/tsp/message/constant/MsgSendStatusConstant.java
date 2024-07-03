package com.hycan.idn.tsp.message.constant;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-22 15:03
 */
public interface MsgSendStatusConstant {

    /**
     * 消息发送成功状态值
     */
    String SUCCESS = "SUCCESS";

    /**
     * 消息发送失败状态值
     */
    String FAILURE = "FAILURE";

    /**
     * 未推送
     */
    String UNSENT = "UNSENT";

    /**
     * 推送中
     */
    String SENDING = "SENDING";
}
