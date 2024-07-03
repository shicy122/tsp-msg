package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hycan.idn.tsp.message.entity.mysql.TaskResourceRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务与资源关系Mapper接口
 *
 * @author liangwenqi
 * @datetime 2022-08-03 14:49
 */
public interface TaskResourceRelationMapper extends BaseMapper<TaskResourceRelationEntity> {

    /**
     * 根据基础任务ID清除相关数据
     *
     * @param baseTaskId 基础任务ID
     */
    void deleteByBaseTaskId(@Param("base_task_id") Long baseTaskId);
}
