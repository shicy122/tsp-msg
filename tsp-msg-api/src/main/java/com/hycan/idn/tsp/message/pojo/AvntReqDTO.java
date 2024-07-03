package com.hycan.idn.tsp.message.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

/**
 * Avnt请求参数
 *
 * @Author:Liuyingjie
 * @Date:2022/8/31
 * @Time:17:14
 */
@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "Avnt请求参数")
public class AvntReqDTO {

    /**
     * 车架号
     */
    @ApiModelProperty(value = "车架号")
    private String vin;

    /**
     * hex
     */
    @ApiModelProperty(value = "hex")
    private String hex;

}
