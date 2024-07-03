package com.hycan.idn.tsp.message.pojo.holidayconfig;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 第三方节假日
 *
 * @author Liuyingjie
 * @datetime 2022/8/29 16:43
 */
@Data
public class SyncLegalHolidayRspVO {
    /**
     * 日期 yyyy-MM-dd
     */
    private Integer date;

    /**
     * 节假日名称
     */
    @JsonProperty("holiday_cn")
    private String holidayCn;
}
