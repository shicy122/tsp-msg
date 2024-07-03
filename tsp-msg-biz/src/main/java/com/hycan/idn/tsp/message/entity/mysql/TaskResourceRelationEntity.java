package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 场景卡片与资源配置关系对象
 * 
 * @author liangwenqi
 * @datetime 2022-08-02 17:05
 */
@Data
@TableName("task_resource_relation")
public class TaskResourceRelationEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 基础任务ID */
    private Long baseTaskId;

    /** 资源配置ID */
    private Long resourceId;

    /** 是否为配图 */
    private Boolean isIllustration;

    public static TaskResourceRelationEntity of(Long baseTaskId, Long resourceId) {
        TaskResourceRelationEntity entity = new TaskResourceRelationEntity();
        entity.setBaseTaskId(baseTaskId);
        entity.setResourceId(resourceId);
        entity.setIsIllustration(false);
        return entity;
    }

    public static TaskResourceRelationEntity of(Long baseTaskId, Long resourceId, Boolean isIllustration) {
        TaskResourceRelationEntity entity = new TaskResourceRelationEntity();
        entity.setBaseTaskId(baseTaskId);
        entity.setResourceId(resourceId);
        entity.setIsIllustration(isIllustration);
        return entity;
    }
}
