package com.hycan.idn.tsp.message.pojo;

import com.hycan.idn.tsp.message.constant.CommonConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页请求DTO
 *
 * @author Shadow
 * @datetime 2024-02-20 16:07
 */
@Data
public class PageReqVO {

    /**
     * 起始页码
     */
    @Min(value = 1, message = "起始页码值不能小于1")
    @Max(value = Integer.MAX_VALUE)
    @ApiModelProperty(value = "起始页码，默认值：1", allowableValues = "range[0, 0x7fffffff]", position = 5)
    private Integer current = CommonConstants.DEFAULT_CURRENT;

    /**
     * 分页大小
     */
    @Min(value = 1, message = "分页大小不能小于1")
    @Max(value = 100, message = "分页大小不能大于100")
    @ApiModelProperty(value = "分页大小，默认值10", allowableValues = "range[1, 100]", position = 6)
    private Integer size = CommonConstants.DEFAULT_SIZE;
}
