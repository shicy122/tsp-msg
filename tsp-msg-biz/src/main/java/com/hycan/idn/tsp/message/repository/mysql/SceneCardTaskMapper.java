package com.hycan.idn.tsp.message.repository.mysql;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.pojo.publishtask.SceneCardTaskPublishedDTO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.SceneResourceRspDTO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.SceneCardTaskRspVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.PageSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.entity.mysql.SceneCardTaskEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场景卡片消息任务Mapper接口
 *
 * @author liangwenqi
 * @datetime 2022-08-02 14:19
 */
public interface SceneCardTaskMapper extends BaseMapper<SceneCardTaskEntity> {

    /**
     * 根据场景卡片消息任务ID，查询基础消息任务ID
     *
     * @param id 场景卡片消息任务ID
     * @return 基础消息任务ID
     */
    Long selectBaskTaskIdById(Long id);

    /**
     * 查询场景卡片消息任务
     *
     * @param id 场景卡片消息任务主键
     * @return 场景卡片消息任务
     */
    SceneCardTaskRspVO selectTspMsgTaskSceneById(Long id);


    /**
     * 查询场景卡片消息任务列表
     *
     * @param queue 场景卡片消息任务
     * @return 场景卡片消息任务集合
     */
    List<SceneCardTaskRspVO> selectTspMsgTaskSceneList(@Param("query") PageSceneCardTaskReqVO queue);

    /**
     * 根据SceneId查询对应的资源配置列表
     *
     * @param id id
     * @return {@link List}<{@link SceneResourceRspDTO}>
     */
    List<SceneResourceRspDTO> selectTspMsgResourceBySceneId(Long id);

    /**
     * 根据场景id列表查询对应的资源配置列表
     *
     * @param ids id
     * @return {@link List}<{@link SceneResourceRspDTO}>
     */
    List<SceneResourceRspDTO> selectTspMsgResourceBySceneIds(List<Long> ids);

    /**
     * 根据资源配置ID查询关联推送中的任务
     *
     * @param resourceConfigId 资源配置ID
     * @return 是否关联推送中的任务
     */
    boolean isResourceConfigRelationSendingTask(@Param("resource_config_id") Long resourceConfigId);

    /**
     * 根据资源配置ID查询关联任务
     *
     * @param resourceConfigId 资源配置ID
     * @return 是否关联任务
     */
    boolean isResourceConfigRelationTask(@Param("resource_config_id") Long resourceConfigId);

    /**
     * 根据场景卡片任务ID，查询场景卡片任务响应信息
     *
     * @param id 场景卡片任务ID
     * @return 场景卡片任务响应VO
     */
    SceneCardTaskRspVO findTaskRspById(Long id);

    /**
     * 按条件分页查询场景卡片任务
     *
     * @param page  分页数据
     * @param query 分页查询场景卡片任务请求VO
     * @return 分页返回场景卡片任务报响应VO
     */
    IPage<SceneCardTaskRspVO> findPage(Page<SceneCardTaskRspVO> page, @Param("query") PageSceneCardTaskReqVO query);

    /**
     * 查询待发布的场景卡片任务
     *
     * @return 待发布的场景卡片任务列表
     */
    List<SceneCardTaskPublishedDTO> findPublishedTask();

    int insertTask(SceneCardTaskEntity entity);
}
