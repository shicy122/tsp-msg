package com.hycan.idn.tsp.message.pojo.taskrelation;

import com.hycan.idn.tsp.message.pojo.scenecardtask.CreateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.UpdateSceneCardTaskReqVO;
import lombok.Data;

/**
 * 任务与资源关系DTO
 *
 * @author Shadow
 * @datetime 2024-03-13 11:53
 */
@Data
public class SaveTaskResourceDTO {
    private Long baseTaskId;
    private Long pictureId;
    private Long resourceId;
    private String customUrl;
    private String resourceType;

    public static SaveTaskResourceDTO of(Long baseTaskId, CreateSceneCardTaskReqVO reqVO) {
        SaveTaskResourceDTO dto = new SaveTaskResourceDTO();
        dto.setBaseTaskId(baseTaskId);
        dto.setResourceId(reqVO.getResourceId());
        dto.setPictureId(reqVO.getPictureId());
        dto.setCustomUrl(reqVO.getCustomUrl());
        dto.setResourceType(reqVO.getResourceType());
        return dto;
    }

    public static SaveTaskResourceDTO of(Long baseTaskId, UpdateSceneCardTaskReqVO reqVO) {
        SaveTaskResourceDTO dto = new SaveTaskResourceDTO();
        dto.setBaseTaskId(baseTaskId);
        dto.setResourceId(reqVO.getResourceId());
        dto.setPictureId(reqVO.getPictureId());
        dto.setCustomUrl(reqVO.getCustomUrl());
        dto.setResourceType(reqVO.getResourceType());
        return dto;
    }
}
