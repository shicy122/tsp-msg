package com.hycan.idn.tsp.message.pojo.holidayconfig;

import cn.hutool.core.bean.BeanUtil;
import com.hycan.idn.tsp.message.entity.mysql.HolidayConfigEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 节假日配置响应VO
 *
 * @author Liuyingjie
 * @datetime 2022/8/31 17:39
 */
@Data
@ApiModel(description = "节假日配置响应VO")
public class HolidayConfigRspVO {

    @ApiModelProperty(value = "节假日id", required = true, position = 1)
    private Long id;

    @ApiModelProperty(value = "节假日名称", required = true, position = 2)
    private String holidayName;

    @ApiModelProperty(value = "节假日类型，支持类型：BIRTHDAY(生日)、LEGAL(法定)、CUSTOM(自定义)",
            allowableValues = "BIRTHDAY, LEGAL, CUSTOM", required = true, position = 3)
    private String holidayType;

    @ApiModelProperty(value = "年份", required = true, position = 4)
    private String year;

    @ApiModelProperty(value = "开始时间", required = true, position = 5)
    private LocalDate startDate;

    @ApiModelProperty(value = "结束时间", required = true, position = 6)
    private LocalDate endDate;

    @ApiModelProperty(value = "节假日状态状态，支持类型：ENABLE(启用)、DISABLE(无效)、FORBIDDEN(禁用)",
            allowableValues = "ENABLE, DISABLE, FORBIDDEN", required = true, position = 7)
    private String status;

    @ApiModelProperty(value = "创建人", required = true, position = 8)
    private String createBy;

    @ApiModelProperty(value = "创建时间", required = true, position = 9)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人", required = true, position = 10)
    private String updateBy;

    @ApiModelProperty(value = "修改时间", required = true, position = 11)
    private LocalDateTime updateTime;

    public static HolidayConfigRspVO of(HolidayConfigEntity entity) {
        return BeanUtil.toBean(entity, HolidayConfigRspVO.class);
    }
}
