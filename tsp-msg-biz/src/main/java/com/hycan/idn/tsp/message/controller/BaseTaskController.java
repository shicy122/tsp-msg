package com.hycan.idn.tsp.message.controller;

import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.*;
import com.hycan.idn.tsp.message.service.TaskDetailRecordService;
import com.hycan.idn.tsp.message.service.TaskExecRecordService;
import com.hycan.idn.tsp.message.service.TaskRelationService;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * 基础消息任务Controller
 *
 * @author shichongying
 * @datetime 2024-02-20 14:56
 */
@Api(value = "MsgTaskController", tags = "消息任务公共模块")
@Validated
@RestController
@RequestMapping("/msg-center-svc/v1/base-tasks")
public class BaseTaskController {

    @Resource
    private TaskRelationService taskRelationService;

    @Resource
    private TaskExecRecordService taskExecRecordService;

    /**
     * 推送详情列表
     */
    @ResponseBody
    @GetMapping("/detail")
    @ApiOperation(value = "推送详情", notes = "推送详情")
    public R<PageRspVO<PushTaskDetailRspVO>> listPushDetail(@Valid PushTaskDetailReqVO reqVO) {
        return R.ok(taskExecRecordService.listPushDetail(reqVO));
    }

    /**
     * 推送统计
     */
    @ResponseBody
    @GetMapping("/statistic")
    @ApiOperation(value = "推送统计", notes = "推送统计")
    public R<List<StatisticsRspDTO>> showPushStatistic(
            @NotNull(message = "任务ID不能为空") @RequestParam("baseTaskId") Long baseTaskId) {
        return R.ok(taskExecRecordService.showPushStatistic(baseTaskId));
    }

    /**
     * 下载批量导入VIN模板
     */
    @ResponseBody
    @PostMapping("/template")
    @ApiOperation(value = "下载批量导入VIN模板", notes = "下载批量导出VIN模板")
    @ResponseExcel(name = "下载批量导入VIN模板")
    public List<ImportVinReqVO> downloadVinTemplate() {
        return Collections.singletonList(new ImportVinReqVO());
    }

    /**
     * 批量导出已上传VIN列表
     */
    @PostMapping("/export")
    @ApiOperation(value = "批量导出已上传VIN列表", notes = "批量导出已上传VIN列表")
    @ResponseExcel(name = "批量导出已上传VIN列表")
    public List<ExportVinRspVO> exportVines(
            @NotNull(message = "任务ID不能为空") @RequestParam("baseTaskId") Long baseTaskId) {
        return taskRelationService.exportVins(baseTaskId);
    }
}
