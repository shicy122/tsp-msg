package com.hycan.idn.tsp.message.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.hycan.idn.tsp.common.core.util.ExcelUtil;
import com.hycan.idn.tsp.message.constant.ResourceFileConstants;
import com.hycan.idn.tsp.message.constant.TaskScopeConstants;
import com.hycan.idn.tsp.message.constant.TaskStatusConstants;
import com.hycan.idn.tsp.message.entity.mysql.BaseTaskInfoEntity;
import com.hycan.idn.tsp.message.entity.mysql.TaskMtRelationEntity;
import com.hycan.idn.tsp.message.entity.mysql.TaskVinRelationEntity;
import com.hycan.idn.tsp.message.entity.mysql.VehicleBaseInfoEntity;
import com.hycan.idn.tsp.message.event.listener.ImportVinExcelListener;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.facade.VmsFeignFacade;
import com.hycan.idn.tsp.message.pojo.basetask.BaseTaskInfoVO;
import com.hycan.idn.tsp.message.pojo.basetask.ImportVinReqVO;
import com.hycan.idn.tsp.message.repository.mysql.BaseTaskInfoMapper;
import com.hycan.idn.tsp.message.repository.mysql.TaskMtRelationMapper;
import com.hycan.idn.tsp.message.repository.mysql.TaskVinRelationMapper;
import com.hycan.idn.tsp.message.repository.mysql.VehicleBaseInfoMapper;
import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehCarDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础任务表业务处理
 *
 * @author Liuyingjie
 * @datetime 2022/9/30 17:14
 */
@Slf4j
@Service
public class BaseTaskService {

    private static final int PAGE_SIZE = 1000;

    @Resource
    private VmsFeignFacade vmsFeignFacade;

    @Resource
    private BaseTaskInfoMapper baseTaskInfoMapper;

    @Resource
    private VehicleBaseInfoMapper vehicleBaseInfoMapper;

    @Resource
    private TaskVinRelationMapper taskVinRelationMapper;

    @Resource
    private TaskMtRelationMapper taskMtRelationMapper;

    @Resource
    private TaskDetailRecordService taskDetailRecordService;

