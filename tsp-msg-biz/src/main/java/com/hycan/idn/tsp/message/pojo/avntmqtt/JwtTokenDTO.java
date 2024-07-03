package com.hycan.idn.tsp.message.pojo.avntmqtt;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 查询MQTT Topic列表请求VO
 *
 * @author liangwenqi
 * @datetime 2022-06-09 15:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {

    /**
     * 车辆VIN号
     */
    @ApiModelProperty(value = "vin", required = true, dataType = "java.lang.String")
    @NotBlank(message = "vin不能为空")
    private String vin;

    /**
     * 注意日期需要在客户端保管好，跨天时需要使用该日期生成秘钥
     */
    @ApiModelProperty(value = "生成秘钥时间", required = true, dataType = "java.time.LocalDateTime")
    @NotBlank(message = "时间不能为空")
    private String date;

    /**
     * 接入设备类型
     */
    @ApiModelProperty(value = "接入设备类型", required = true, dataType = "java.lang.Integer")
    @NotBlank(message = "接入设备类型")
    private String source;

    /**
     * 车系编号
     */
    @ApiModelProperty(value = "车系编号", required = true, dataType = "java.lang.Integer")
    @NotBlank(message = "车系编号")
    private String iss;

    /**
     * jwt加密时间
     */
    @ApiModelProperty(value = "jwt加密时间", required = true, dataType = "java.lang.Integer")
    @NotBlank(message = "jwt加密时间")
    private Long iat;

    /**
     * jwt过期时间
     */
    @ApiModelProperty(value = "jwt过期时间", required = true, dataType = "java.lang.Integer")
    @NotBlank(message = "jwt过期时间")
    private Long exp;

    /**
     * 车机sn
     */
    @ApiModelProperty(value = "车机sn", required = true, dataType = "java.lang.Integer")
    @NotBlank(message = "车机sn")
    private String sn;
}
