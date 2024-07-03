package com.hycan.idn.tsp.message.pojo.scenecardtask;

import com.hycan.idn.tsp.message.pojo.taskrelation.QueryTaskResourceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 场景卡片任务响应VO
 *
 * @author liangwenqi
 * @datetime 2022-08-02 17:20
 */
@Data
@ApiModel(description = "场景卡片任务响应VO")
public class SceneCardTaskRspVO {

    @ApiModelProperty(value = "id", hidden = true, position = 1)
    private Long id;

    @ApiModelProperty(value = "基础任务ID", required = true, position = 2)
    private Long baseTaskId;

    @ApiModelProperty(value = "执行计划(RIGHT_NOW:立即 AT_TIME:定时)", required = true, position = 3)
    private String execPlan;

    @ApiModelProperty(value = "计划执行时间(定时必填)", position = 4)
    private LocalDateTime planTime;

    @ApiModelProperty(value = "消息状态：CREATED(已创建)、PUBLISHED(已发布)、CANCELED(已撤回))", required = true, position = 5)
    private String status;

    @ApiModelProperty(value = "保留时长(默认一天，单位:秒)", position = 6)
    private long duration;

    @ApiModelProperty(value = "任务名称", required = true, position = 7)
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @ApiModelProperty(value = "消息标题", required = true, position = 8)
    @NotBlank(message = "消息标题不能为空")
    private String msgTitle;

    @ApiModelProperty(value = "消息内容", required = true, position = 9)
    @NotBlank(message = "消息内容不能为空")
    private String msgContent;

    @ApiModelProperty(value = "执行策略(AVNT_ONLINE:车辆上电，USER_LOGIN:账号登陆)", required = true, position = 10)
    @NotNull(message = "执行策略不能为空")
    private String execPolicy;

    @ApiModelProperty(value = "消息配图资源ID", position = 11)
    private Long pictureId;

    @ApiModelProperty(value = "消息配图链接", position = 12)
    private String pictureUrl;

    @ApiModelProperty(value = "资源ID", position = 13)
    private Long resourceId;

    @ApiModelProperty(value = "资源链接", position = 14)
    private String resourceUrl;

    @ApiModelProperty(value = "资源类型", position = 15)
    private String resourceType;

    @ApiModelProperty(value = "触发动作列表", position = 16)
    private Object actions;

    @ApiModelProperty(value = "发送范围(SINGLE:单个VIN MULTIPLE:批量VIN COMBINATION:组合条件)", required = true, position = 16)
    private String scope;

    @ApiModelProperty(value = "组合条件车系", position = 17)
    private Object scopeValue;

    @ApiModelProperty(value = "发布时间", hidden = true, position = 18)
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "推送时间", hidden = true, position = 19)
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "撤回时间", hidden = true, position = 20)
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "结束时间", hidden = true, position = 21)
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "创建人", hidden = true, position = 22)
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = true, position = 23)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人", hidden = true, position = 24)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = true, position = 25)
    private LocalDateTime updateTime;

    public static SceneCardTaskRspVO of(SceneCardTaskRspVO rspVO, QueryTaskResourceDTO dto) {
        rspVO.setPictureId(dto.getPictureId());
        rspVO.setPictureUrl(dto.getPictureUrl());
        rspVO.setResourceType(dto.getResourceType());
        rspVO.setResourceId(dto.getResourceId());
        rspVO.setResourceUrl(dto.getResourceUrl());
        return rspVO;
    }
}

