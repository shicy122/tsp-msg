package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.pojo.broadcasttask.PageBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.BroadcastTaskRspVO;
import com.hycan.idn.tsp.message.pojo.publishtask.BroadcastTaskPublishedDTO;
import com.hycan.idn.tsp.message.entity.mysql.BroadcastTaskEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 萌宠播报任务表接口
 *
 * @author Liuyingjie
 * @datetime 2022/8/2 17:29
 */
public interface BroadcastTaskMapper extends BaseMapper<BroadcastTaskEntity> {

    /**
     * 按条件分页查询萌宠播报任务
     *
     * @param page  分页数据
     * @param reqVO 分页查询萌宠播报任务报请求VO
     * @return 分页返回萌宠播报任务报响应VO
     */
    IPage<BroadcastTaskRspVO> findPage(Page<BroadcastTaskRspVO> page, @Param("query") PageBroadcastTaskReqVO reqVO);

    /**
     * 根据萌宠播报任务ID，查询基础任务ID
     *
     * @param id 宠播报报任务ID
     * @return 基础任务ID
     */
    Long selectBaskTaskIdById(Long id);

    /**
     * 根据节假日配置ID查询关联推送中的任务
     *
     * @param holidayConfigId 节假日配置ID
     * @return 是否关联推送中的任务
     */
    boolean isHolidayConfigRelationSendingTask(@Param("holiday_config_id") Long holidayConfigId);

    /**
     * 根据节假日配置ID查询关联任务
     *
     * @param holidayConfigId 节假日配置ID
     * @return 是否关联任务
     */
    boolean isHolidayConfigRelationTask(@Param("holiday_config_id") Long holidayConfigId);

    /**
     * 根据萌宠播报任务ID，查询萌宠播报任务响应信息
     *
     * @param id 萌宠播报报任务ID
     * @return 萌宠播报任务响应VO
     */
    BroadcastTaskRspVO findTaskRspById(Long id);

    /**
     * 查询待发布的萌宠播报任务
     *
     * @return 待发布的萌宠播报任务列表
     */
    List<BroadcastTaskPublishedDTO> findPublishedTask();

    /**
     * 查询发送中的萌宠播报-生日问候任务
     *
     * @return 发送中的萌宠播报-生日问候任务
     */
    List<Long> findSendingBirthdayTask();
}
