package com.hycan.idn.tsp.message.pojo.publishtask;

import lombok.Data;

import java.time.LocalDate;

/**
 * 待发布的萌宠播报任务DTO
 *
 * @author Shadow
 * @datetime 2024-03-15 14:50
 */
@Data
public class BroadcastTaskPublishedDTO {

    /** 萌宠播报任务ID */
    private Long broadcastTaskId;

    /** 任务ID */
    private Long baseTaskId;

    /** 执行频率 (ONE_TIME:推送一次, EVERY_DAY:每天一次) */
    private String execRate;

    /** 任务名称 */
    private String taskName;

    /** 消息标题 */
    private String msgTitle;

    /** 消息内容 */
    private String msgContent;

    /** 执行策略(AVNT_ONLINE:车辆上电, USER_LOGIN:账号登陆) */
    private String execPolicy;

    /** 发送范围(SINGLE:单个VIN, MULTIPLE:批量VIN, COMBINATION:组合条件) */
    private String scope;

    /** 开始时间 */
    private LocalDate startDate;

    /** 结束时间 */
    private LocalDate endDate;

    /** 节假日类型 */
    private String holidayType;
}
