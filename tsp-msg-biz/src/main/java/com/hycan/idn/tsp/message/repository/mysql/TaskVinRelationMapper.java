package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hycan.idn.tsp.message.pojo.basetask.ExportVinRspVO;
import com.hycan.idn.tsp.message.entity.mysql.TaskVinRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务与VIN码关系Mapper接口
 *
 * @author liangwenqi
 * @date 2022-08-03
 */
public interface TaskVinRelationMapper extends BaseMapper<TaskVinRelationEntity> {

    /**
     * 根据基础消息任务ID查询关联的VIN列表
     *
     * @param baseTaskId 基本任务ID
     * @return VIN列表
     */
    List<String> findVinsByBaseTaskId(@Param("base_task_id")Long baseTaskId);

    /**
     * 根据基础消息任务ID查询关联的VIN列表
     *
     * @param baseTaskId 基本任务ID
     * @return VIN列表
     */
    List<ExportVinRspVO> exportVinsByBaseTaskId(@Param("base_task_id")Long baseTaskId);

    /**
     * 批量保存基础任务ID与VIN的关系
     */
    void batchInsertOrUpdate(List<TaskVinRelationEntity> entities);

    /**
     * 根据基础任务ID清除相关数据
     *
     * @param baseTaskId 基础任务ID
     */
    void deleteByBaseTaskId(@Param("base_task_id") Long baseTaskId);
}
