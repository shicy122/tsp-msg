package com.hycan.idn.tsp.message.pojo.basetask;

import cn.hutool.core.bean.BeanUtil;
import com.hycan.idn.tsp.message.constant.TaskStatusConstants;
import com.hycan.idn.tsp.message.pojo.broadcasttask.CreateBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.UpdateBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.CreateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.UpdateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.entity.mysql.TaskVinRelationEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 基础任务对象VO
 *
 * @author Liuyingjie
 * @datetime 2022/8/28 22:12
 */
@Data
public class BaseTaskInfoVO {

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "消息标题")
    private String msgTitle;

    @ApiModelProperty(value = "消息内容")
    private String msgContent;

    @ApiModelProperty(value = "执行策略(AVNT_ONLINE:车辆上电，USER_LOGIN:账号登陆)")
    private String execPolicy;

    @ApiModelProperty(value = "发送范围(SINGLE:单个VIN MULTIPLE:批量VIN COMBINATION:组合条件)")
    private String scope;

    @ApiModelProperty(value = "状态(CREATED:已创建 PUBLISHED:已发布SENDING:推送中 CANCELED:已撤回 FINISHED:已结束)")
    private String status;

    @ApiModelProperty(value = "车型车系(组合条件时填写)")
    private List<Long> mtIds;

    @ApiModelProperty(value = "批量车辆VIN.xlsx文件(批量VIN时填写)")
    private MultipartFile file;

    @ApiModelProperty(value = "车辆VIN码(单个VIN时填写)")
    private String vin;

    @ApiModelProperty(hidden = true)
    private List<TaskVinRelationEntity> vinList;

    public static BaseTaskInfoVO of(CreateBroadcastTaskReqVO reqVO) {
        BaseTaskInfoVO baseTaskInfoVO = BeanUtil.toBean(reqVO, BaseTaskInfoVO.class);
        baseTaskInfoVO.setMsgTitle(reqVO.getTaskName());
        baseTaskInfoVO.setStatus(TaskStatusConstants.CREATED);
        return baseTaskInfoVO;
    }

    public static BaseTaskInfoVO of(UpdateBroadcastTaskReqVO reqVO) {
        BaseTaskInfoVO baseTaskInfoVO = BeanUtil.toBean(reqVO, BaseTaskInfoVO.class);
        baseTaskInfoVO.setMsgTitle(reqVO.getTaskName());
        return baseTaskInfoVO;
    }

    public static BaseTaskInfoVO of(CreateSceneCardTaskReqVO reqVO) {
        BaseTaskInfoVO baseTaskInfoVO = BeanUtil.toBean(reqVO, BaseTaskInfoVO.class);
        baseTaskInfoVO.setStatus(TaskStatusConstants.CREATED);
        return baseTaskInfoVO;
    }

    public static BaseTaskInfoVO of(UpdateSceneCardTaskReqVO reqVO) {
        return BeanUtil.toBean(reqVO, BaseTaskInfoVO.class);
    }

}
