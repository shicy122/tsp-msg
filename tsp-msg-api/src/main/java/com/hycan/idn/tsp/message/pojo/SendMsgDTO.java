package com.hycan.idn.tsp.message.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * TSP平台业务服务发送消息到消息中心公共DTO
 *
 * @author  liangwenqi
 * @datetime 2022-06-09  15:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMsgDTO {

    /**
     * 车辆VIN号
     */
    @NotBlank(message = "vin不能为空")
    private String vin;

    /**
     * 用户ID(预留，业务场景使用不到，可以不传)
     */
    private String uid;

    /**
     * 消息唯一ID，统一使用UUID
     */
    @NotBlank(message = "消息ID不能为空")
    private String msgId;

    /**
     * 消息所属业务类型，使用BussTypeConstants中定义的类型
     */
    @NotBlank(message = "业务类型不能为空")
    private String bussType;

    /**
     * 消息上报时间，统一使用毫秒时间戳
     */
    @NotBlank(message = "上报时间")
    private Long reportTime;

    /**
     * 消息内容结构体，消息中心不对内容做校验
     */
    private Object payload;

    public static SendMsgDTO of(String vin, String bussType, Object payload) {
        SendMsgDTO msgDTO = new SendMsgDTO();
        msgDTO.setMsgId(UUID.randomUUID().toString());
        msgDTO.setVin(vin);
        msgDTO.setBussType(bussType);
        msgDTO.setPayload(payload);
        msgDTO.setReportTime(System.currentTimeMillis());
        return msgDTO;
    }

    public static SendMsgDTO of(String vin, String bussType, Object payload, Long reportTime) {
        SendMsgDTO msgDTO = new SendMsgDTO();
        msgDTO.setMsgId(UUID.randomUUID().toString());
        msgDTO.setVin(vin);
        msgDTO.setBussType(bussType);
        msgDTO.setPayload(payload);
        msgDTO.setReportTime(reportTime);
        return msgDTO;
    }
}
