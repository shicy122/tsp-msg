package com.hycan.idn.tsp.message.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 状态码和描述
 * @Author:Liuyingjie
 * @Date:2022/6/10
 * @Time:11:09
 */
@Getter
@RequiredArgsConstructor
public enum  ResultCodeTypeEnum {

    /**
     * 状态码=500101
     * 错误描述=车辆VIN号不存在
     */
    VIN_FAIL(500101,"车辆VIN号不存在"),

    /**
     * 状态码=000001
     * 错误描述=服务器内部错误
     */
    SERVER_FAIL(1,"服务器内部错误"),

    /**
     * 状态码=200
     * 描述为空
     */
    SUCCESS(200,""),

    /**
     * 状态码=500104
     * 错误描述=参数不能为空
     */
    PARAM_NON_NULL(500104,"请求参数对象不能存在空值"),

    /**
     * 状态码=500103
     * 错误描述=请求token不合法
     */
    TOKEN_FAIL(500103,"请求Token不合法"),

    /**
     * 状态码=500102
     * 错误描述=车型号serial不存在
     */
    PARAM_FAIL(500102, "请求参数值校验失败");

    /**
     * 类型
     */
    private final int code;
    /**
     * 描述
     */
    private final String description;


}
