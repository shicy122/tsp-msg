package com.hycan.idn.tsp.message.pojo.avnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author:jianglin.hu
 * @createDate: 2023/1/4
 * @description: 描述
 */
@Data
public class VehicleDataDTO implements Serializable {

    /**
     * 车系
     */
    private String carSeries;

    /**
     * 协议
     */
    private String contract;

    /**
     * 车型
     */
    private String carModelMarket;

    public VehicleDataDTO(Map<String, String> carData) {
        this.carSeries = carData.get("carSeries");
        this.contract = carData.get("contract");
        this.carModelMarket = carData.get("carModelMarket");
    }
}
