package com.hycan.idn.tsp.message.pojo.broadcasttask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 萌宠播报任务响应VO
 *
 * @author Liuyingjie
 * @datetime 2022/8/23 16:36
 */
@Data
@ApiModel(description = "萌宠播报任务响应VO")
public class BroadcastTaskRspVO {

    @ApiModelProperty(value = "萌宠播报任务ID", required = true, position = 1)
    private Long id;

    @ApiModelProperty(value = "基础任务ID", required = true, position = 2)
    private Long baseTaskId;

    @ApiModelProperty(value = "播报任务名称", required = true, position = 3)
    private String taskName;

    @ApiModelProperty(value = "消息内容", required = true, position = 4)
    private String msgContent;

    @ApiModelProperty(value = "执行策略(AVNT_ONLINE:车辆上电，USER_LOGIN:账号登陆)", required = true, position = 5)
    private String execPolicy;

    @ApiModelProperty(value = "执行频率(ONE_TIME:推送一次 EVERY_DAY:每天一次)", required = true, position = 6)
    private String execRate;

    @ApiModelProperty(value = "消息状态：CREATED(已创建)、PUBLISHED(已发布)、CANCELED(已撤回))", required = true, position = 7)
    private String status;

    @ApiModelProperty(value = "发送范围 (SINGLE:单个VIN、MULTIPLE:批量VIN、COMBINATION:组合条件)", required = true, position = 8)
    private String scope;

    @ApiModelProperty(value = "节假日主键ID", required = true, position = 9)
    private Long holidayConfigId;

    @ApiModelProperty(value = "节假日名称", required = true, position = 10)
    private String holidayConfigName;

    @ApiModelProperty(value = "节假日年份", required = true, position = 11)
    private String holidayYear;

    @ApiModelProperty(value = "创建时间", required = true, position = 12)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间", required = true, position = 13)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "发布时间", required = true, position = 14)
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "推送时间", required = true, position = 15)
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "撤回时间", required = true, position = 16)
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "结束时间", required = true, position = 17)
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "创建人", required = true, position = 18)
    private String createBy;

    @ApiModelProperty(value = "修改人", required = true, position = 19)
    private String updateBy;

    @ApiModelProperty(value = "发送范围取值", position = 20)
    private Object scopeValue;
}
