package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hycan.idn.tsp.common.mybatis.base.BaseEntity;
import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.pojo.holidayconfig.CreateHolidayConfigReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 节假日配置对象
 *
 * @author shichongying
 * @datetime 2024-02-23 15:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("holiday_config")
public class HolidayConfigEntity extends BaseEntity {

    private static final long serialVersionUID = 108334691714761215L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 节假日名称 */
    private String holidayName;

    /** 节假日类型 (BIRTHDAY:生日, LEGAL:法定, CUSTOM:自定义) */
    private String holidayType;

    /** 年份 */
    private Integer year;

    /** 状态 (ENABLE:启用, DISABLE:无效, FORBIDDEN:禁用) */
    private String status;

    /** 开始时间 */
    private LocalDate startDate;

    /** 结束时间 */
    private LocalDate endDate;

    public static HolidayConfigEntity of(CreateHolidayConfigReqVO reqVO) {
        HolidayConfigEntity entity = new HolidayConfigEntity();
        entity.setHolidayName(reqVO.getHolidayName());
        entity.setHolidayType(reqVO.getHolidayType());
        entity.setYear(reqVO.getEndDate().getYear());
        entity.setStatus(BizDataStatusConstants.ENABLE);
        entity.setStartDate(reqVO.getStartDate());
        entity.setEndDate(reqVO.getEndDate());

        return entity;
    }

    public static void of(HolidayConfigEntity entity, String holidayName,
                          String holidayType, LocalDate startDate, LocalDate endDate) {
        entity.setHolidayName(holidayName);
        entity.setHolidayType(holidayType);
        entity.setYear(startDate.getYear());
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
    }

    public static HolidayConfigEntity of(String holidayName,
                          String holidayType, LocalDate startDate, LocalDate endDate) {
        HolidayConfigEntity entity = new HolidayConfigEntity();
        entity.setHolidayName(holidayName);
        entity.setHolidayType(holidayType);
        entity.setYear(startDate.getYear());
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setStatus(BizDataStatusConstants.ENABLE);
        entity.setCreateBy(CommonConstants.SYS);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy(CommonConstants.SYS);
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }
}
