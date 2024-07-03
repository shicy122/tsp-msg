package com.hycan.idn.tsp.message.pojo.taskrelation;

import com.hycan.idn.tsp.message.pojo.resourceconfig.ResourceConfigDTO;
import lombok.Data;

import java.util.List;
import java.util.Locale;

/**
 * 任务与资源关系DTO
 *
 * @author Shadow
 * @datetime 2024-03-13 11:53
 */
@Data
public class QueryTaskResourceDTO {

    private Long pictureId;

    private String pictureUrl;

    private Long resourceId;

    private String resourceType;

    private String resourceUrl;

    public static QueryTaskResourceDTO of(List<ResourceConfigDTO> resourceConfigList) {
        QueryTaskResourceDTO relationDTO = new QueryTaskResourceDTO();
        for (ResourceConfigDTO dto : resourceConfigList) {
            if (dto.isIllustration()) {
                relationDTO.setPictureId(dto.getResourceId());
                relationDTO.setPictureUrl(dto.getResourceUrl());
            } else {
                relationDTO.setResourceId(dto.getResourceId());
                relationDTO.setResourceUrl(dto.getResourceUrl());
                relationDTO.setResourceType(dto.getResourceType());
            }
        }
        return relationDTO;
    }
}
