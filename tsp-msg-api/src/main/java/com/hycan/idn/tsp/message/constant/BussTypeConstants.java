package com.hycan.idn.tsp.message.constant;

/**
 * 业务类型常量类
 *
 * @author shichongying
 */
public interface BussTypeConstants {

    /**
     * 发送扫码成功返回
     */
    String SCAN_CODE_SUCCESS = "SCAN_CODE_SUCCESS";

    /**
     * 发送用户要打开的url地址
     */
    String PUSH_DELIVERY_URL = "PUSH_DELIVERY_URL";

    /**
     * 发送POI
     */
    String SEND_POI = "SEND_POI";

    /**
     * 数据消息 tsp-msg 刘英杰
     */
    String DATA_MSG = "DATA_MSG";

    /**
     * 图文消息 tsp-msg 梁文琪
     */
    String IMAGE_TEXT_MSG = "IMAGE_TEXT_MSG";

    /**
     * 车况信息
     */
    String VEHICLE_CONDITION = "VEHICLE_CONDITION";

    /**
     * 车控指令
     */
    String VEHICLE_CONTROL = "VEHICLE_CONTROL";

    /**
     * 异常告警
     */
    String ABNORMAL_ALARM = "ABNORMAL_ALARM";

    /**
     * 实名认证
     */
    String REAL_NAME = "REAL_NAME";

    /**
     * 授权到期
     */
    String AUTH_EXPIRE = "AUTH_EXPIRE";

    /**
     * 流量告警
     */
    String FLOW_ALARM = "FLOW_ALARM";

    /**
     * OTA任务/状态/结果 tsp-vms  张增煌
     */
    String OTA = "OTA";

    /**
     * 售后换件  tsp-composite 张增煌
     */
    String REPLACEMENT = "REPLACEMENT";

    /**
     * 同步车辆状态扭转
     */
    String VEHICLE_STATUS = "VEHICLE_STATUS";

    /**
     * 事件型车况消息
     */
    String VEHICLE_CONDITION_REPORT = "VEHICLE_CONDITION_REPORT";

    /**
     * 车机应用埋点数据上传消息
     */
    String COLLECT_APP_MSG = "COLLECT_APP_MSG";

    /**
     * 车机系统埋点数据上传消息
     */
    String COLLECT_SYS_MSG = "COLLECT_SYS_MSG";

    /**
     * 车机硬件资源数据上传消息
     */
    String COLLECT_RESOURCE_MSG = "COLLECT_RESOURCE_MSG";

    /**
     * 发送采集上传配置消息  tsp-collector  陈田
     */
    String COLLECT_MSG = "COLLECT_MSG";

    /**
     * 哨兵模式
     */
    String SENTRY_MODEL = "SENTRY_MODEL";
}
