package com.hycan.idn.tsp.message.pojo.basetask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

/**
 * 推送详情响应VO
 *
 * @author shichongying
 * @datetime 2022/9/4 22:09
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(description = "推送详情请求返回DTO")
public class PushTaskDetailRspVO {

    @ApiModelProperty(value = "VIN码", required = true, position = 1)
    private String vin;

    @ApiModelProperty(value = "车系", required = true, position = 2)
    private String serial;

    @ApiModelProperty(value = "车型", required = true, position = 4)
    private String mt;

    @ApiModelProperty(value = "推送时间", position = 7)
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "推送状态：SUCCESS(成功)、FAILURE(失败)、UNSENT(未推送)", required = true, position = 8)
    private String status;

}
