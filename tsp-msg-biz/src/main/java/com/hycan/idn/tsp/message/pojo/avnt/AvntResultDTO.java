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
public class AvntResultDTO {

    @ApiModelProperty(value = "0成功1失败", required = true, position = 1)
    private Integer code;

    @ApiModelProperty(value = "成功失败", required = true, position = 1)
    private String msg;


    public static AvntResultDTO getSuccess(){
        return new AvntResultDTO(0, "成功");
    }

    public static AvntResultDTO getFail(){
        return new AvntResultDTO(1, "失败");
    }
}