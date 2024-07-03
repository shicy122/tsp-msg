package com.hycan.idn.tsp.message.pojo.basetask;

import com.hycan.idn.tsp.message.pojo.PageReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 推送详情请求VO
 *
 * @author Liuyingjie
 * @datetime 2022/9/4 21:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "推送详情请求DTO")
public class PushTaskDetailReqVO extends PageReqVO {

    /**
     * 基础任务ID
     */
    @ApiModelProperty(value = "基础任务ID", required = true, position = 1)
    @NotNull(message = "基础任务ID不能为空")
    private Long baseTaskId;

    /**
     * 车系ID
     */
    @ApiModelProperty(value = "车系ID", position = 2)
    @Length(min = 1, message = "车系ID长度不符")
    private String serialId;

    /**
     * VIN码
     */
    @ApiModelProperty(value = "VIN码", position = 3)
    @Length(min = 1, message = "VIN码长度不符")
    private String vin;


    /**
     * 状态(SUCCESS:成功 FAILURE:失败 UNSENT:未推送)
     */
    @ApiModelProperty(value = "推送状态：SUCCESS(成功)、FAILURE(失败)、UNSENT(未推送)",
            allowableValues = "SUCCESS, FAILURE, UNSENT", position = 4)
    @Pattern(regexp = "SUCCESS|FAILURE|UNSENT", message = "推送状态类型不符")
    private String status;
}
