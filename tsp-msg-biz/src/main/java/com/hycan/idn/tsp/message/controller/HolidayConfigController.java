package com.hycan.idn.tsp.message.controller;

import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.common.log.annotation.SysLog;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.*;
import com.hycan.idn.tsp.message.service.HolidayConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

/**
 * 节假日管理Controller
 *
 * @author Liuyingjie
 * @datetime 2022/8/2 17:36
 */
@Api(value = "HolidayConfigController", tags = "节假日管理模块")
@Validated
@RestController
@RequestMapping("/msg-center-svc/v1/holiday-configs")
public class HolidayConfigController {

    @Resource
    private HolidayConfigService holidayConfigService;

    /**
     * 添加节假日配置
     *
     * @param reqVO 新增节假日配置请求VO
     * @return 节假日配置响应VO
     */
    @ResponseBody
    @PostMapping
    @SysLog("添加节假日配置")
    @ApiOperation(value = "新增节假日配置", notes = "新增节假日配置")
    public R<HolidayConfigRspVO> createHolidayConfig(@RequestBody @Valid CreateHolidayConfigReqVO reqVO) {
        return R.ok(holidayConfigService.createHolidayConfig(reqVO));
    }

    /**
     * 修改节假日配置
     *
     * @param id    节假日配置ID
     * @param reqVO 修改节假日配置请求VO
     * @return 节假日配置响应VO
     */
    @ResponseBody
    @PutMapping("/{id}")
    @SysLog("修改节假日配置")
    @ApiOperation(value = "修改节假日配置", notes = "修改节假日配置")
    public R<HolidayConfigRspVO> updateHolidayConfig(@PathVariable Long id, @RequestBody @Valid UpdateHolidayConfigReqVO reqVO) {
        return R.ok(holidayConfigService.updateHolidayConfig(id, reqVO));
    }

    /**
     * 修改节假日配置状态
     *
     * @param id    节假日配置ID
     * @param reqVO 修改节假日状态请求VO
     * @return 节假日配置响应VO
     */
    @ResponseBody
    @PutMapping("/status/{id}")
    @SysLog("修改节假日配置状态")
    @ApiOperation(value = "修改节假日配置状态", notes = "修改节假日配置状态")
    public R<HolidayConfigRspVO> updateHolidayConfigStatus(@PathVariable("id") Long id, @RequestBody @Valid UpdateHolidayConfigStatusReqVO reqVO) {
        return R.ok(holidayConfigService.updateHolidayConfigStatus(id, reqVO.getStatus()));
    }

    /**
     * 删除节假日配置
     *
     * @param id 节假日配置ID
     * @return 操作结果
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    @SysLog("删除节假日配置")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功", response = R.class)
    })
    @ApiOperation(value = "删除节假日配置", notes = "删除节假日配置")
    public R<Integer> deleteHolidayConfig(@PathVariable Long id) {
        return R.ok(holidayConfigService.deleteHolidayConfigById(id));
    }

    /**
     * 查询节假日配置
     *
     * @param id 节假日配置ID
     * @return 节假日配置响应VO
     */
    @ResponseBody
    @GetMapping("/{id}")
    @ApiOperation(value = "查询节假日配置", notes = "查询节假日配置")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功", response = R.class)
    })
    public R<HolidayConfigRspVO> showHolidayConfig(@PathVariable Long id) {
        return R.ok(holidayConfigService.showHolidayConfig(id));
    }

    /**
     * 分页查询节假日配置
     *
     * @param reqVO 分页查询节假日配置请求VO
     * @return 分页返回节假日配置响应VO
     */
    @ResponseBody
    @GetMapping
    @ApiOperation(value = "分页查询节假日配置列表", notes = "分页查询节假日配置列表")
    public R<PageRspVO<HolidayConfigRspVO>> listHolidayConfig(@Valid PageHolidayConfigReqVO reqVO) {
        return R.ok(holidayConfigService.listHolidayConfig(reqVO));
    }

    /**
     * 同步国家法定节假日
     *
     * @param year 年份类型
     */
    @ResponseBody
    @SysLog("同步国家法定节假日")
    @PutMapping("holiday/sync")
    @ApiOperation(value = "同步国家法定节假日", notes = "同步国家法定节假日")
    public R<Void> syncLegalHolidayConfig(
            @Pattern(regexp = "CURRENT|NEXT", message = "年份类型不符") @RequestParam("year") String year) {
        holidayConfigService.syncLegalHolidayConfig(year);
        return R.ok();
    }
}
