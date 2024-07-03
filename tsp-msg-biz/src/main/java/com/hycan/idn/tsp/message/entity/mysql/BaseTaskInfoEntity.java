package com.hycan.idn.tsp.message.entity.mysql;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hycan.idn.tsp.common.mybatis.base.BaseEntity;
import com.hycan.idn.tsp.message.pojo.basetask.BaseTaskInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 基础消息任务对象（萌宠播报、场景卡片的基础）
 * 
 * @author shichongying
 * @datetime 2024-02-23 15:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("base_task_info")
public class BaseTaskInfoEntity extends BaseEntity {

    private static final long serialVersionUID = 1094049837219534104L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务名称 */
    private String taskName;

    /** 任务类型(SCENE_CARD/BROADCAST) */
    private String taskType;

    /** 消息标题 */
    private String msgTitle;

    /** 消息内容 */
    private String msgContent;

    /** 执行策略(AVNT_ONLINE:车辆上电, USER_LOGIN:账号登陆) */
    private String execPolicy;

    /** 任务状态(CREATED:已创建, PUBLISHED:已发布, SENDING:推送中, CANCELED:已撤回, FINISHED:已结束) */
    private String status;

    /** 发送范围(SINGLE:单个VIN, MULTIPLE:批量VIN, COMBINATION:组合条件) */
    private String scope;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 推送时间 */
    private LocalDateTime sendTime;

    /** 撤销时间 */
    private LocalDateTime cancelTime;

    /** 结束时间 */
    private LocalDateTime finishTime;

    public static BaseTaskInfoEntity of(BaseTaskInfoVO baseTaskInfoVO, String taskType) {
        BaseTaskInfoEntity entity =  BeanUtil.toBean(baseTaskInfoVO, BaseTaskInfoEntity.class);
        entity.setTaskType(taskType);
        return entity;
    }

    public static BaseTaskInfoEntity of(Long id, BaseTaskInfoVO baseTaskInfoVO,  String taskType) {
        BaseTaskInfoEntity entity = BeanUtil.toBean(baseTaskInfoVO, BaseTaskInfoEntity.class);
        entity.setId(id);
        entity.setTaskType(taskType);
        return entity;
    }
}
