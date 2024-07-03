package com.hycan.idn.tsp.message.service;

import cn.hutool.core.util.StrUtil;
import com.hycan.idn.tsp.common.core.config.PublicKeyProperties;
import com.hycan.idn.tsp.message.cache.VehBaseInfoCache;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.constant.ProtocolConstants;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity;
import com.hycan.idn.tsp.message.enums.ResultCodeTypeEnum;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.facade.MqttxRemoteFacade;
import com.hycan.idn.tsp.message.pojo.MqttTopicsRspDTO;
import com.hycan.idn.tsp.message.pojo.MqttTopicsRspDTO.MqttTopicDTO;
import com.hycan.idn.tsp.message.pojo.avntmqtt.JwtTokenDTO;
import com.hycan.idn.tsp.message.repository.mongo.MsgPolicyConfigRepository;
import com.hycan.idn.tsp.message.utils.JwtTokenUtil;
import com.hycan.idn.tsp.message.utils.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hycan.idn.tsp.message.constant.CommonConstants.AVNT;
import static com.hycan.idn.tsp.message.constant.CommonConstants.UNDERLINE;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-22 15:49
 */
@Slf4j
@Service
public class AvntMqttService {

    /**
     * MQTT上行TOPIC
     */
    public static final String MQTT_TOPIC_UP = "up";

    /**
     * MQTT下行TOPIC
     */
    public static final String MQTT_TOPIC_DOWN = "down";

    /**
     * 客户端ID德赛
     */
    private static final String CLIENT_ID_DESAI = "avnt-desaysv";
    /**
     * 客户端ID远锋
     */
    private static final String CLIENT_ID_YUANFENG = "avnt-yuanfeng";

    @Resource
    private PublicKeyProperties publicKeyProperties;

    @Resource
    private VehBaseInfoCache vehBaseInfoCache;

    @Resource
    private MsgPolicyConfigRepository policyConfigRepository;

    @Resource
    private MqttxRemoteFacade mqttxRemoteFacade;

    /**
     * 获取MQTT Topic列表
     *
     * @param clientId 客户端ID
     * @param token    MQTT鉴权信息
     * @return MQTT Topic列表
     */
    public MqttTopicsRspDTO getMqttTopics(String clientId, String token) {
        String publicKey = getPublicKey(clientId);

        // 解码鉴权信息
        JwtTokenDTO jwtToken = JwtTokenUtil.decodeToken(token, publicKey, clientId);

        // 检验鉴权信息中的车系和SN码是否匹配
        validParams(jwtToken, clientId);

        // 保存MQTT鉴权信息
        saveMqttAuthInfo(jwtToken);

        // 返回MQTT Topic列表
        return getMqttTopicsRsp(jwtToken.getIss(), jwtToken.getVin());
    }

    /**
     * 检验鉴权信息中的车系和SN码是否匹配
     */
    private void validParams(JwtTokenDTO jwtToken, String clientId) {
                String vin = jwtToken.getVin();
        if (StringUtils.isBlank(vin)) {
            throw new MsgBusinessException(
                    StrUtil.format("客户端{}获取MQTT Topic列表: VIN校验失败, 入参VIN码为空!", clientId),
                    ResultCodeTypeEnum.PARAM_FAIL.getCode());
        }

        String currentDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        if (!Objects.equals(currentDate, jwtToken.getDate())) {
            throw new MsgBusinessException(
                    StrUtil.format("客户端{}获取MQTT Topic列表: 日期校验失败, VIN=[{}], 当前日期=[{}], 与入参(date)=[{}]不匹配",
                            clientId, vin, currentDate, jwtToken.getDate()),
                    ResultCodeTypeEnum.PARAM_FAIL.getCode());
        }

        String serial = vehBaseInfoCache.getSerialByVin(vin);
        String sn = vehBaseInfoCache.getCdcSnByVin(vin);

        if (StringUtils.isBlank(serial)) {
            throw new MsgBusinessException(
                    StrUtil.format("客户端{}获取MQTT Topic列表: 车系校验失败, VIN=[{}]无匹配的车系!", clientId, vin),
                    ResultCodeTypeEnum.PARAM_FAIL.getCode());
        }

//        if (!serial.equals(jwtToken.getIss())) {
//            throw new MsgBusinessException(
//                    StrUtil.format("客户端{}获取MQTT Topic列表: 车系校验失败, VIN=[{}]获取的车系=[{}], 与入参(iss)=[{}]不匹配",
//                            clientId, vin, serial, jwtToken.getIss()),
//                    ResultCodeTypeEnum.PARAM_FAIL.getCode());
//        }

        if (StringUtils.isBlank(sn)) {
            throw new MsgBusinessException(
                    StrUtil.format("客户端{}获取MQTT Topic列表: SN校验失败, VIN=[{}]无匹配的SN!", clientId, vin),
                    ResultCodeTypeEnum.PARAM_FAIL.getCode());
        }

        if (!sn.equals(jwtToken.getSn())) {
            throw new MsgBusinessException(
                    StrUtil.format("客户端{}获取MQTT Topic列表: SN校验失败, VIN=[{}]获取的SN=[{}], 与入参(sn)=[{}]不匹配",
                            clientId, vin, sn, jwtToken.getSn()),
                    ResultCodeTypeEnum.PARAM_FAIL.getCode());
        }
    }

