package com.hycan.idn.tsp.message.pojo.broadcasttask;

import com.hycan.idn.tsp.message.pojo.PageReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 分页查询萌宠播报任务请求VO
 *
 * @author Liuyingjie
 * @datetime 2022/9/1 23:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "分页查询萌宠播报任务请求VO")
public class PageBroadcastTaskReqVO extends PageReqVO {

    @ApiModelProperty(value = "任务名称", position = 1)
    @Length(min = 1, max = 30, message = "任务名称长度不符")
    private String taskName;

    @ApiModelProperty(value = "消息状态：CREATED(已创建)、PUBLISHED(已发布)、SENDING(推送中)、CANCELED(已撤回)、FINISHED(已结束))",
            allowableValues = "CREATED, PUBLISHED, SENDING, CANCELED, FINISHED,", position = 2)
    @Pattern(regexp = "CREATED|PUBLISHED|SENDING|CANCELED|FINISHED", message = "消息状态类型不符")
    private String status;

    @ApiModelProperty(value = "开始推送时间", position = 5)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startSendTime;

    @ApiModelProperty(value = "结束推送时间", position = 6)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endSendTime;
}
