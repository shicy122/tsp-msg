package com.hycan.idn.tsp.message.pojo.publishtask;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待发布的场景卡片任务DTO
 *
 * @author Shadow
 * @datetime 2024-03-15 14:50
 */
@Data
public class SceneCardTaskPublishedDTO {

    /** 场景卡片任务ID */
    private Long sceneCardTaskId;

    /** 任务ID */
    private Long baseTaskId;

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
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 触发动作JSON */
    private String actions;
}
