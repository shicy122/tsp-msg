package com.hycan.idn.tsp.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.constant.TaskTypeConstants;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.BaseTaskInfoVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.BroadcastTaskRspVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.CreateBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.PageBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.UpdateBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.entity.mysql.BroadcastTaskEntity;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.repository.mysql.BroadcastTaskMapper;
import com.hycan.idn.tsp.message.repository.mysql.HolidayConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 萌宠播报业务层
 *
 * @author Liuyingjie
 * @datetime 2022/9/30 17:09
 */
@Slf4j
@Service
public class BroadcastTasService {

    @Resource
    private BaseTaskService baseTaskService;

    @Resource
    private BroadcastTaskMapper broadcastTaskMapper;

    @Resource
    private HolidayConfigMapper holidayConfigMapper;

    /**
     * 新增萌宠播报任务
     *
     * @param reqVO 新增萌宠播报任务请求VO
     * @return 萌宠播报任务响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public BroadcastTaskRspVO createBroadcastTask(CreateBroadcastTaskReqVO reqVO) {
        // 新增基础任务
        Long baseTaskId = baseTaskService.createBaseTask(BaseTaskInfoVO.of(reqVO), TaskTypeConstants.BROADCAST);

        // 新增萌宠播报任务
        BroadcastTaskEntity entity = BroadcastTaskEntity.of(baseTaskId, reqVO.getExecRate(), reqVO.getHolidayId());
        if (broadcastTaskMapper.insert(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return showBroadcastTask(entity.getId());
    }

    /**
     * 修改萌宠播报任务任务
     *
     * @param id    萌宠播报任务ID
     * @param reqVO 修改萌宠播报任务请求VO
     * @return 萌宠播报任务响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public BroadcastTaskRspVO updateBroadcastTask(Long id, UpdateBroadcastTaskReqVO reqVO) {
        BroadcastTaskEntity entity = broadcastTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("萌宠播报任务不存在!");
        }

        if (null == holidayConfigMapper.selectById(reqVO.getHolidayId())) {
            throw new MsgBusinessException("萌宠播报任务关联的节假日配置不存在!");
        }

        baseTaskService.updateBaseTask(entity.getBaseTaskId(), BaseTaskInfoVO.of(reqVO), TaskTypeConstants.BROADCAST);

        BroadcastTaskEntity.of(entity, reqVO.getExecRate(), reqVO.getHolidayId());
        if (broadcastTaskMapper.updateById(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return showBroadcastTask(entity.getId());
    }

    /**
     * 修改萌宠播报任务状态
     *
     * @param id 萌宠播报任务ID
     * @param status 任务状态
     * @return 萌宠播报任务响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public BroadcastTaskRspVO updateBroadcastTaskStatus(Long id, String status) {
        BroadcastTaskEntity entity = broadcastTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("萌宠播报任务不存在!");
        }

        baseTaskService.updateBaseTaskStatus(entity.getBaseTaskId(), status);

        return showBroadcastTask(entity.getId());
    }

    /**
     * 删除萌宠播报任务
     *
     * @param id 萌宠播报任务ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteBroadcastTask(Long id) {
        BroadcastTaskEntity entity = broadcastTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("萌宠播报任务不存在!");
        }

        baseTaskService.deleteBaseTask(entity.getBaseTaskId());

        return broadcastTaskMapper.deleteById(id);
    }

    /**
     * 查询萌宠播报任务 
     *
     * @param id 萌宠播报任务ID
     * @return 萌宠播报任务响应VO
     */
    public BroadcastTaskRspVO showBroadcastTask(Long id) {
        BroadcastTaskEntity entity = broadcastTaskMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("萌宠播报任务不存在!");
        }

        BroadcastTaskRspVO rspVO = broadcastTaskMapper.findTaskRspById(entity.getId());

        Object scopeValue = baseTaskService.showBaseTaskRelation(entity.getBaseTaskId());
        rspVO.setScopeValue(scopeValue);

        return rspVO;
    }

    /**
     * 查询萌宠播报任务列表
     *
     * @param reqVO 分页查询萌宠播报任务请求VO
     * @return 分页返回萌宠播报任务响应VO
     */
    public PageRspVO<BroadcastTaskRspVO> listBroadcastTask(PageBroadcastTaskReqVO reqVO) {
        IPage<BroadcastTaskRspVO> page = broadcastTaskMapper.findPage(Page.of(reqVO.getCurrent(), reqVO.getSize()), reqVO);
        return PageRspVO.of(page);
    }
}
