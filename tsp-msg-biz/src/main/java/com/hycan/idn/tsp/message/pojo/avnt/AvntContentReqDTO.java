package com.hycan.idn.tsp.message.pojo.avnt;

import com.hycan.idn.tsp.message.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * Avnt请求参数
 *
 * @Author:Liuyingjie
 * @Date:2022/8/31
 * @Time:17:14
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Avnt请求参数")
@ToString
public class AvntContentReqDTO {

    @ApiModelProperty(value = "菜单id" )
    private Long menuId;

    @ApiModelProperty(value = "起始页码，默认值：1", allowableValues = "range[0, 0x7fffffff]", position = 5)
    private Integer current = CommonConstants.DEFAULT_CURRENT;

    @ApiModelProperty(value = "分页大小，默认值10", allowableValues = "range[1, 100]", position = 6)
    private Integer size = CommonConstants.DEFAULT_SIZE;
}
