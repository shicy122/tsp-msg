package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hycan.idn.tsp.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 萌宠播报消息任务对象
 *
 * @author shichongying
 * @datetime 2024-02-23 15:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("broadcast_task_info")
public class BroadcastTaskEntity extends BaseEntity {

    private static final long serialVersionUID = -7924682426534925062L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务ID */
    private Long baseTaskId;

    /** 执行频率 (ONE_TIME:推送一次, EVERY_DAY:每天一次) */
    private String execRate;

    /** 节假日配置ID */
    private Long holidayConfigId;

    public static BroadcastTaskEntity of(Long baseTaskId, String execRate, Long holidayConfigId) {
        BroadcastTaskEntity entity = new BroadcastTaskEntity();
        entity.setBaseTaskId(baseTaskId);
        entity.setExecRate(execRate);
        entity.setHolidayConfigId(holidayConfigId);
        return entity;
    }

    public static void of(BroadcastTaskEntity entity, String execRate, Long holidayConfigId) {
        entity.setExecRate(execRate);
        entity.setHolidayConfigId(holidayConfigId);
    }
}
