package com.hycan.idn.tsp.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.CreateHolidayConfigReqVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.HolidayConfigRspVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.PageHolidayConfigReqVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.UpdateHolidayConfigReqVO;
import com.hycan.idn.tsp.message.entity.mysql.HolidayConfigEntity;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.facade.HolidayRemoteFacade;
import com.hycan.idn.tsp.message.repository.mysql.BroadcastTaskMapper;
import com.hycan.idn.tsp.message.repository.mysql.HolidayConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 节假日管理业务层
 *
 * @author Liuyingjie
 * @datetime 2022/9/30 16:32
 */
@Slf4j
@Component
public class HolidayConfigService {

    private static final String YEAR_TYPE_NEXT = "NEXT";

    @Resource
    private HolidayConfigMapper holidayConfigMapper;

    @Resource
    private BroadcastTaskMapper broadcastTaskMapper;

    @Resource
    private HolidayRemoteFacade holidayRemoteFacade;

    /**
     * 新增节假日配置
     *
     * @param reqVO 新增节假日配置请求VO
     * @return 节假日配置响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public HolidayConfigRspVO createHolidayConfig(CreateHolidayConfigReqVO reqVO) {
        if (holidayConfigMapper.isExists(reqVO.getHolidayName(), reqVO.getStartDate().getYear())) {
            throw new MsgBusinessException("该年份下存在重复的节假日配置, 请勿重复添加!");
        }

        HolidayConfigEntity entity = HolidayConfigEntity.of(reqVO);
        if (holidayConfigMapper.insert(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return HolidayConfigRspVO.of(entity);
    }

    /**
     * 根据ID修改节假日配置
     *
     * @param id    节假日配置ID
     * @param reqVO 修改节假日配置请求VO
     * @return 节假日配置响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public HolidayConfigRspVO updateHolidayConfig(Long id, UpdateHolidayConfigReqVO reqVO) {
        HolidayConfigEntity entity = holidayConfigMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("节假日配置不存在!");
        }

        if (broadcastTaskMapper.isHolidayConfigRelationSendingTask(id)) {
            throw new MsgBusinessException("该节假日配置所关联的萌宠播报任务, 当前为推送中状态, 不允许修改!");
        }

        if (!reqVO.getHolidayName().equals(entity.getHolidayName())) {
            if (holidayConfigMapper.isExists(reqVO.getHolidayName(), reqVO.getStartDate().getYear())) {
                throw new MsgBusinessException("该年份下存在重复的节假日配置, 请勿重复操作!");
            }
        }

        HolidayConfigEntity.of(entity, reqVO.getHolidayName(),
                reqVO.getHolidayType(), reqVO.getStartDate(), reqVO.getEndDate());
        if (holidayConfigMapper.updateById(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }
        return HolidayConfigRspVO.of(entity);
    }

    /**
     * 修改节假日状态
     *
     * @param id     节假日配置ID
     * @param status 节假日配置状态
     * @return 节假日配置响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public HolidayConfigRspVO updateHolidayConfigStatus(Long id, String status) {
        if (BizDataStatusConstants.FORBIDDEN.equals(status)) {
            if (broadcastTaskMapper.isHolidayConfigRelationSendingTask(id)) {
                throw new MsgBusinessException("该节假日所关联的萌宠播报任务, 当前为推送中状态, 请先结束推送任务后再禁用!");
            }
        }

        if (holidayConfigMapper.updateStatusById(id, status) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }
        return HolidayConfigRspVO.of(holidayConfigMapper.selectById(id));
    }

    /**
     * 删除节假日配置
     *
     * @param id 节假日配置ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteHolidayConfigById(Long id) {
        if (null == holidayConfigMapper.selectById(id)) {
            throw new MsgBusinessException("节假日配置不存在!");
        }

        if (broadcastTaskMapper.isHolidayConfigRelationTask(id)) {
            throw new MsgBusinessException("该节假日配置已关联萌宠播报任务, 请先删除萌宠播报任务后, 再删除节假日配置!");
        }
        return holidayConfigMapper.deleteById(id);
    }

    /**
     * 查询节假日配置
     *
     * @param id 节假日配置ID
     * @return 节假日配置响应VO
     */
    public HolidayConfigRspVO showHolidayConfig(Long id) {
        return HolidayConfigRspVO.of(holidayConfigMapper.selectById(id));
    }

    /**
     * 分页查询节假日配置
     *
     * @param reqVO 分页查询节假日配置请求VO
     * @return 分页返回节假日配置响应VO
     */
    public PageRspVO<HolidayConfigRspVO> listHolidayConfig(PageHolidayConfigReqVO reqVO) {
        IPage<HolidayConfigRspVO> page = holidayConfigMapper.findPage(Page.of(reqVO.getCurrent(), reqVO.getSize()), reqVO);
        return PageRspVO.of(page);
    }

    /**
     * 同步今年或明年国家法定节假日
     *
     * @param yearType 年份类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncLegalHolidayConfig(String yearType) {
        int year = LocalDate.now().getYear();
        if (YEAR_TYPE_NEXT.equals(yearType)) {
            year++;
        }
        List<HolidayConfigEntity> entities = holidayRemoteFacade.syncLegalHoliday(year);
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        holidayConfigMapper.insertOrUpdate(entities);
    }
}

