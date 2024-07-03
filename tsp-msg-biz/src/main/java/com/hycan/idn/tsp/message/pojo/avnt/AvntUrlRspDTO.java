package com.hycan.idn.tsp.message.pojo.avnt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
@NoArgsConstructor
@AllArgsConstructor
public class AvntUrlRspDTO {

    @ApiModelProperty(value = "发送的url地址", required = true, position = 2)
    private String url;
}