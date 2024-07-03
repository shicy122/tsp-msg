package com.hycan.idn.tsp.message.entity.mysql;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hycan.idn.tsp.common.mybatis.base.BaseEntity;
import com.hycan.idn.tsp.message.pojo.scenecardtask.CreateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.UpdateSceneCardTaskReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 场景卡片消息任务对象
 * 
 * @author shichongying
 * @datetime 2024-02-23 15:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("scene_card_task_info")
public class SceneCardTaskEntity extends BaseEntity {

    private static final long serialVersionUID = -8485331871127581331L;

    private static final String EXEC_PLAN_RIGHT_NOW = "RIGHT_NOW";

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务ID */
    private Long baseTaskId;

    /** 执行计划(RIGHT_NOW:立即, AT_TIME:定时) */
    private String execPlan;

    /** 计划执行时间 */
    private LocalDateTime planTime;

    /** 保留时长(默认一天，单位:秒)  */
    private Long duration;

    /** 触发动作 */
    private String actions;

    public static SceneCardTaskEntity of(Long baseTaskId, CreateSceneCardTaskReqVO reqVO) {
        SceneCardTaskEntity entity = BeanUtil.toBean(reqVO, SceneCardTaskEntity.class);
        if (EXEC_PLAN_RIGHT_NOW.equals(entity.execPlan)) {
            entity.setPlanTime(LocalDateTime.now());
        }
        entity.setBaseTaskId(baseTaskId);
        entity.setActions(reqVO.getActions());
        return entity;
    }

    public static void of(SceneCardTaskEntity entity, UpdateSceneCardTaskReqVO reqVO) {
        entity.setExecPlan(reqVO.getExecPlan());
        entity.setDuration(reqVO.getDuration());
        entity.setActions(reqVO.getActions());
        if (EXEC_PLAN_RIGHT_NOW.equals(entity.execPlan)) {
            entity.setPlanTime(LocalDateTime.now());
        } else {
            entity.setPlanTime(reqVO.getPlanTime());
        }
    }
}
