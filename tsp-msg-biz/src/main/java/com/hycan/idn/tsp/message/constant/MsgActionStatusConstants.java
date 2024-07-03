package com.hycan.idn.tsp.message.constant;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-22 15:26
 */
public interface MsgActionStatusConstants {

    /**
     * 车辆与mqtt平台连接状态 连接
     */
    String AVNT_STATUS_CONNECTED = "CONNECTED";

    /**
     * 车辆与mqtt平台连接状态 断开连接
     */
    String AVNT_STATUS_DISCONNECTED = "DISCONNECTED";

    /**
     * 车辆用户登录状态  登录
     */
    String USER_LOGIN_STATUS_ONLINE = "ON_LINE";

    /**
     * 车辆用户登录状态 退出
     */
    String USER_LOGIN_STATUS_OFFLINE = "OFF_LINE";

    String MQTT_CONNECT_STATUS = "MQTT_STATUS";

    String USER_LOGIN_STATUS = "LOGIN_STATUS";
}
