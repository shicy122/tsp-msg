package com.hycan.idn.tsp.message.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
public class AvntGuideRspDTO {

    @ApiModelProperty(value = "菜单", required = true, position = 1)
    private String menu;

    @ApiModelProperty(value = "菜单编号", required = true, position = 2)
    private String menuNo;

    @ApiModelProperty(value = "内容列表", required = true, position = 3)
    private List<AvntGuideContentRspDTO> content;
}
