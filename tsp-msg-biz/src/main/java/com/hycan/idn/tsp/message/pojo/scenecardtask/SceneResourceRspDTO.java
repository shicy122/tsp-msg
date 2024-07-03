package com.hycan.idn.tsp.message.pojo.scenecardtask;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 资源配置对象
 * 
    @author liangwenqi
 * @date 2022-08-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneResourceRspDTO {

    /** 资源id */
    @ApiModelProperty(value = "资源id")
    private Long resourceId;

    /** 场景卡片id */
    @ApiModelProperty(value = "场景卡片id", hidden = true)
    private Long sceneId;

    /** 资源类型(VIDEO:视频 ARTICLE:图文 PICTURE:图片 WALLPAPER:壁纸) */
    @ApiModelProperty(value = "资源类型(VIDEO:视频 ARTICLE:图文 PICTURE:图片 WALLPAPER:壁纸)")
    private String resourceType;

    /** 资源链接 */
    @ApiModelProperty(value = "资源链接")
    private String resourceUrl;

    /** 资源链接 */
    @ApiModelProperty(value = "资源链接")
    private String source;

}
