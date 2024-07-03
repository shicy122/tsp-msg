package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hycan.idn.tsp.common.mybatis.base.BaseEntity;
import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.constant.HolidayTypeConstants;
import com.hycan.idn.tsp.message.pojo.resourceconfig.CreateResourceConfigReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * 图片、壁纸、视频等资源配置对象
 * 
 * @author shichongying
 * @datetime 2024-02-23 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("resource_config")
public class ResourceConfigEntity extends BaseEntity {

    private static final long serialVersionUID = -1861678863837571634L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 资源名称 */
    private String resourceName;

    /** 资源类型(VIDEO:视频, ARTICLE:图文, PICTURE:图片, WALLPAPER:壁纸) */
    private String resourceType;

    /** 资源大小(单位:KB) */
    private Long resourceSize;

    /** 资源链接 */
    private String resourceUrl;

    /** 数据来源(默认SYS)(SYS:系统录入, CUSTOM:自定义) */
    private String source;

    /** 状态(ENABLE:启用, FORBIDDEN:禁用) */
    private String status;

    public static ResourceConfigEntity of(CreateResourceConfigReqVO reqVO) {
        ResourceConfigEntity entity = new ResourceConfigEntity();
        entity.setResourceName(reqVO.getResourceName());
        entity.setResourceType(reqVO.getResourceType());
        entity.setResourceSize(reqVO.getResourceSize());
        entity.setResourceUrl(reqVO.getResourceUrl());
        entity.setSource(CommonConstants.SYS);
        entity.setStatus(BizDataStatusConstants.ENABLE);
        return entity;
    }

    public static void of(ResourceConfigEntity entity, String resourceName, Long resourceSize, String resourceUrl) {
        entity.setResourceName(resourceName);
        entity.setResourceSize(resourceSize);
        entity.setResourceUrl(resourceUrl);
    }

    public static ResourceConfigEntity of(String customUrl, String resourceType) {
        ResourceConfigEntity entity = new ResourceConfigEntity();
        entity.setResourceName(UUID.randomUUID().toString());
        entity.setResourceType(resourceType);
        entity.setResourceSize(0L);
        entity.setResourceUrl(customUrl);
        entity.setSource(HolidayTypeConstants.CUSTOM);
        entity.setStatus(BizDataStatusConstants.ENABLE);
        return entity;
    }

}
