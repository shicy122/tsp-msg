package com.hycan.idn.tsp.message.pojo.basetask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改任务状态VO
 *
 * @author Liuyingjie
 * @datetime 2022/9/9 10:42
 */
@Data
@ApiModel(description = "修改任务状态VO")
public class BaseTaskStatusVO {

    @ApiModelProperty(value = "消息状态：PUBLISHED(已发布)、CANCELED(已撤回)、FINISHED(已结束))", required = true,
            allowableValues = "PUBLISHED, CANCELED, FINISHED,", position = 1)
    @Pattern(regexp = "PUBLISHED|CANCELED|FINISHED", message = "任务状态类型不符")
    @NotBlank(message = "任务类型不能为空")
    private String status;
}
