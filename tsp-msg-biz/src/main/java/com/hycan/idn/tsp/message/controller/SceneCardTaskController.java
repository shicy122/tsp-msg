package com.hycan.idn.tsp.message.controller;

import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.common.log.annotation.SysLog;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.BaseTaskStatusVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.CreateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.PageSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.SceneCardTaskRspVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.UpdateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.service.SceneCardTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 场景卡片任务Controller
 *
 * @author liangwenqi
 * @datetime 2022-08-02 14:04
 */
@Api(value = "TspMsgTaskSceneController", tags = "场景卡片任务模块")
@Validated
@RestController
@RequestMapping("/msg-center-svc/v1/scene-tasks")
public class SceneCardTaskController {

    @Resource
    private SceneCardTaskService sceneCardTaskService;

    /**
     * 新增场景卡片任务
     *
     * @param reqVO 新增场景卡片任务请求VO
     * @return 场景卡片任务响应VO
     */
    @ResponseBody
    @SysLog("新增场景卡片任务")
    @PostMapping
    @ApiOperation(value = "新增场景卡片任务", notes = "新增场景卡片任务")
    public R<SceneCardTaskRspVO> createSceneCardTask(@Valid CreateSceneCardTaskReqVO reqVO) {
        return R.ok(sceneCardTaskService.createSceneCardTask(reqVO));
    }

    /**
     * 修改场景卡片任务
     *
     * @param id    场景卡片任务ID
     * @param reqVO 修改场景卡片任务请求VO
     * @return 场景卡片任务响应VO
     */
    @ResponseBody
    @SysLog("修改场景卡片任务")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改场景卡片任务", notes = "修改场景卡片任务")
    public R<SceneCardTaskRspVO> updateSceneCardTask(@PathVariable("id") Long id, @Valid UpdateSceneCardTaskReqVO reqVO) {
        return R.ok(sceneCardTaskService.updateSceneCardTask(id, reqVO));
    }

    /**
     * 修改场景卡片任务状态
     *
     * @param id    场景卡片任务ID
     * @param reqVO 修改任务状态VO
     * @return 场景卡片任务响应VO
     */
    @ResponseBody
    @SysLog("修改场景卡片任务状态")
    @PutMapping("status/{id}")
    @ApiOperation(value = "修改场景卡片任务状态", notes = "修改场景卡片任务状态")
    public R<SceneCardTaskRspVO> updateSceneCardTaskStatus(@PathVariable("id") Long id, @RequestBody @Valid BaseTaskStatusVO reqVO) {
        return R.ok(sceneCardTaskService.updateSceneCardTaskStatus(id, reqVO.getStatus()));
    }

    /**
     * 删除场景卡片任务
     *
     * @param id 场景卡片任务ID
     */
    @ResponseBody
    @SysLog("删除场景卡片任务")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除场景卡片任务", notes = "删除场景卡片任务")
    public R<Integer> deleteSceneCardTask(@PathVariable Long id) {
        return R.ok(sceneCardTaskService.deleteSceneCardTask(id));
    }

    /**
     * 查询场景卡片任务详情
     * @param id 场景卡片任务ID
     * @return 场景卡片任务响应VO
     */
    @ResponseBody
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询场景卡片任务详情", notes = "查询场景卡片任务详情")
    public R<SceneCardTaskRspVO> showSceneCardTask(@PathVariable("id") Long id) {
        return R.ok(sceneCardTaskService.showSceneCardTask(id));
    }

    /**
     * 分页查询场景卡片任务列表
     */
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功", response = R.class)
    })
    @GetMapping
    @ApiOperation(value = "分页查询场景卡片任务列表", notes = "分页查询场景卡片任务列表")
    public R<PageRspVO<SceneCardTaskRspVO>> listSceneCardTask(@Valid PageSceneCardTaskReqVO reqVO) {
        return R.ok(sceneCardTaskService.listSceneCardTask(reqVO));
    }
}
