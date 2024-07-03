package com.hycan.idn.tsp.message.pojo.resourceconfig;

import com.hycan.idn.tsp.message.annotation.ResourceConfigExclusiveFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 新增节假日配置请求VO
 *
 * @author liangwenqi
 * @datetime 2022-08-02 16:30
 */
@Data
@ResourceConfigExclusiveFields(targetClass = CreateResourceConfigReqVO.class)
@ApiModel(description = "新增节假日配置请求VO")
public class CreateResourceConfigReqVO {

    @ApiModelProperty(value = "资源名称", required = true, position = 1)
    @NotBlank(message = "资源名称不能为空")
    @Length(min = 1, max = 30, message = "资源名称长度不符")
    private String resourceName;

    @Pattern(regexp = "VIDEO|ARTICLE|PICTURE|WALLPAPER|COVER|AUDIO", message = "资源类型不符")
    @ApiModelProperty(value = "资源类型: VIDEO:视频、ARTICLE:图文、PICTURE:图片、WALLPAPER:壁纸、COVER:封面、AUDIO:音频",
            allowableValues = "VIDEO、ARTICLE、PICTURE、WALLPAPER、COVER、AUDIO", required = true, position = 2)
    @NotBlank(message = "资源类型不能为空")
    private String resourceType;

    @ApiModelProperty(value = "资源大小(单位:KB)", position = 3)
    @Min(value = 0, message = "资源大小不合法")
    private Long resourceSize;

    @ApiModelProperty(value = "资源链接(非图文类型时内容不能为空)", position = 4)
    private String resourceUrl;

    @ApiModelProperty(value = "图文内容(图文类型时内容不能为空)", position = 5)
    private String articleContent;
}
