package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送任务与VIN码关联关系对象
 *
 * @author shichongying
 * @datetime 2024-02-23 15:14
 */
@Data
@TableName("task_vin_relation")
public class TaskVinRelationEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务ID */
    private Long baseTaskId;

    /** 车辆VIN码 */
    private String vin;

    public static List<TaskVinRelationEntity> of(Long baseTaskId, List<String> vinList) {
        List<TaskVinRelationEntity> entities = new ArrayList<>();

        vinList.forEach(vin -> {
            TaskVinRelationEntity entity = new TaskVinRelationEntity();
            entity.setBaseTaskId(baseTaskId);
            entity.setVin(vin);
            entities.add(entity);
        });

        return entities;
    }
}
