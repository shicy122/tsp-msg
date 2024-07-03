package com.hycan.idn.tsp.message.pojo.holidayconfig;

import com.hycan.idn.tsp.message.annotation.HolidayConfigExclusiveFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * 修改节假日配置请求VO
 *
 * @author Liuyingjie
 * @datetime 2022/8/4 16:30
 */
@Data
@HolidayConfigExclusiveFields(targetClass = UpdateHolidayConfigReqVO.class)
@ApiModel(description = "修改节假日配置请求VO")
public class UpdateHolidayConfigReqVO {

    @ApiModelProperty(value = "节假日名称", required = true, position = 1)
    @NotBlank(message = "节假日名称不能为空")
    @Length(min = 1, max = 30, message = "节假日名称长度不符")
    private String holidayName;

    @ApiModelProperty(value = "节假日类型: BIRTHDAY(生日)、LEGAL(法定)、CUSTOM(自定义)",
            allowableValues = "BIRTHDAY、LEGAL、CUSTOM", required = true, position = 2)
    @Pattern(regexp = "CUSTOM|BIRTHDAY", message = "节假日类型不符")
    @NotBlank(message = "节假日类型不能为空")
    private String holidayType;

    @ApiModelProperty(value = "开始日期", required = true, position = 3)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "开始时间不能为空")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期", required = true, position = 4)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "结束时间不能为空")
    private LocalDate endDate;
}
