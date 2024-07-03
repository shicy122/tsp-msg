package com.hycan.idn.tsp.message.repository.mysql;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hycan.idn.tsp.message.entity.mysql.BaseTaskInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基础消息任务Mapper接口
 *
 * @author liangwenqi
 * @date 2022-08-02
 */
public interface BaseTaskInfoMapper extends BaseMapper<BaseTaskInfoEntity> {

    /**
     * 根据id查询任务状态
     *
     * @param id
     * @return
     */
    String findStatusById(Long id);

    /**
     * 根据结束时间修改状态为已结束
     */
    void updateStatusFinishedByFinishTime();

    /**
     * 修改任务为发送中状态
     *
     * @param id 任务ID
     */
    void updateTaskStatusSending(Long id);

    /**
     * 查询近两天已结束的任务ID
     *
     * @param yesterday 昨天
     * @return 基础任务ID
     */
    List<Long> findStatusFinishedByFinishTime(@Param("yesterday") LocalDateTime yesterday);

    /**
     * 根据任务名称，查询任务是否存在
     *
     * @param taskName 节假日名称
     * @return 是否存在重复节假日配置
     */
    boolean isExists(@Param("task_name") String taskName);

}
