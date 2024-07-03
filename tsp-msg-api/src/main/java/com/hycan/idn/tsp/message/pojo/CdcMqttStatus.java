package com.hycan.idn.tsp.message.pojo;

import lombok.Data;

/**
 * 车机MQTT状态
 *
 * @author Shadow
 * @datetime 2024-05-06 09:30
 */
@Data
public class CdcMqttStatus {

    public static final Integer OFFLINE = 0;
    public static final Integer ONLINE = 1;

    /**
     * 车辆VIN码
     */
    private String vin;

    /**
     * 0:离线 1:在线
     */
    private Integer status;

    /**
     * 时间戳
     */
    private Long timestamp;

    public static CdcMqttStatus of(String vin, Integer status) {
        CdcMqttStatus cdcMqttStatus = new CdcMqttStatus();
        cdcMqttStatus.setVin(vin);
        cdcMqttStatus.setStatus(status);
        cdcMqttStatus.setTimestamp(System.currentTimeMillis());
        return cdcMqttStatus;
    }
}
