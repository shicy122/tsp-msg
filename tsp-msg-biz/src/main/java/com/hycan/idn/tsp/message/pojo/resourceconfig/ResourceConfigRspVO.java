package com.hycan.idn.tsp.message.pojo.resourceconfig;

import cn.hutool.core.bean.BeanUtil;
import com.hycan.idn.tsp.message.entity.mysql.ResourceConfigEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资源配置响应VO
 * 
 * @author liangwenqi
 * @datetime 2022-08-02 17:53
 */
@Data
@ApiModel(description = "资源配置响应VO")
public class ResourceConfigRspVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源类型(VIDEO:视频 ARTICLE:图文 PICTURE:图片 WALLPAPER:壁纸)")
    private String resourceType;

    @ApiModelProperty(value = "资源大小(单位:KB)")
    private Long resourceSize;

    @ApiModelProperty(value = "资源链接")
    private String resourceUrl;

    @ApiModelProperty(value = "状态(ENABLE:启用 FORBIDDEN:禁用)")
    private String status;

    @ApiModelProperty(value = "图文类型：内容不能为空", position = 6)
    private String articleContent;

    @ApiModelProperty(value = "创建人", required = true, position = 8)
    private String createBy;

    @ApiModelProperty(value = "创建时间", required = true, position = 9)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人", required = true, position = 10)
    private String updateBy;

    @ApiModelProperty(value = "修改时间", required = true, position = 11)
    private LocalDateTime updateTime;

    public static ResourceConfigRspVO of(ResourceConfigEntity entity) {
        return BeanUtil.toBean(entity, ResourceConfigRspVO.class);
    }

    public static ResourceConfigRspVO of(ResourceConfigEntity entity, String articleContent) {
        ResourceConfigRspVO rspVO = BeanUtil.toBean(entity, ResourceConfigRspVO.class);
        rspVO.setArticleContent(articleContent);
        return rspVO;
    }
}
