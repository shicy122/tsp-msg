package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 推送任务与车型关联关系对象
 *
 * @author liangwenqi
 * @datetime 2022-08-02 17:24
 */
@Data
@TableName("task_mt_relation")
public class TaskMtRelationEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务ID */
    private Long baseTaskId;

    /** 车型ID */
    private Long mtId;

    public static TaskMtRelationEntity of(Long baseTaskId, Long mtId) {
        TaskMtRelationEntity entity = new TaskMtRelationEntity();
        entity.setBaseTaskId(baseTaskId);
        entity.setMtId(mtId);
        return entity;
    }
}