    /**
     * 保存MQTT鉴权信息
     */
    private void saveMqttAuthInfo(JwtTokenDTO jwtToken) {
        String username = AVNT + jwtToken.getVin();
        String canonicalString = jwtToken.getVin() + UNDERLINE + jwtToken.getSn() + UNDERLINE + jwtToken.getDate();
        String password = SHA256Util.signWithHmacSha256(jwtToken.getDate(), canonicalString);
        boolean isSuccess = mqttxRemoteFacade.saveAuthInfo(username, password);
        if (!isSuccess) {
            throw new MsgBusinessException(
                    StrUtil.format("获取MQTT Topic列表接口: 保存MQTT鉴权信息异常!", jwtToken.getVin()),
                    ResultCodeTypeEnum.SERVER_FAIL.getCode());
        }
    }

    private MqttTopicsRspDTO getMqttTopicsRsp(String serial, String vin) {
        List<MsgPolicyConfigEntity> entities = policyConfigRepository.findBySerialsAndProtocol(serial, ProtocolConstants.MQTT);

        MqttTopicsRspDTO mqttTopicsRspDTO = new MqttTopicsRspDTO();

        List<MqttTopicDTO> mqttPubTopicsDTOList = new ArrayList<>();
        List<MqttTopicDTO> mqttSubTopicsDTOList = new ArrayList<>();

        entities.forEach(msgPolicy -> msgPolicy.getForwardConfigs().stream()
                .distinct()
                .filter(forward -> ProtocolConstants.MQTT.equals(forward.getProtocol()))
                .forEach(forward -> {
                    MqttTopicDTO mqttTopicDTO = buildMqttTopicDTO(
                            serial, vin, msgPolicy.getBussType(), forward.getTopic(), forward.getQos());
                    if (forward.getDirection().equals(MQTT_TOPIC_UP)) {
                        mqttPubTopicsDTOList.add(mqttTopicDTO);
                    } else if (forward.getDirection().equals(MQTT_TOPIC_DOWN)) {
                        mqttSubTopicsDTOList.add(mqttTopicDTO);
                    }
                })
        );

        mqttTopicsRspDTO.setPubTopics(mqttPubTopicsDTOList);
        mqttTopicsRspDTO.setSubTopics(mqttSubTopicsDTOList);
        return mqttTopicsRspDTO;
    }

    private MqttTopicDTO buildMqttTopicDTO(String serial, String vin, String bussType, String topic, Integer qos) {
        MqttTopicDTO mqttTopicDTO = new MqttTopicDTO();
        mqttTopicDTO.setBussType(bussType);
        mqttTopicDTO.setTopic(topic
                .replace(CommonConstants.SERIAL_PLACEHOLDER, serial)
                .replace(CommonConstants.VIN_PLACEHOLDER, vin));
        mqttTopicDTO.setQos(qos);
       return mqttTopicDTO;
    }

    /**
     * 根据HEADER中携带的clientId，获取授权公钥
     *
     * @param clientId 平台分配的clientId，德赛/远峰
     * @return 公钥
     */
    private String getPublicKey(String clientId) {
        if (CLIENT_ID_YUANFENG.equals(clientId)) {
            return publicKeyProperties.getPublicKey();
        } else if (CLIENT_ID_DESAI.equals(clientId)) {
            return publicKeyProperties.getDesaiPublicKey();
        } else {
            throw new MsgBusinessException("Header中携带的授权Client-ID非法!");
        }
    }
}
