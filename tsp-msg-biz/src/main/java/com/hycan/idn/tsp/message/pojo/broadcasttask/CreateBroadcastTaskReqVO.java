package com.hycan.idn.tsp.message.pojo.broadcasttask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 新增萌宠播报任务请求VO
 *
 * @author Liuyingjie
 * @datetime 2022/8/4 17:32
 */
@Data
@ApiModel(description = "新增萌宠播报任务请求VO")
public class CreateBroadcastTaskReqVO {

    @ApiModelProperty(value = "任务名称", required = true, position = 1)
    @NotBlank(message = "任务名称不能为空")
    @Length(min = 1, max = 30, message = "任务名称长度不符")
    private String taskName;

    @ApiModelProperty(value = "消息内容", required = true, position = 2)
    @NotBlank(message = "消息内容不能为空")
    @Length(min = 1, max = 30, message = "消息内容长度不符")
    private String msgContent;

    @ApiModelProperty(value = "执行策略；AVNT_ONLINE(车辆上电)、USER_LOGIN(账号登陆)",
            allowableValues = "AVNT_ONLINE, USER_LOGIN", required = true, position = 3)
    @Pattern(regexp = "AVNT_ONLINE|USER_LOGIN", message = "执行策略类型不符")
    @NotBlank(message = "执行策略不能为空")
    private String execPolicy;

    @NotBlank(message = "执行频率不能为空")
    @ApiModelProperty(value = "执行频率，支持类型：ONE_TIME:(推送一次)、EVERY_DAY:(每天一次)",
            allowableValues = "ONE_TIME, EVERY_DAY", required = true, position = 4)
    @Pattern(regexp = "EVERY_DAY|ONE_TIME", message = "执行频率类型不符")
    private String execRate;

    @ApiModelProperty(value = "发送范围：SINGLE(单个VIN)、MULTIPLE(批量VIN)、COMBINATION(组合条件))",
            allowableValues = "SINGLE, MULTIPLE, COMBINATION", required = true, position = 5)
    @Pattern(regexp = "SINGLE|MULTIPLE|COMBINATION", message = "发送范围类型不符")
    @NotBlank(message = "发送范围不能为空")
    private String scope;

    @ApiModelProperty(value = "节假日主键ID", required = true, position = 6)
    @NotNull(message = "节假日ID不能为空")
    private Long holidayId;

    @ApiModelProperty(value = "车型ID列表", position = 7)
    private List<Long> mtIds;

    @ApiModelProperty(value = "批量车辆VIN.xlsx文件(批量VIN时填写)", position = 8)
    private MultipartFile file;

    @ApiModelProperty(value = "车辆VIN码(单个VIN时填写)", position = 9)
    @Length(min = 1, max = 17, message = "车辆vin码长度不符合")
    private String vin;
}
