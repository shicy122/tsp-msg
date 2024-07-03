package com.hycan.idn.tsp.message.pojo;

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
public class AvntGuideContentRspDTO {

    @ApiModelProperty(value = "标题", required = true, position = 1)
    private String title;

    @ApiModelProperty(value = "封面", required = true, position = 2)
    private String cover;

    @ApiModelProperty(value = "url地址", required = true, position = 3)
    private String video;
}
