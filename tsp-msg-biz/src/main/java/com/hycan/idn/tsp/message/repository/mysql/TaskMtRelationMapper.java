package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hycan.idn.tsp.message.pojo.basetask.VehicleBaseInfoVO;
import com.hycan.idn.tsp.message.entity.mysql.TaskMtRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务与车系车型关系Mapper接口
 *
 * @author liangwenqi
 * @datetime 2022-08-03 14:49
 */
public interface TaskMtRelationMapper extends BaseMapper<TaskMtRelationEntity> {

    /**
     * 批量保存任务与车系车型关系
     *
     * @param entities vin基础信息
     */
    void batchInsertOrUpdate(@Param("list") List<TaskMtRelationEntity> entities);

    /**
     * 根据基础任务ID清除相关数据
     *
     * @param baseTaskId 基础任务ID
     */
    void deleteByBaseTaskId(@Param("base_task_id") Long baseTaskId);

    /**
     * 根据基础任务ID查询MT列表
     *
     * @param baseTaskId 基础任务ID
     * @return MT列表
     */
    List<Long> findMtsByBaseTaskId(@Param("base_task_id") Long baseTaskId);
}
