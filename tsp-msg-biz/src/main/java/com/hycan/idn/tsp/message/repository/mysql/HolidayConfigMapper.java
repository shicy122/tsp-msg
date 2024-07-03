package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.pojo.holidayconfig.HolidayConfigRspVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.PageHolidayConfigReqVO;
import com.hycan.idn.tsp.message.entity.mysql.HolidayConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节假日配置表接口
 *
 * @author shichongying
 * @datetime 2022/8/2 17:23
 */
public interface HolidayConfigMapper extends BaseMapper<HolidayConfigEntity> {


    /***
     * 将到期节假日状态修改为失效
     *
     * @param endDate 截止日期
     */
    boolean updateStatusDisableByEndDate();

    /**
     * 根据节假日配置ID获取节假日名称
     *
     * @param id 节假日配置ID
     * @return 假日名称
     */
    String findHolidayNameById(@Param("id") Long id);

    /**
     * 根据年份和节假日名称查询节假日配置是否存在
     *
     * @param holidayName 节假日名称
     * @param year        节假日年份
     * @return 是否存在重复节假日配置
     */
    boolean isExists(@Param("holiday_name") String holidayName, @Param("year") int year);

    /**
     * 根据节假日配置ID修改节假日配置状态
     *
     * @param id     节假日配置ID
     * @param status 节假日配置状态
     */
    int updateStatusById(@Param("id") Long id, @Param("status") String status);

    /**
     * 按条件分页查询节假日配置
     *
     * @param page  分页数据
     * @param reqVO 分页查询节假日配置请求VO
     * @return 分页返回节假日配置响应VO
     */
    IPage<HolidayConfigRspVO> findPage(Page<HolidayConfigRspVO> page, @Param("query") PageHolidayConfigReqVO reqVO);

    /**
     * 批量插入或更新节假日配置对象列表
     *
     * @param entities 节假日配置对象列表
     */
    void insertOrUpdate(@Param("list") List<HolidayConfigEntity> entities);
}
