package com.hycan.idn.tsp.message.pojo.holidayconfig;

import com.hycan.idn.tsp.message.pojo.PageReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 分页查询节假日配置请求VO
 *
 * @author Liuyingjie
 * @datetime 2022/8/31 16:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "分页查询节假日配置请求VO")
public class PageHolidayConfigReqVO extends PageReqVO {

    @ApiModelProperty(value = "节假日名称", position = 1)
    @Length(min = 1, max = 30, message = "节假日名称长度不符")
    private String holidayName;

    @ApiModelProperty(value = "节假日类型，支持类型：BIRTHDAY(生日)、LEGAL(法定)、CUSTOM(自定义)",
            allowableValues = "BIRTHDAY, LEGAL, CUSTOM", position = 2)
    @Pattern(regexp = "BIRTHDAY|LEGAL|CUSTOM", message = "节假日类型不符")
    private String holidayType;

    @Pattern(regexp = "ENABLE|DISABLE|FORBIDDEN", message = "状态类型不符")
    @ApiModelProperty(value = "节假日状态状态，支持类型：ENABLE(启用)、DISABLE(无效)、FORBIDDEN(禁用)",
            allowableValues = "ENABLE, DISABLE, FORBIDDEN", position = 3)
    private String status;
}
