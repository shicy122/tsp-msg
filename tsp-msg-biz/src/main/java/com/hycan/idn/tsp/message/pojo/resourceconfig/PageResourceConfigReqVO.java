package com.hycan.idn.tsp.message.pojo.resourceconfig;

import com.hycan.idn.tsp.message.pojo.PageReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;


/**
 * 分页查询资源配置请求VO
 *
 * @author liangwenqi
 * @datetime 2022-08-02 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "分页查询资源配置请求VO")
public class PageResourceConfigReqVO extends PageReqVO {

    @Length(min = 1, max = 30, message = "资源名称长度需要在1~30之间")
    @ApiModelProperty(value = "资源名称", position = 1)
    private String resourceName;

    @Pattern(regexp = "VIDEO|ARTICLE|PICTURE|WALLPAPER|COVER|AUDIO",
            message = "支持资源类型(VIDEO:视频 ARTICLE:图文 PICTURE:图片 WALLPAPER:壁纸 COVER:封面 AUDIO:音频)")
    @ApiModelProperty(value = "支持资源类型(VIDEO:视频 ARTICLE:图文 PICTURE:图片 WALLPAPER:壁纸 COVER:封面 AUDIO:音频)",
            allowableValues = "VIDEO, ARTICLE, PICTURE, WALLPAPER,COVER,AUDIO", position = 2)
    private String resourceType;

    @Pattern(regexp = "ENABLE|FORBIDDEN", message = "支持状态ENABLE:启用 FORBIDDEN:禁用")
    @ApiModelProperty(value = "支持状态(ENABLE:启用 FORBIDDEN:禁用)", allowableValues = "ENABLE, FORBIDDEN", position = 3)
    private String status;
}
