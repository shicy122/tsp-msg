package com.hycan.idn.tsp.message.pojo.holidayconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改节假日状态请求VO
 *
 * @author Liuyingjie
 * @datetime 2022/9/9 9:13
 */
@Data
@ApiModel(description = "修改节假日状态请求VO")
public class UpdateHolidayConfigStatusReqVO {

    @Pattern(regexp = "ENABLE|FORBIDDEN", message = "状态类型不符")
    @ApiModelProperty(value = "状态，支持类型：ENABLE(启用)、FORBIDDEN(禁用)", required = true,
            allowableValues = "ENABLE, FORBIDDEN", position = 1)
    @NotBlank(message = "状态类型不能为空")
    private String status;
}
