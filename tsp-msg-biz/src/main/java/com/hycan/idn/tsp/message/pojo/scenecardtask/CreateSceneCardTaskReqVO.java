package com.hycan.idn.tsp.message.pojo.scenecardtask;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hycan.idn.tsp.message.annotation.SceneCardTaskExclusiveFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 新增场景卡片任务请求VO
 *
 * @author liangwenqi
 * @datetime 2022-08-02 11:20
 */
@Data
@SceneCardTaskExclusiveFields(targetClass = CreateSceneCardTaskReqVO.class)
@ApiModel(description = "新增场景卡片任务请求VO")
public class CreateSceneCardTaskReqVO {

    @Pattern(regexp = "RIGHT_NOW|AT_TIME", message = "执行计划支持(RIGHT_NOW:立即、AT_TIME:定时)")
    @ApiModelProperty(
            value = "执行计划支持(RIGHT_NOW:立即 AT_TIME:定时)",
            allowableValues ="RIGHT_NOW, AT_TIME", required = true, position = 1)
    @NotBlank(message = "执行计划不能为空或类型不合法")
    private String execPlan;

    @ApiModelProperty(value = "计划执行时间(定时必填)", position = 2)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "计划执行时间必须为当前之间之后")
    private LocalDateTime planTime;

    @Min(0L)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "保留时长(默认一天，单位:秒)", required = true, position = 3)
    @NotNull(message = "保留时长不能为空")
    private Long duration;

    @ApiModelProperty(value = "任务名称", required = true, position = 4)
    @NotBlank(message = "任务名称不能为空")
    @Size(max = 30, message = "任务名称长度不符")
    private String taskName;

    @ApiModelProperty(value = "消息标题", required = true, position = 5)
    @Pattern(regexp = "小智推荐", message = "消息标题仅支持[小智推荐]")
    @NotBlank(message = "消息标题不能为空")
    private String msgTitle;

    @ApiModelProperty(value = "消息内容", required = true, position = 6)
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 30, message = "消息内容长度不符")
    private String msgContent;

    @ApiModelProperty(value = "支持执行策略(AVNT_ONLINE:车辆上电、USER_LOGIN:账号登陆)", allowableValues = "AVNT_ONLINE, USER_LOGIN", required = true, position = 7)
    @Pattern(regexp = "AVNT_ONLINE|USER_LOGIN", message = "支持执行策略(AVNT_ONLINE:车辆上电、USER_LOGIN:账号登陆)")
    @NotBlank(message = "执行策略不能为空")
    private String execPolicy;

    @ApiModelProperty(value = "配图资源ID", position = 8)
    private Long pictureId;

    @ApiModelProperty(value = "资源ID", position = 10)
    private Long resourceId;

    @ApiModelProperty(value = "支持资源类型(VIDEO:视频、ARTICLE:图文、PICTURE:图片、WALLPAPER:壁纸)",
            allowableValues = "VIDEO, ARTICLE, PICTURE, WALLPAPER", position = 11)
    @Pattern(regexp = "VIDEO|ARTICLE|PICTURE|WALLPAPER", message = "支持资源类型(VIDEO:视频、 ARTICLE:图文、PICTURE:图片、WALLPAPER:壁纸)")
    private String resourceType;

    @ApiModelProperty(value = "自定义资源链接", position = 12)
    private String customUrl;

    @ApiModelProperty(
            value = "按钮列表Json结构([{\"no\" : \"1\",\"button\" : \"合创美食\",\"action\" : \"OPEN_APP_STORE\"," +
                    "\"params\" :{\"keyword\": \"合创方块\"}}])", position = 13)
    private String actions;

    @NotBlank(message = "发送范围不能为空")
    @ApiModelProperty(value = "支持发送范围(SINGLE:单个VIN、MULTIPLE:批量VIN、COMBINATION:组合条件)",
            allowableValues = "SINGLE, MULTIPLE, COMBINATION", required = true, position = 14)
    @Pattern(regexp = "SINGLE|MULTIPLE|COMBINATION", message = "支持发送范围(SINGLE:单个VIN、MULTIPLE:批量VIN、COMBINATION:组合条件)")
    private String scope;

    @ApiModelProperty(value = "车型ID列表", position = 15)
    private List<Long> mtIds;

    @ApiModelProperty(value = "批量车辆VIN.xlsx文件(批量VIN时填写)", position = 16)
    private MultipartFile file;

    @ApiModelProperty(value = "车辆VIN码(单个VIN时填写)", position = 17)
    private String vin;
}
