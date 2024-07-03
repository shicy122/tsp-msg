package com.hycan.idn.tsp.message.pojo.basetask;

import lombok.Data;

import java.util.List;

/**
 * 车辆基础信息VO
 *
 * @author Shadow
 * @datetime 2024-03-08 17:45
 */
@Data
public class VehicleBaseInfoVO {

    /** 车系ID */
    private Long serialId;

    /** 车型ID */
    private List<Long> mtId;
}


