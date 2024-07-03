package com.hycan.idn.tsp.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.constant.TaskTypeConstants;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.BaseTaskInfoVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.ResourceConfigDTO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.CreateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.PageSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.SceneCardTaskRspVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.UpdateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.taskrelation.QueryTaskResourceDTO;
import com.hycan.idn.tsp.message.pojo.taskrelation.SaveTaskResourceDTO;
import com.hycan.idn.tsp.message.entity.mysql.SceneCardTaskEntity;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.repository.mysql.ResourceConfigMapper;
import com.hycan.idn.tsp.message.repository.mysql.SceneCardTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 场景卡片任务处理逻辑
 *
 * @author liangliang
 */
@Slf4j
@Service
public class SceneCardTaskService {

    @Resource
    private BaseTaskService baseTaskService;

    @Resource
    private SceneCardTaskMapper sceneCardTaskMapper;

    @Resource
    private ResourceConfigMapper resourceConfigMapper;

    @Resource
    private TaskRelationService taskRelationService;

    /**
     * 新增场景卡片任务
     *
     * @param reqVO 新增场景卡片任务请求VO
     * @return 场景卡片任务响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public SceneCardTaskRspVO createSceneCardTask(CreateSceneCardTaskReqVO reqVO) {
        Long baseTaskId = baseTaskService.createBaseTask(BaseTaskInfoVO.of(reqVO), TaskTypeConstants.SCENE_CARD);

        taskRelationService.createTaskResource(SaveTaskResourceDTO.of(baseTaskId, reqVO));

        SceneCardTaskEntity entity = SceneCardTaskEntity.of(baseTaskId, reqVO);
        // TODO 前端修复后删除下面一行代码
        entity.setActions(null);
        if (sceneCardTaskMapper.insertTask(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return showSceneCardTask(entity.getId());
    }

    /**
     * 修改场景卡片任务
     *
     * @param id    场景卡片任务ID
     * @param reqVO 修改场景卡片任务请求VO
     * @return 场景卡片任务响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public SceneCardTaskRspVO updateSceneCardTask(Long id, UpdateSceneCardTaskReqVO reqVO) {
        SceneCardTaskEntity entity = sceneCardTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("场景卡片任务不存在!");
        }

        if (null != reqVO.getPictureId() && null == resourceConfigMapper.selectById(reqVO.getResourceId())) {
            throw new MsgBusinessException("消息配图关联的资源配置不存在!");
        }

        if (null != reqVO.getResourceId() && null == resourceConfigMapper.selectById(reqVO.getResourceId())) {
            throw new MsgBusinessException("资源内容关联的资源配置不存在!");
        }

        taskRelationService.updateTaskResource(SaveTaskResourceDTO.of(entity.getBaseTaskId(), reqVO));

        baseTaskService.updateBaseTask(entity.getBaseTaskId(), BaseTaskInfoVO.of(reqVO), TaskTypeConstants.SCENE_CARD);

        SceneCardTaskEntity.of(entity, reqVO);
        // TODO 前端修复后删除下面一行代码
        entity.setActions(null);
        if (sceneCardTaskMapper.updateById(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return showSceneCardTask(entity.getId());
    }

    /**
     * 修改场景卡片任务状态
     *
     * @param id     场景卡片任务ID
     * @param status 任务状态
     * @return 场景卡片任务响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public SceneCardTaskRspVO updateSceneCardTaskStatus(Long id, String status) {
        SceneCardTaskEntity entity = sceneCardTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("场景卡片任务不存在!");
        }

        baseTaskService.updateBaseTaskStatus(entity.getBaseTaskId(), status);

        return showSceneCardTask(entity.getId());
    }

    /**
     * 删除场景卡片任务
     *
     * @param id 场景卡片任务ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteSceneCardTask(Long id) {
        SceneCardTaskEntity entity = sceneCardTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("场景卡片任务不存在!");
        }

        baseTaskService.deleteBaseTask(entity.getBaseTaskId());

        return sceneCardTaskMapper.deleteById(id);
    }

    /**
     * 查询场景卡片任务
     *
     * @param id 场景卡片任务ID
     * @return 场景卡片任务响应VO
     */
    public SceneCardTaskRspVO showSceneCardTask(Long id) {
        SceneCardTaskEntity entity = sceneCardTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("场景卡片任务不存在!");
        }

        SceneCardTaskRspVO rspVO = sceneCardTaskMapper.findTaskRspById(id);

        Object scopeValue = baseTaskService.showBaseTaskRelation(entity.getBaseTaskId());
        rspVO.setScopeValue(scopeValue);

        QueryTaskResourceDTO dto = taskRelationService.getTaskResourceByBaseTaskId(entity.getBaseTaskId());

        return SceneCardTaskRspVO.of(rspVO, dto);
    }


    /**
     * 查询场景卡片任务列表
     *
     * @param reqVO 分页查询场景卡片任务请求VO
     * @return 分页返回场景卡片任务响应VO
     */
    public PageRspVO<SceneCardTaskRspVO> listSceneCardTask(PageSceneCardTaskReqVO reqVO) {
        IPage<SceneCardTaskRspVO> page = sceneCardTaskMapper.findPage(Page.of(reqVO.getCurrent(), reqVO.getSize()), reqVO);
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new PageRspVO<>();
        }

        List<Long> baseTaskIds = page.getRecords().stream()
                .map(SceneCardTaskRspVO::getBaseTaskId)
                .collect(Collectors.toList());
        Map<Long, List<ResourceConfigDTO>> resourceConfigMap  = resourceConfigMapper.findResourcesByBaseTaskIds(baseTaskIds)
                .stream().collect(Collectors.groupingBy(ResourceConfigDTO::getBaseTaskId));;

        for (SceneCardTaskRspVO rspVO : page.getRecords()) {
            List<ResourceConfigDTO> resourceConfigList = resourceConfigMap.get(rspVO.getBaseTaskId());
            if (CollectionUtils.isEmpty(resourceConfigList)) {
                continue;
            }
            SceneCardTaskRspVO.of(rspVO, QueryTaskResourceDTO.of(resourceConfigList));
        }

        return PageRspVO.of(page);
    }
}
