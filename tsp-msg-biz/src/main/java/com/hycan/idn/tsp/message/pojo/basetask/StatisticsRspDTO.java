package com.hycan.idn.tsp.message.pojo.basetask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推送统计请求返回DTO
 * @author Liuyingjie
 * @datetime 2022/8/18 17:19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "推送统计请求返回DTO")
public class StatisticsRspDTO {

    /**
     * 推送状态(SUCCESS:成功 FAILURE:失败 UNSENT:未推送)
     */
    @ApiModelProperty(value = "推送状态：SUCCESS(成功)、FAILURE(失败)、UNSENT(未推送)", required = true, position = 1)
    private String status;

    /**
     * 车辆数
     */
    @ApiModelProperty(value = "车辆数", required = true, position = 2)
    private int count;
}
