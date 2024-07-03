package com.hycan.idn.tsp.message.pojo.resourceconfig;

import lombok.Data;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-12 19:56
 */
@Data
public class ResourceConfigDTO {

    private Long baseTaskId;

    private Long resourceId;

    private String resourceUrl;

    private String resourceType;

    private boolean isIllustration;


}