    /**
     * 新增基础消息任务，及任务关联的VIN列表、MT列表，VIN基础信息
     *
     * @param baseTaskInfoVO 基础消息任务
     * @return 基础消息任务ID
     */
    public Long createBaseTask(BaseTaskInfoVO baseTaskInfoVO, String taskType) {
        if (baseTaskInfoMapper.isExists(baseTaskInfoVO.getTaskName())) {
            throw new MsgBusinessException("任务名称重复, 请勿重复添加!");
        }

        BaseTaskInfoEntity entity = BaseTaskInfoEntity.of(baseTaskInfoVO, taskType);
        if (baseTaskInfoMapper.insert(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        // 处理与任务有关联的数据，如VIN的基础信息，与任务关联的MT、VIN列表
        handleTaskRelation(baseTaskInfoVO, entity.getId());

        return entity.getId();
    }

    /**
     * 修改基础消息任务，及任务关联的VIN列表、MT列表，VIN基础信息
     *
     * @param baseTaskId     基础消息任务ID
     * @param baseTaskInfoVO 基础消息任务
     */
    public void updateBaseTask(Long baseTaskId, BaseTaskInfoVO baseTaskInfoVO, String taskType) {
        BaseTaskInfoEntity entity = baseTaskInfoMapper.selectById(baseTaskId);
        // 判断状态是已创建和已发送时不允许做任何修改
        String taskStatus = entity.getStatus();
        if (TaskStatusConstants.FINISHED.equals(taskStatus)
                || TaskStatusConstants.SENDING.equals(taskStatus)
                || TaskStatusConstants.PUBLISHED.equals(taskStatus)) {
            throw new MsgBusinessException("不允许修改状态为[已发布][推送中][已结束]的任务, 请先撤回或终止任务!");
        }

        if (!baseTaskInfoVO.getTaskName().equals(entity.getTaskName())
                && baseTaskInfoMapper.isExists(baseTaskInfoVO.getTaskName())) {
            throw new MsgBusinessException("任务名称重复, 不允许修改!");
        }

        if (baseTaskInfoMapper.updateById(BaseTaskInfoEntity.of(baseTaskId, baseTaskInfoVO, taskType)) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        clearTaskRelation(baseTaskId);
        handleTaskRelation(baseTaskInfoVO, baseTaskId);
    }

    /**
     * 修改基础消息任务状态
     *
     * @param baseTaskId 基础消息任务ID
     * @param status     基础消息任务状态
     */
    public void updateBaseTaskStatus(Long baseTaskId, String status) {
        BaseTaskInfoEntity entity = baseTaskInfoMapper.selectById(baseTaskId);

        switch (status) {
            case TaskStatusConstants.PUBLISHED:
                updateTaskStatusPublished(entity);
                break;
            case TaskStatusConstants.CANCELED:
                updateTaskStatusCanceled(entity);
                break;
            case TaskStatusConstants.FINISHED:
                updateTaskStatusFinished(entity);
                taskDetailRecordService.updateDetailRecordForbidden(Collections.singletonList(baseTaskId));
                break;
            default:
                break;
        }
    }

    /**
     * 删除基础消息任务状态
     *
     * @param baseTaskId 基础消息任务ID
     */
    public void deleteBaseTask(Long baseTaskId) {
        BaseTaskInfoEntity entity = baseTaskInfoMapper.selectById(baseTaskId);
        if (TaskStatusConstants.SENDING.equals(entity.getStatus())) {
            throw new MsgBusinessException("当前任务状态为[推送中]，请终止任务后再删除!");
        }

        clearTaskRelation(baseTaskId);

        baseTaskInfoMapper.deleteById(baseTaskId);
        taskDetailRecordService.updateDetailRecordForbidden(Collections.singletonList(baseTaskId));
    }

    /**
     * 查询任务关联的VIN或MT数据
     *
     * @param baseTaskId 基础任务ID
     */
    public Object showBaseTaskRelation(Long baseTaskId) {
        BaseTaskInfoEntity entity = baseTaskInfoMapper.selectById(baseTaskId);

        if (TaskScopeConstants.SINGLE.equals(entity.getScope())) {
            List<String> relationVins = taskVinRelationMapper.findVinsByBaseTaskId(baseTaskId);
            if (CollectionUtils.isEmpty(relationVins)) {
                return null;
            }
            return relationVins.get(0);
        } else if (TaskScopeConstants.COMBINATION.equals(entity.getScope())) {
            List<Long> mtIds = taskMtRelationMapper.findMtsByBaseTaskId(baseTaskId);
            return vmsFeignFacade.getVehBaselInfo(mtIds);
        }
        return null;
    }

    /**
     * 清除任务关联的数据
     *
     * @param baseTaskId 基础任务ID
     */
    private void clearTaskRelation(Long baseTaskId) {
        taskVinRelationMapper.deleteByBaseTaskId(baseTaskId);
        taskMtRelationMapper.deleteByBaseTaskId(baseTaskId);
    }

    /**
     * 根据发送范围类型，处理关联数据
     *
     * @param baseTaskVO 基础任务对象VO
     * @param baseTaskId 基础任务ID
     */
    private void handleTaskRelation(BaseTaskInfoVO baseTaskVO, Long baseTaskId) {
        if (TaskScopeConstants.SINGLE.equals(baseTaskVO.getScope())) {
            if (StringUtils.isNotBlank(baseTaskVO.getVin())) {
                batchSaveTaskVinRelation(Collections.singletonList(baseTaskVO.getVin()), baseTaskId);
            }
        } else if (TaskScopeConstants.MULTIPLE.equals(baseTaskVO.getScope())) {
            batchSaveTaskVinRelation(readExcelFile(baseTaskVO.getFile()), baseTaskId);
        } else if (TaskScopeConstants.COMBINATION.equals(baseTaskVO.getScope())) {
            batchSaveTaskMtRelation(baseTaskVO.getMtIds(), baseTaskId);
        }
    }

    /**
     * 解析vin文件
     *
     * @param file 文件
     */
    private List<String> readExcelFile(MultipartFile file) {
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            throw new MsgBusinessException("请选择需要上传的文件!");
        }
        if (!file.getOriginalFilename().endsWith(ResourceFileConstants.SUFFIX_XLSX)) {
            throw new MsgBusinessException("不支持的文件类型, 请上传.xlsx后缀的文件!");
        }

        try (InputStream inputStream = file.getInputStream()){
            ExcelReader excelReader = EasyExcel.read(inputStream).build();
            ImportVinExcelListener importVinExcelListener = new ImportVinExcelListener();
            ReadSheet readSheet = EasyExcel
                    .readSheet(0)
                    .head(ImportVinReqVO.class)
                    .registerReadListener(importVinExcelListener)
                    .build();
            excelReader.read(readSheet);

            List<ImportVinReqVO> importVinReqVOList = importVinExcelListener.getDataList();

            if (ObjectUtils.isEmpty(importVinReqVOList)) {
                throw new MsgBusinessException("空的vin文件");
            }

            return importVinReqVOList.stream().map(ImportVinReqVO::getVin).distinct().collect(Collectors.toList());
        } catch (IOException e) {
            throw new MsgBusinessException("读取EXCEL文件失败!");
        }
    }

    /**
     * 批量保存车辆基础信息、车辆与任务ID的关系
     *
     * @param vinList    VIN列表
     * @param baseTaskId 基础任务ID
     */
    private void batchSaveTaskVinRelation(List<String> vinList, Long baseTaskId) {
        int index = 0;
        while (index < vinList.size()) {
            List<String> batch = vinList.subList(index, Math.min(index + PAGE_SIZE, vinList.size()));

            List<VehCarDTO> vehCarDTOList = vmsFeignFacade.getVehInfo(batch);
            if (CollectionUtils.isNotEmpty(vehCarDTOList)) {
                vehicleBaseInfoMapper.batchInsertOrUpdate(VehicleBaseInfoEntity.of(vehCarDTOList));
            }

            if (CollectionUtils.isNotEmpty(batch)) {
                taskVinRelationMapper.batchInsertOrUpdate(TaskVinRelationEntity.of(baseTaskId, batch));
            }

            index += PAGE_SIZE;
        }
    }

    /**
     * 批量保存任务与MT的关系
     *
     * @param mtIds      MT ID列表
     * @param baseTaskId 基础任务ID
     */
    private void batchSaveTaskMtRelation(List<Long> mtIds, Long baseTaskId) {
        if (CollectionUtils.isEmpty(mtIds)) {
            throw new MsgBusinessException("播报范围为组合条件时, 车型列表不能为空!");
        }

        List<TaskMtRelationEntity> entities = new ArrayList<>();
        for (Long mtId : mtIds) {
            entities.add(TaskMtRelationEntity.of(baseTaskId, mtId));
        }

        if (CollectionUtils.isNotEmpty(entities)) {
            taskMtRelationMapper.batchInsertOrUpdate(entities);
        }
    }

    /**
     * 将基础消息任务状态修改为已发布
     *
     * @param entity 基础消息任务对象
     */
    private void updateTaskStatusPublished(BaseTaskInfoEntity entity) {
        String status = entity.getStatus();
        if (!(TaskStatusConstants.CREATED.equals(status) || TaskStatusConstants.CANCELED.equals(status))) {
            throw new MsgBusinessException("当前状态非[已创建]或[已撤回], 不允许修改为[已发布]状态!");
        }

        entity.setPublishTime(LocalDateTime.now());
        entity.setStatus(TaskStatusConstants.PUBLISHED);

        baseTaskInfoMapper.updateById(entity);
    }

    /**
     * 将基础消息任务状态修改为已取消
     *
     * @param entity 基础消息任务对象
     */
    private void updateTaskStatusCanceled(BaseTaskInfoEntity entity) {
        String status = entity.getStatus();
        if (!TaskStatusConstants.PUBLISHED.equals(status)) {
            throw new MsgBusinessException("当前状态非[已发布], 不允许修改为[已撤回]状态!");
        }

        entity.setCancelTime(LocalDateTime.now());
        entity.setStatus(TaskStatusConstants.CANCELED);

        baseTaskInfoMapper.updateById(entity);
    }

    /**
     * 将基础消息任务状态修改为已终止
     *
     * @param entity 基础消息任务对象
     */
    private void updateTaskStatusFinished(BaseTaskInfoEntity entity) {
        String status = entity.getStatus();
        if (!TaskStatusConstants.SENDING.equals(status)) {
            throw new MsgBusinessException("当前状态非[推送中], 不允许修改为[已终止]状态!");
        }

        entity.setCancelTime(LocalDateTime.now());
        entity.setStatus(TaskStatusConstants.FINISHED);

        baseTaskInfoMapper.updateById(entity);
    }
}
