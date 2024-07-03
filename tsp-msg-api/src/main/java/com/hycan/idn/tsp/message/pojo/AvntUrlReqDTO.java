package com.hycan.idn.tsp.message.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

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
public class AvntUrlReqDTO {

    /**
     * hex
     */
    @NotBlank
    @ApiModelProperty(value = "hex", required = true, position = 1)
    private String hex;

    /**
     * 发送的url地址
     */
    @ApiModelProperty(value = "发送的url地址", required = true, position = 2)
    private String url;
}
