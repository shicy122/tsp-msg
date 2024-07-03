package com.hycan.idn.tsp.message.controller;

import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.common.log.annotation.SysLog;
import com.hycan.idn.tsp.message.constant.ResourceTypeConstants;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.*;
import com.hycan.idn.tsp.message.facade.ObsRemoteFacade;
import com.hycan.idn.tsp.message.service.ResourceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 资源配置管理Controller
 *
 * @author liangwenqi
 * @datetime 2022-08-02 09:11
 */
@Api(value = "ResourceConfigController", tags = "资源配置管理模块")
@Validated
@Controller
@RequestMapping("/msg-center-svc/v1/resource-configs")
public class ResourceConfigController {

    @Resource
    private ResourceConfigService resourceConfigService;

    @Resource
    private ObsRemoteFacade obsRemoteFacade;

    /**
     * 新增资源配置
     */
    @SysLog("新增资源配置")
    @ResponseBody
    @PostMapping
    @ApiOperation(value = "新增资源配置", notes = "新增资源配置")
    public R<ResourceConfigRspVO> createResourceConfig(@RequestBody @Valid CreateResourceConfigReqVO reqVO) {
        return R.ok(resourceConfigService.createResourceConfig(reqVO));
    }

    /**
     * 修改资源配置
     */
    @SysLog("修改资源配置")
    @ResponseBody
    @PutMapping("/{id}")
    @ApiOperation(value = "修改资源配置", notes = "修改资源配置")
    public R<ResourceConfigRspVO> updateResourceConfig(@PathVariable("id") Long id, @RequestBody @Valid UpdateResourceConfigReqVO reqVO) {
        return R.ok(resourceConfigService.updateResourceConfig(id, reqVO));
    }

    /**
     * 更新资源配置状态
     *
     * @param id    资源配置ID
     * @param reqVO 修改节资源配置状态请求VO
     * @return {@link R}<{@link ResourceConfigRspVO}>
     */
    @SysLog("更新资源配置状态")
    @ResponseBody
    @PutMapping("status/{id}")
    @ApiOperation(value = "更新资源配置状态", notes = "更新资源配置状态")
    public R<ResourceConfigRspVO> updateResourceConfigStatus(@PathVariable("id") Long id, @RequestBody @Valid UpdateResourceConfigStatusReqVO reqVO) {
        return R.ok(resourceConfigService.updateResourceConfigStatus(id, reqVO.getStatus()));
    }

    /**
     * 删除资源配置
     *
     * @param id 资源配置ID
     * @return 操作结果
     */
    @SysLog("删除资源配置")
    @ResponseBody
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除资源配置", notes = "删除资源配置")
    public R<Integer> deleteResourceConfig(@PathVariable Long id) {
        return R.ok(resourceConfigService.deleteResourceConfig(id));
    }

    /**
     * 获取资源配置详细信息
     *
     * @param id 资源配置ID
     * @return 资源配置响应VO
     */
    @ResponseBody
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取资源配置详细信息", notes = "获取资源配置详细信息")
    public R<ResourceConfigRspVO> showResourceConfig(@PathVariable("id") Long id) {
        return R.ok(resourceConfigService.showResourceConfig(id));
    }

    /**
     * 查询资源配置列表
     */
    @ResponseBody
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "成功", response = R.class)
    })
    @ApiOperation(value = "查询资源配置列表", notes = "查询资源配置列表")
    public R<PageRspVO<ResourceConfigRspVO>> listResourceConfig(@Valid PageResourceConfigReqVO query) {
        return R.ok(resourceConfigService.selectResourcePage(query));
    }

    /**
     * 资源上传
     */
    @ResponseBody
    @PostMapping(value = "/upload")
    @ApiOperation(value = "资源上传", notes = "资源上传")
    public R<String> uploadResourceToObs(@RequestParam MultipartFile file, @RequestParam("resource_type") @Pattern(regexp = "VIDEO|ARTICLE|PICTURE|WALLPAPER|COVER|AUDIO",
            message = "支持资源类型(VIDEO:视频 ARTICLE:图文 PICTURE:图片 WALLPAPER:壁纸 COVER:封面 AUDIO:音频)") String resourceType) {
        return R.ok(resourceConfigService.uploadResourceToObs(file, resourceType));
    }

    /**
     * 图文渲染
     *
     * @param model       模型
     * @param resourceUrl 资源路径
     * @param resourceType        资源类型
     * @return {@link String}
     */
    @GetMapping("/detail")
    @ApiOperation(value = "图文渲染", notes = "图文渲染")
    public String articleResourceDetail(Model model,
                                        @NotBlank @RequestParam("resource_url") String resourceUrl,
                                        @NotBlank @RequestParam("resource_type") String resourceType) {
        if (resourceType.equalsIgnoreCase(ResourceTypeConstants.ARTICLE)) {
            final String articleContent = obsRemoteFacade.downloadFileFromObs(ResourceTypeConstants.ARTICLE, resourceUrl);
            model.addAttribute("article", articleContent);

            final String resourceName = resourceUrl
                    .substring(resourceUrl.lastIndexOf('/') + 1, resourceUrl.lastIndexOf('_'));
            model.addAttribute("title", resourceName);

            return "article";
        }
        return null;
    }
}
