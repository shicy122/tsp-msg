package com.hycan.idn.tsp.message.service;

import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.constant.ResourceTypeConstants;
import com.hycan.idn.tsp.message.constant.TaskScopeConstants;
import com.hycan.idn.tsp.message.pojo.basetask.ExportVinRspVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.ResourceConfigDTO;
import com.hycan.idn.tsp.message.pojo.taskrelation.QueryTaskResourceDTO;
import com.hycan.idn.tsp.message.pojo.taskrelation.SaveTaskResourceDTO;
import com.hycan.idn.tsp.message.entity.mysql.ResourceConfigEntity;
import com.hycan.idn.tsp.message.entity.mysql.TaskResourceRelationEntity;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.facade.VmsFeignFacade;
import com.hycan.idn.tsp.message.repository.mysql.ResourceConfigMapper;
import com.hycan.idn.tsp.message.repository.mysql.TaskMtRelationMapper;
import com.hycan.idn.tsp.message.repository.mysql.TaskResourceRelationMapper;
import com.hycan.idn.tsp.message.repository.mysql.TaskVinRelationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 任务与资源关系Service
 *
 * @author Shadow
 * @datetime 2024-03-13 11:43
 */
@Service
public class TaskRelationService {

    @Resource
    private ResourceConfigMapper resourceConfigMapper;

    @Resource
    private TaskResourceRelationMapper taskResourceRelationMapper;

    @Resource
    private TaskVinRelationMapper taskVinRelationMapper;

    @Resource
    private TaskMtRelationMapper taskMtRelationMapper;

    @Resource
    private VmsFeignFacade vmsFeignFacade;

    public List<String> getVinsByBaseTaskIdAndScope(Long baseTaskId, String scope) {
        if (TaskScopeConstants.SINGLE.equals(scope) || TaskScopeConstants.MULTIPLE.equals(scope)) {
            return taskVinRelationMapper.findVinsByBaseTaskId(baseTaskId);
        } else if (TaskScopeConstants.COMBINATION.equals(scope)) {
            List<Long> mts = taskMtRelationMapper.findMtsByBaseTaskId(baseTaskId);
            return vmsFeignFacade.getVinsByMts(mts);
        }
        return Collections.emptyList();
    }

    public QueryTaskResourceDTO getTaskResourceByBaseTaskId(Long baseTaskId) {
        List<ResourceConfigDTO> resourceConfigList = resourceConfigMapper.findResourcesByBaseTaskId(baseTaskId);
        if (CollectionUtils.isEmpty(resourceConfigList) || resourceConfigList.size() > 2) {
            return new QueryTaskResourceDTO();
        }

        return QueryTaskResourceDTO.of(resourceConfigList);
    }

    /**
     * 保存任务与资源的关系
     *
     * @param dto TaskResourceDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void createTaskResource(SaveTaskResourceDTO dto) {
        if (StringUtils.isNotBlank(dto.getCustomUrl())) {
            ResourceConfigEntity entity = ResourceConfigEntity.of(dto.getCustomUrl(), dto.getResourceType());
            if (resourceConfigMapper.insert(entity) > 0) {
                taskResourceRelationMapper.insert(TaskResourceRelationEntity.of(dto.getBaseTaskId(), entity.getId()));
            }
        } else {
            if (validResource(dto.getResourceId(), false)) {
                taskResourceRelationMapper.insert(TaskResourceRelationEntity.of(dto.getBaseTaskId(), dto.getResourceId()));
            }
        }

        if (validResource(dto.getPictureId(), true)) {
            taskResourceRelationMapper.insert(TaskResourceRelationEntity.of(dto.getBaseTaskId(), dto.getPictureId(), Boolean.TRUE));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTaskResource(SaveTaskResourceDTO dto) {
        taskResourceRelationMapper.deleteByBaseTaskId(dto.getBaseTaskId());

        createTaskResource(dto);
    }

    public List<ExportVinRspVO> exportVins(Long baseTaskId) {
        return taskVinRelationMapper.exportVinsByBaseTaskId(baseTaskId);
    }

    private boolean validResource(Long resourceId, boolean isIllustration) {
        if (null == resourceId) {
            return false;
        }

        ResourceConfigEntity entity = resourceConfigMapper.selectById(resourceId);

        if (ObjectUtils.isEmpty(entity)) {
            throw new MsgBusinessException("资源不存在!");
        }

        if (BizDataStatusConstants.FORBIDDEN.equals(entity.getStatus())) {
            throw new MsgBusinessException("资源已被禁用，请重新选择");
        }

        if (isIllustration && !ResourceTypeConstants.PICTURE.equals(entity.getResourceType())) {
            throw new MsgBusinessException("配图的资源类型必须为图片!");
        }
        return true;
    }
}
