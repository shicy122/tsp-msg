package com.hycan.idn.tsp.message.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehCarDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 车辆基础信息对象
 *
 * @author shichongying
 * @datetime 2024-02-23 15:14
 */
@Data
@TableName("vehicle_base_info")
public class VehicleBaseInfoEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** VIN */
    private String vin;

    /** 车系ID */
    private String serialId;

    /** 车型ID */
    private Long mtId;

    /** 产线 */
    private String carModelMarket;

    /** 车系 */
    private String serial;

    /** 车型 */
    private String mt;

    public static List<VehicleBaseInfoEntity> of(List<VehCarDTO> vehCarDTOList) {
        return vehCarDTOList.stream().map(VehicleBaseInfoEntity::of).collect(Collectors.toList());
    }

    public static VehicleBaseInfoEntity of(VehCarDTO vehCarDTO) {
        VehicleBaseInfoEntity entity = new VehicleBaseInfoEntity();
        entity.setVin(vehCarDTO.getVin());
        entity.setSerial(vehCarDTO.getSeriesName());
        entity.setSerialId(vehCarDTO.getSeriesId());
        entity.setMt(vehCarDTO.getModelName());
        entity.setMtId(vehCarDTO.getModelId());

        return entity;
    }
}
