package com.hycan.idn.tsp.message.controller;

import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.common.log.annotation.SysLog;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.BaseTaskStatusVO;
import com.hycan.idn.tsp.message.pojo.basetask.ImportVinReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.BroadcastTaskRspVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.CreateBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.PageBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.pojo.broadcasttask.UpdateBroadcastTaskReqVO;
import com.hycan.idn.tsp.message.service.BroadcastTasService;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 萌宠播报任务模块
 *
 * @author Liuyingjie
 * @datetime 2022/8/2 17:44
 */
@Api(value = "BroadcastTaskController", tags = "萌宠播报任务模块")
@Validated
@RestController
@RequestMapping("/msg-center-svc/v1/holiday-tasks")
public class BroadcastTaskController {

    @Resource
    private BroadcastTasService broadcastTasService;

    /**
     * 添加萌宠播报任务
     *
     * @param reqVO 添加萌宠播报任务请求VO
     * @return 萌宠播报任务响应VO
     */
    @ResponseBody
    @PostMapping
    @SysLog("添加萌宠播报任务")
    @ApiOperation(value = "添加萌宠播报任务", notes = "添加萌宠播报任务")
    public R<BroadcastTaskRspVO> createBroadcastTask(@Valid CreateBroadcastTaskReqVO reqVO) {
        return R.ok(broadcastTasService.createBroadcastTask(reqVO));
    }

    /**
     * 修改萌宠播报任务表
     *
     * @param id    萌宠播报任务ID
     * @param reqVO 修改萌宠播报任务请求VO
     * @return 萌宠播报任务响应VO
     */
    @ResponseBody
    @PutMapping("/{id}")
    @SysLog("修改萌宠播报任务")
    @ApiOperation(value = "修改萌宠播报任务", notes = "修改萌宠播报任务")
    public R<BroadcastTaskRspVO> updateHolidayTask(@PathVariable Long id, @Valid UpdateBroadcastTaskReqVO reqVO) {
        return R.ok(broadcastTasService.updateBroadcastTask(id, reqVO));
    }

    /**
     * 修改萌宠播报任务状态
     *
     * @param id  萌宠播报任务ID
     * @param dto 修改任务状态VO
     * @return 萌宠播报任务响应VO
     */
    @ResponseBody
    @PutMapping("/status/{id}")
    @SysLog("修改萌宠播报任务状态")
    @ApiOperation(value = "修改萌宠播报任务状态", notes = "修改萌宠播报任务状态")
    public R<BroadcastTaskRspVO> updateBroadcastTaskStatus(@PathVariable Long id, @RequestBody @Valid BaseTaskStatusVO dto) {
        return R.ok(broadcastTasService.updateBroadcastTaskStatus(id, dto.getStatus()));
    }

    /**
     * 删除萌宠播报任务
     *
     * @param id 萌宠播报任务ID
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    @SysLog("删除萌宠播报任务表")
    @ApiOperation(value = "删除萌宠播报任务表", notes = "删除萌宠播报任务表")
    public R<Integer> deleteBroadcastTask(@PathVariable Long id) {
        return R.ok(broadcastTasService.deleteBroadcastTask(id));
    }

    /**
     * 查询萌宠播报详情
     *
     * @param id 萌宠播报任务ID
     * @return 萌宠播报详情响应VO
     */
    @ResponseBody
    @GetMapping("/{id}")
    @ApiOperation(value = "查询萌宠播报任务", notes = "查询萌宠播报任务")
    public R<BroadcastTaskRspVO> showBroadcastTask(@PathVariable Long id) {
        return R.ok(broadcastTasService.showBroadcastTask(id));
    }

    /**
     * 分页查询萌宠播报任务
     *
     * @param reqVO 分页查询萌宠播报请求VO
     * @return 分页返回萌宠播报响应VO
     */
    @ResponseBody
    @GetMapping
    @ApiOperation(value = "分页查询萌宠播报任务列表", notes = "分页查询萌宠播报任务列表")
    public R<PageRspVO<BroadcastTaskRspVO>> listBroadcastTask(@Valid PageBroadcastTaskReqVO reqVO) {
        return R.ok(broadcastTasService.listBroadcastTask(reqVO));
    }
}
