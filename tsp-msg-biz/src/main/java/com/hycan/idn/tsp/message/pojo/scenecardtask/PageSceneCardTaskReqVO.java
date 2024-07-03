package com.hycan.idn.tsp.message.pojo.scenecardtask;

import com.hycan.idn.tsp.message.pojo.PageReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 * 分页查询场景卡片任务请求VO
 *
 * @author liangwenqi
 * @datetime 2022-08-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "分页查询场景卡片任务请求VO")
public class PageSceneCardTaskReqVO extends PageReqVO {

    @ApiModelProperty(value = "任务名称", position = 1)
    private String taskName;

    @ApiModelProperty(value = "支持状态：CREATED:已创建 PUBLISHED:已发布 SENDING:推送中 CANCELED:已撤回 FINISHED:已结束 ",
            allowableValues = "CREATED, PUBLISHED, SENDING, FINISHED, CANCELED", position = 2)
    private String status;

    @ApiModelProperty(value = "推送开始时间", position = 3)
    private LocalDateTime sendStartTime;

    @ApiModelProperty(value = "推送开始时间", position = 4)
    private LocalDateTime sendEndTime;
}
