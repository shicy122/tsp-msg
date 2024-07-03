package com.hycan.idn.tsp.message.exception;

import com.hycan.idn.tsp.common.core.constant.CommonConstants;
import lombok.Getter;

/**
 * @description:
 * @BelongsProject: tsp-message-center-service
 * @BelongsPackage: com.hycan.idn.tsp.message.exception
 * @Author: liangwenqi
 * @CreateTime: 2022-06-27  17:02
 * @Version: 1.0
 */
@Getter
public class MsgBusinessException extends RuntimeException {
    /**
     * 编码
     */
    private int code;
    /**
     * 数据
     */
    private Object data;

    /**
     * 处理消息
     *
     * @param msg 消息
     */
    public MsgBusinessException(String msg) {
        super(msg);
        this.code = CommonConstants.FAIL;
    }

    /**
     * 处理消息、编码
     *
     * @param msg  消息
     * @param code 代码
     */
    public MsgBusinessException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    /**
     * 处理消息、编码、数据
     *
     * @param msg  消息
     * @param code 代码
     * @param data 数据
     */
    public MsgBusinessException(String msg, int code, Object data) {
        super(msg);
        this.code = code;
        this.data = data;
    }

}
