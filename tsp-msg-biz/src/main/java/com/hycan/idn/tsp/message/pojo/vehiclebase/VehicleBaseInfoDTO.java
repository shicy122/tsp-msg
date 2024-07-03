package com.hycan.idn.tsp.message.pojo.vehiclebase;

import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehCarDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆基础信息DTO
 *
 * @author Shadow
 * @datetime 2024-03-20 09:49
 */
@Data
public class VehicleBaseInfoDTO {

    /** VIN */
    private String vin;

    /** 车系ID */
    private String serialId;

    /** 车型ID */
    private Long mtId;

    /** 车系 */
    private String serial;

    /** 车型 */
    private String mt;

    public static List<VehicleBaseInfoDTO> of(List<VehCarDTO> vehCarDTOList) {
        List<VehicleBaseInfoDTO> vehicleBaseInfoDTOList = new ArrayList<>();
        for (VehCarDTO vehCarDTO : vehCarDTOList) {
            VehicleBaseInfoDTO vehicleBaseInfoDTO = new VehicleBaseInfoDTO();
            vehicleBaseInfoDTO.setVin(vehCarDTO.getVin());
            vehicleBaseInfoDTO.setSerialId(vehCarDTO.getSeriesId());
            vehicleBaseInfoDTO.setSerial(vehCarDTO.getSeriesName());
            vehicleBaseInfoDTO.setMtId(vehCarDTO.getModelId());
            vehicleBaseInfoDTO.setMt(vehCarDTO.getModelName());

            vehicleBaseInfoDTOList.add(vehicleBaseInfoDTO);
        }
        return vehicleBaseInfoDTOList;
    }
}
