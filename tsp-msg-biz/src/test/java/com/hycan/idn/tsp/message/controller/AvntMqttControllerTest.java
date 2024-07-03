//package com.hycan.idn.tsp.message.controller;
//
//import cn.hutool.json.JSONUtil;
//import com.hycan.idn.tsp.common.core.config.PublicKeyProperties;
//import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
//import com.hycan.idn.tsp.common.core.util.R;
//import com.hycan.idn.tsp.message.constant.CommonConstants;
//import com.hycan.idn.tsp.message.constant.ProtocolConstants;
//import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity;
//import com.hycan.idn.tsp.message.enums.ResultCodeTypeEnum;
//import com.hycan.idn.tsp.message.repository.mongo.MsgPolicyConfigRepository;
//import com.hycan.idn.tsp.message.utils.JwtTokenUtil;
//import com.hycan.idn.tsp.vms.feign.VehInfoService;
//import com.hycan.idn.tsp.vms.feign.VehPartsInfoService;
//import com.hycan.idn.tsp.vms.params.inner.bo.veh.VehPartsDetailsBO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.UUID;
///**
// * 获取MQTT列表接口模块测试用例
// */
//@ActiveProfiles("unit")
//@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@Slf4j
//class AvntMqttControllerTest {
//
//    private static final String LIST_TOPICS_URL = "/tsp-api/msg-center-svc/v1/mqtt-topics";
//
//    private static final String ENCOD = "UTF-8";
//
//    private static final String SERIAL_VAL = "G06";
//
//    private static final String SOURCE_VAL = "avnt";
//
//    private static final String TOKEN_KEY = "token";
//    private static final String TOKEN_VAL = "TOKEN123455";
//    private static final String CLIENT_ID_KEY = "clientId";
//    private static final String CLIENT_ID_VAL = "avnt-desaysv";
//
//    private static final int DEFAULT_MAP_SIZE = 5;
//
//    private static final int QOS = 2;
//
//    private static final String SECRET_VAL = "SECRET1234";
//
//
//    @MockBean
//    private PublicKeyProperties publicKeyProperties;
//
//    @Autowired
//    private MsgPolicyConfigRepository policyConfigRepository;
//
//    @MockBean
//    private VehPartsInfoService vehPartsInfoService;
//
//    @Resource
//    private MqttUserMapper mqttUserMapper;
//
//
//    @MockBean
//    private VehInfoService vehInfoService;
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    @BeforeEach
//    void setup() {
//
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口成功，携带所有必填参数
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口成功
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 4. mqttuser表数据、消息发送策略配置表、mqtt配置表
//     * 5. Mock调用查询serial接口成功
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用成功，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_withAllParams_success() throws Exception {
//        String vin = UUID.randomUUID().toString();
//        //预置数据库等配置
//        List<MsgForwardConfig> msgForwardConfigs = buildTspMsgMqttConfigTable();
//        String policyConfigId = buildTspMsgPolicyConfigTable(msgForwardConfigs);
//        buildMqttUserTable(vin);
//        //预置第三方接口配置
//        Mockito.when(publicKeyProperties.getDesaiPublicKey()).thenReturn(SECRET_VAL);
//        Mockito.when(vehPartsInfoService.getByVinPartType(vin, ProtocolConstants.AVNT_VALUE, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehPartsDetailsBO());
//        Mockito.when(vehInfoService.getSeriesByVin(vin, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehInfoByVinSuccess());
//
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeToken(vin, SERIAL_VAL));
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//            assertTrue(topicEquals(msgForwardConfigs, r.getData().toString(), vin, SERIAL_VAL), r.toString());
//        } finally {
//            //删除数据库数据
//            deleteData(policyConfigId, vin);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口成功，携带所有必填参数
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口成功
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 4. Mock调用查询serial接口成功
//     * 5. 消息发送策略配置表、mqtt配置表
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用成功，mqttuser表找不到对应则新增，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_withAllParams_lackMqttUsetTable_success() throws Exception {
//        String vin = UUID.randomUUID().toString();
//        //预置数据库等配置
//        List<MsgForwardConfig> msgForwardConfigs = buildTspMsgMqttConfigTable();
//        String policyConfigId = buildTspMsgPolicyConfigTable(msgForwardConfigs);
//        //预置第三方接口配置
//        Mockito.when(publicKeyProperties.getDesaiPublicKey()).thenReturn(SECRET_VAL);
//        Mockito.when(vehPartsInfoService.getByVinPartType(vin, ProtocolConstants.AVNT_VALUE, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehPartsDetailsBO());
//        Mockito.when(vehInfoService.getSeriesByVin(vin, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehInfoByVinSuccess());
//
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeToken(vin, SERIAL_VAL));
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//            MqttUser mqttUserByUsername = mqttUserMapper
//                    .getMqttUserByUserName(SOURCE_VAL + CommonConstants.UNDERLINE + vin);
//            assertTrue(Objects.nonNull(mqttUserByUsername), "新增mqttuser表失败");
//            assertTrue(topicEquals(msgForwardConfigs, r.getData().toString(), vin, SERIAL_VAL), r.toString());
//        } finally {
//            //        删除数据库数据
//            deleteData(policyConfigId, vin);
//        }
//
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口失败，携带所有必填参数
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口成功
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 4. Mock调用查询serial接口成功
//     * 5. mqttuser表数据
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用失败，配置表根据vin号找不到对应车系展示为vin号不存在，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_withAllParams_lackPolicyTable_fail() throws Exception {
//        String vin = UUID.randomUUID().toString();
//        //预置数据库等配置
//        buildMqttUserTable(vin);
//        //预置第三方接口配置
//        Mockito.when(publicKeyProperties.getDesaiPublicKey()).thenReturn(SECRET_VAL);
//        Mockito.when(vehPartsInfoService.getByVinPartType(vin, ProtocolConstants.AVNT_VALUE, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehPartsDetailsBO());
//        Mockito.when(vehInfoService.getSeriesByVin(vin, SecurityConstants.FROM_IN)).thenReturn(buildVehInfoByVinFail());
//
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeToken(vin, SERIAL_VAL));
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//            assertEquals(ResultCodeTypeEnum.VIN_FAIL.getCode(), r.getCode(), r.toString());
//        } finally {
//            //        删除数据库数据
//            deleteDataByname(vin);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口失败，携带所有必填参数
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口成功
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 4. mqttuser表数据
//     * 5. Mock调用查询serial失败
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用失败，调用查询serial失败，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_withAllParams_getSerial_fail() throws Exception {
//        String vin = UUID.randomUUID().toString();
//        //预置数据库等配置
//        buildMqttUserTable(vin);
//        //预置第三方接口配置
//        Mockito.when(publicKeyProperties.getDesaiPublicKey()).thenReturn(SECRET_VAL);
//        Mockito.when(vehPartsInfoService.getByVinPartType(vin, ProtocolConstants.AVNT_VALUE, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehPartsDetailsBO());
//        Mockito.when(vehInfoService.getSeriesByVin(vin, SecurityConstants.FROM_IN)).thenReturn(R.ok());
//
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeToken(vin, SERIAL_VAL));
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//
//            assertEquals(ResultCodeTypeEnum.VIN_FAIL.getCode(), r.getCode());
//        } finally {
//            //        删除数据库数据
//            deleteDataByname(vin);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口失败，携带所有必填参数
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口失败
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 4. mqttuser表数据
//     * 5. Mock调用查询serial
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用失败，调用查询sn失败，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_withAllParams_getSN_fail() throws Exception {
//        String vin = UUID.randomUUID().toString();
//        //预置数据库等配置
//        buildMqttUserTable(vin);
//        //预置第三方接口配置
//        Mockito.when(publicKeyProperties.getDesaiPublicKey()).thenReturn(SECRET_VAL);
//        Mockito.when(vehPartsInfoService.getByVinPartType(vin, ProtocolConstants.AVNT_VALUE, SecurityConstants.FROM_IN))
//                .thenReturn(R.ok());
//        Mockito.when(vehInfoService.getSeriesByVin(vin, SecurityConstants.FROM_IN))
//                .thenReturn(buildVehInfoByVinSuccess());
//
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeToken(vin, SERIAL_VAL));
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//            assertEquals(ResultCodeTypeEnum.VIN_FAIL.getCode(), r.getCode());
//        } finally {
//            //        删除数据库数据
//            deleteDataByname(vin);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口失败，缺少必填参数
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口成功
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用失败，缺少必填参数，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_lackParams_fail() throws Exception {
//        //预置第三方接口配置
//        Mockito.when(publicKeyProperties.getDesaiPublicKey()).thenReturn(SECRET_VAL);
//
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeTokenFail());
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//            assertEquals(ResultCodeTypeEnum.PARAM_NON_NULL.getCode(), r.getCode(), r.toString());
//        }
//
//
//    }
//
//    /**
//     * 用例描述：
//     * 测试获取MQTT列表接口失败，Authentication不合法
//     * 预置条件：
//     * 1. Mock获取公钥成功
//     * 2. Mock调用查询车辆信息(sn号)接口成功
//     * 3. Mock解密Token成功，解密Token得到VIN、日期、来源等字段
//     * 测试步骤：
//     * 1. 调用获取MQTT列表接口
//     * 2. 验证结果是否与预置数据相同
//     * 预期结果：
//     * 接口调用失败，Authentication不合法，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_withAllParams_token_fail() throws Exception {
//        String vin = UUID.randomUUID().toString();
//        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
//            mockedStatic.when(() -> JwtTokenUtil.decodeToken(Mockito.anyString(), Mockito.anyString()))
//                    .thenReturn(buildDecodeToken(vin, SERIAL_VAL));
//            MvcResult result = mockMvc.perform(
//                    MockMvcRequestBuilders.get(LIST_TOPICS_URL)
//                            .param(TOKEN_KEY, TOKEN_VAL)
//                            .param(CLIENT_ID_KEY, CLIENT_ID_VAL)
//                            .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//            result.getResponse().setCharacterEncoding(ENCOD);
//            R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//            assertEquals(ResultCodeTypeEnum.TOKEN_FAIL.getCode(), r.getCode(), r.toString());
//        }
//    }
//
//    private Map<String, Object> buildDecodeTokenFail() {
//        Map<String, Object> map = new HashMap<>(DEFAULT_MAP_SIZE);
//        map.put("vin", UUID.randomUUID().toString());
//        map.put("date", LocalDateTime.now(ZoneId.of(DateTimeConstants.ZONEID)).toString());
//        map.put("source", SOURCE_VAL);
//        map.put("sn", "SN123456");
//        return map;
//    }
//
//    private Map<String, Object> buildDecodeToken(String vin, String serial) {
//        Map<String, Object> map = new HashMap<>(DEFAULT_MAP_SIZE);
//        map.put("vin", vin);
//        map.put("date", LocalDateTime.now(ZoneId.of(DateTimeConstants.ZONEID)).toString());
//        map.put("source", SOURCE_VAL);
//        map.put("sn", "SN123456");
//        map.put("iss", serial);
//        return map;
//    }
//
//    private R<VehPartsDetailsBO> buildVehPartsDetailsBO() {
//        VehPartsDetailsBO vehPartsDetailsBO = new VehPartsDetailsBO();
//        vehPartsDetailsBO.setSn("SN123456");
//        return R.ok(vehPartsDetailsBO);
//    }
//
//    private R<String> buildVehInfoByVinSuccess() {
//        R<String> ok = R.ok(SERIAL_VAL);
//        ok.getData();
//        return R.ok(SERIAL_VAL);
//    }
//    private R<String> buildVehInfoByVinFail() {
//        return R.failed(SERIAL_VAL);
//    }
//
//    /**
//     * 消息发送策略配置表
//     *
//     * @param forwardConfigs
//     */
//    private String buildTspMsgPolicyConfigTable(List<MsgForwardConfig> forwardConfigs) {
//        MsgPolicyConfigEntity msgPolicyConfigEntity = new MsgPolicyConfigEntity();
//        msgPolicyConfigEntity.setBussType("SEND_POI");
//        msgPolicyConfigEntity.setSerials(Arrays.asList("G06", "G06-A"));
//        msgPolicyConfigEntity.setForwardConfigs(forwardConfigs);
//        MsgPolicyConfigEntity msgPolicyConfig = policyConfigRepository.save(msgPolicyConfigEntity);
//        return msgPolicyConfig.getId();
//    }
//
//    /**
//     * 转发目标配置 MQTT
//     *
//     * @return
//     */
//    private List<MsgForwardConfig> buildTspMsgMqttConfigTable() {
//        MsgForwardConfig down = new MsgForwardConfig();
//        down.setProtocol(ProtocolConstants.MQTT);
//        down.setTopic("{serial}/{vin}/avnt/send_poi_down");
//        down.setQos(QOS);
//        down.setDirection("down");
//        down.setTarget("AVNT");
//
//        MsgForwardConfig up = new MsgForwardConfig();
//        up.setProtocol(ProtocolConstants.MQTT);
//        up.setTopic("{serial}/{vin}/avnt/send_poi_up");
//        up.setQos(QOS);
//        up.setDirection("up");
//        up.setTarget("AVNT");
//
//        List<MsgForwardConfig> idList = new ArrayList<>();
//        idList.add(down);
//        idList.add(up);
//        return idList;
//    }
//
//    /**
//     * mqttUser表
//     */
//    private void buildMqttUserTable(String vin) {
//        MqttUser mqttUser = new MqttUser();
//        mqttUser.setUsername(SOURCE_VAL + CommonConstants.UNDERLINE + vin);
//        mqttUser.setPassword("abc");
//        mqttUserMapper.insertMqttUser(mqttUser);
//
//        mqttUserMapper.getMqttUserByUserName(SOURCE_VAL + CommonConstants.UNDERLINE + vin);
//    }
//
//    /**
//     * 删除消息发送策略配置表、mqtt配置表、mqtt_user表指定数据
//     *
//     * @param policyConfigId
//     * @param vin
//     */
//    private void deleteData(String policyConfigId, String vin) {
//        mqttUserMapper.deleteMqttUserByUsername(SOURCE_VAL + CommonConstants.UNDERLINE + vin);
//        policyConfigRepository.deleteById(policyConfigId);
//    }
//
//    /**
//     * 删除mqtt_user表指定数据
//     *
//     * @param vin
//     */
//    private void deleteDataByname(String vin) {
//        mqttUserMapper.deleteMqttUserByUsername(SOURCE_VAL + CommonConstants.UNDERLINE + vin);
//    }
//
//    /**
//     * 判断topic是否一致
//     *
//     * @param forwardConfigs
//     * @param data
//     * @param vin
//     * @param serial
//     * @return
//     */
//    private boolean topicEquals(List<MsgForwardConfig> forwardConfigs, String data, String vin, String serial) {
//        //根据ids去数据库查询相关配置并进行拼接对比
//        for (MsgForwardConfig forwardConfig : forwardConfigs) {
//            String topic = forwardConfig.getTopic()
//                    .replace(CommonConstants.SERIAL_PLACEHOLDER, serial)
//                    .replace(CommonConstants.VIN_PLACEHOLDER, vin);
//            if (!data.contains(topic)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @AfterEach
//    void teardown() {
//    }
//}
