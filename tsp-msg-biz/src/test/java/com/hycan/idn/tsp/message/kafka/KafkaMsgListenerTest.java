//package com.hycan.idn.tsp.message.kafka;
//
//import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
//import com.hycan.idn.tsp.common.core.util.GSON;
//import com.hycan.idn.tsp.common.core.util.R;
//import com.hycan.idn.tsp.message.constant.BussTypeConstants;
//import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
//import com.hycan.idn.tsp.message.constant.ProtocolConstants;
//import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity;
//import com.hycan.idn.tsp.message.entity.mongo.MsgSendRecordEntity;
//import com.hycan.idn.tsp.message.event.SendMsgEvent;
//import com.hycan.idn.tsp.message.event.listener.SendMsg2MqttListener;
//import com.hycan.idn.tsp.message.event.listener.SendMsg2RabbitListener;
//import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
//import com.hycan.idn.tsp.message.pojo.avntmqtt.ReceiveMsgDTO;
//import com.hycan.idn.tsp.message.repository.mongo.MsgPolicyConfigRepository;
//import com.hycan.idn.tsp.message.repository.mongo.MsgSendRecordRepository;
//import com.hycan.idn.tsp.vms.feign.VehInfoService;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.test.context.ActiveProfiles;
//
//import javax.annotation.Resource;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@ActiveProfiles("unit")
//@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class KafkaMsgListenerTest {
//
//    private static final String SERIAL_VAL = "G06";
//
//    private static final long DELAY_TIME = 30;
//
//    private static final long INTERVAL_TIME = 30;
//
//    private static final int RETRY_TIMES = 3;
//    private static final int QOS = 2;
//
//    @Autowired
//    private KafkaMsgListener kafkaMsgListener;
//
//    @Autowired
//    private MsgSendRecordRepository contentRepository;
//
//    @Autowired
////    private TspMsgSendStateRepository tspMsgSendStateRepository;
//
//    @Autowired
//    private SendMsg2RabbitListener rabbitmqListener;
//
//    @Autowired
//    private SendMsg2MqttListener mqttListener;
//
//    @Autowired
//    private MsgSendRecordRepository msgContentRepository;
//
//
//    @Autowired
//    private MsgPolicyConfigRepository policyConfigRepository;
//
//    @MockBean
//    private VehInfoService vehInfoService;
//
//    @Resource
//    private MongoTemplate mongoTemplate;
//
//
//    /**
//     * 用例描述：
//     * 测试消息推送mqtt成功，携带所有必填参数
//     * 预置条件：
//     * 1.  Mock调用查询serial接口成功
//     * 2. 消息发送策略配置表、mqtt配置表
//     * 测试步骤：
//     * 1. 调用kafka消费端
//     * 2. 验证消息是否发送成功
//     * 预期结果：
//     * 消息推送成功，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_consumeData_mqtt_success() {
//        //预置消息数据
//        String msgId = "test" + UUID.randomUUID();
//        SendMsgDTO sendMsgDTO = buildSendMsgDTO(msgId, BussTypeConstants.SEND_POI);
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<String, String>(
//                "", 1, 1L, "", GSON.toJSONString(sendMsgDTO));
//        //预置数据库等配置
//        List<MsgForwardConfig> msgForwardConfigs = buildTspMsgMqttConfigTable();
//        TspMsgRetryConfig tspMsgRetryConfig = buildTspMsgRetryConfigTable();
//        String policyConfigId = buildTspMsgPolicyConfigTable(msgForwardConfigs, tspMsgRetryConfig);
//        //预置第三方接口配置
//        Mockito.when(vehInfoService.getSeriesByVin("testvin" + msgId, SecurityConstants.FROM_IN))
//                .thenReturn(buildvehInfoByVin());
//        try {
//            kafkaMsgListener.onMessage(Arrays.asList(consumerRecord));
//            for (MsgForwardConfig msgForwardConfig : msgForwardConfigs) {
//                mqttListener.handling(new SendMsgEvent(
//                        buildReceiveMsgDTO(sendMsgDTO, tspMsgRetryConfig, SERIAL_VAL, msgForwardConfig)));
//            }
//            assertTrue(Objects.nonNull(contentRepository.findByMsgId(msgId)), "新增消息内容表失败");
//            assertTrue(sendStateEquals(msgForwardConfigs,
//                    tspMsgSendStateRepository.findByMsgId(msgId), ProtocolConstants.MQTT), "新增消息状态表失败");
//        } finally {
//            //删除数据库数据
//            deletePolicyData(policyConfigId);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试消息推送rabbitmq成功，携带所有必填参数
//     * 预置条件：
//     * 1.  Mock调用查询serial接口成功
//     * 2. 消息发送策略配置表、rabbitmq配置表
//     * 测试步骤：
//     * 1. 调用kafka消费端
//     * 2. 验证消息是否发送成功
//     * 预期结果：
//     * 消息推送成功，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_consumeData_rabbitmq_success() {
//        //预置消息数据
//        String msgId = "test" + UUID.randomUUID();
//        SendMsgDTO sendMsgDTO = buildSendMsgDTO(msgId, BussTypeConstants.SEND_POI);
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<String, String>(
//                "", 1, 1L, "", GSON.toJSONString(sendMsgDTO));
//        //预置数据库等配置
//        List<MsgForwardConfig> msgForwardConfigs = buildTspMsgRabbitMQConfigTable();
//        TspMsgRetryConfig tspMsgRetryConfig = buildTspMsgRetryConfigTable();
//        String policyConfigId = buildTspMsgPolicyConfigTable(msgForwardConfigs, tspMsgRetryConfig);
//        //预置第三方接口配置
//        Mockito.when(vehInfoService.getSeriesByVin("testvin" + msgId, SecurityConstants.FROM_IN))
//                .thenReturn(buildvehInfoByVin());
//        try {
//            kafkaMsgListener.onMessage(Arrays.asList(consumerRecord));
//            for (MsgForwardConfig msgForwardConfig : msgForwardConfigs) {
//                rabbitmqListener.handling(new SendMsgEvent(
//                        buildReceiveMsgDTO(sendMsgDTO, tspMsgRetryConfig, SERIAL_VAL, msgForwardConfig)));
//            }
//            assertTrue(Objects.nonNull(contentRepository.findByMsgId(msgId)), "新增消息内容表失败");
//            assertTrue(sendStateEquals(msgForwardConfigs,
//                    tspMsgSendStateRepository.findByMsgId(msgId), ProtocolConstants.RABBITMQ),
//                    "新增消息状态表失败");
//        } finally {
//            //删除数据库数据
//            deletePolicyData(policyConfigId);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试消息推送失败，携带所有必填参数
//     * 预置条件：
//     * 1.  Mock调用查询serial接口失败
//     * 测试步骤：
//     * 1. 调用kafka消费端
//     * 2. 验证消息是否发送成功
//     * 预期结果：
//     * 消息推送失败，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_consumeData_withAllParams_getSerial_fail() {
//        //预置消息数据
//        String msgId = "test" + UUID.randomUUID();
//        SendMsgDTO sendMsgDTO = buildSendMsgDTO(msgId, BussTypeConstants.SEND_POI);
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<String, String>(
//                "", 1, 1L, "", GSON.toJSONString(sendMsgDTO));
//        //预置第三方接口配置
//        Mockito.when(vehInfoService.getSeriesByVin("testvin" + msgId, SecurityConstants.FROM_IN)).thenReturn(R.ok());
//        kafkaMsgListener.onMessage(Arrays.asList(consumerRecord));
//        assertTrue(Objects.isNull(contentRepository.findByMsgId(msgId)), "校验serial失败");
//    }
//
//    /**
//     * 用例描述：
//     * 测试消息推送失败，携带所有必填参数
//     * 预置条件：
//     * 1.  预置消息内容表，消息id重复，推送失败
//     * 测试步骤：
//     * 1. 调用kafka消费端
//     * 2. 验证消息是否发送成功
//     * 预期结果：
//     * 消息推送失败，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_consumeData_withAllParams_MsgID_fail() {
//        //预置消息数据
//        String msgId = "test" + UUID.randomUUID();
//        SendMsgDTO sendMsgDTO = buildSendMsgDTO(msgId, BussTypeConstants.SEND_POI);
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<String, String>(
//                "", 1, 1L, "", GSON.toJSONString(sendMsgDTO));
//        //预置数据库等配置
//        MsgSendRecordEntity msgContent = new MsgSendRecordEntity();
//        msgContent.setMsgId(msgId);
//        msgContentRepository.save(msgContent);
//        try {
//            kafkaMsgListener.onMessage(Arrays.asList(consumerRecord));
//            assertTrue(contentRepository.findByMsgIds(msgId).size() <= 1, "消息去重失败");
//        } finally {
//            //删除数据库数据
//            deleteMsgData(msgId);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试消息推送失败，携带所有必填参数
//     * 预置条件：
//     * 1. 预置配置表内容 查询配置表失败（查询不到相关配置或业务类型不存在）
//     * 2. Mock调用查询serial接口成功
//     * 测试步骤：
//     * 1. 调用kafka消费端
//     * 2. 验证消息是否发送成功
//     * 预期结果：
//     * 消息推送失败，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_consumeData_withAllParams_lackPolicyTable_fail() {
//        //预置消息数据
//        String msgId = "test" + UUID.randomUUID();
//        SendMsgDTO sendMsgDTO = buildSendMsgDTO(msgId, BussTypeConstants.SEND_POI);
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<String, String>(
//                "", 1, 1L, "", GSON.toJSONString(sendMsgDTO));
//        //预置第三方接口数据
//        Mockito.when(vehInfoService.getSeriesByVin("testvin" + msgId, SecurityConstants.FROM_IN))
//                .thenReturn(buildvehInfoByVin());
//        try {
//            kafkaMsgListener.onMessage(Arrays.asList(consumerRecord));
//            assertTrue(contentRepository.findByMsgIds(msgId).size() == 0, "业务类型校验失败");
//        } finally {
//            //删除数据库数据
//            deleteMsgData(msgId);
//        }
//    }
//
//    /**
//     * 用例描述：
//     * 测试消息推送失败，缺少必填参数
//     * 预置条件：
//     * 测试步骤：
//     * 1. 调用kafka消费端
//     * 2. 验证消息是否发送成功
//     * 预期结果：
//     * 消息推送失败，响应结果符合预期
//     *
//     * @throws Exception
//     */
//    @Test
//    void test_getTopics_lackParams_fail() {
//        //预置消息数据
//        String msgId = "test" + UUID.randomUUID();
//        SendMsgDTO sendMsgDTO = buildSendMsgDTO(msgId, BussTypeConstants.SEND_POI);
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<String, String>(
//                "", 1, 1L, "", GSON.toJSONString(sendMsgDTO));
//        //预置第三方接口数据
//        Mockito.when(vehInfoService.getSeriesByVin("testvin" + msgId, SecurityConstants.FROM_IN))
//                .thenReturn(buildvehInfoByVin());
//        try {
//            kafkaMsgListener.onMessage(Arrays.asList(consumerRecord));
//            assertTrue(contentRepository.findByMsgIds(msgId).size() == 0, "校验参数失败");
//        } finally {
//            //删除数据库数据
//            deleteMsgData(msgId);
//        }
//    }
//
//
//    private SendMsgDTO buildSendMsgDTO(String msgId, String bussType) {
//        SendMsgDTO sendMsgDTO = new SendMsgDTO();
//        sendMsgDTO.setBussType(bussType);
//        sendMsgDTO.setMsgId(msgId);
//        sendMsgDTO.setVin("testvin" + msgId);
//        sendMsgDTO.setReportTime(System.currentTimeMillis());
//        sendMsgDTO.setPayload("abc");
//        return sendMsgDTO;
//
//    }
//
//    private SendMsgDTO buildSendMsgDTOFail(String msgId, String bussType) {
//        SendMsgDTO sendMsgDTO = new SendMsgDTO();
//        sendMsgDTO.setBussType(bussType);
//        sendMsgDTO.setMsgId(msgId);
//        sendMsgDTO.setReportTime(System.currentTimeMillis());
//        sendMsgDTO.setPayload("abc");
//        return sendMsgDTO;
//
//    }
//
//    private R<String> buildvehInfoByVin() {
//        R<String> ok = R.ok(SERIAL_VAL);
//        ok.getData();
//        return R.ok(SERIAL_VAL);
//    }
//
//
//    /**
//     * 判断消息内容表新增数据是否跟预期结果一致
//     *
//     * @param msgForwardConfigs
//     * @param byMsgId
//     * @return
//     */
//    private boolean sendStateEquals(List<MsgForwardConfig> msgForwardConfigs,
//                                    List<TspMsgState> byMsgId, String protocol) {
//        if (Objects.isNull(byMsgId)) {
//            return false;
//        }
//        if (msgForwardConfigs.size() != byMsgId.size()) {
//            return false;
//        }
//        for (TspMsgState sendState : byMsgId) {
//            if (sendState.getTimes() != 1 && sendState.getStatus().equals(MsgSendStatusConstant.FAILURE)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 消息发送策略配置表
//     *
//     * @param forwardConfigs
//     * @param tspMsgRetryConfig
//     */
//    private String buildTspMsgPolicyConfigTable(List<MsgForwardConfig> forwardConfigs,
//                                                TspMsgRetryConfig tspMsgRetryConfig) {
//        MsgPolicyConfigEntity msgPolicyConfigEntity = new MsgPolicyConfigEntity();
//        msgPolicyConfigEntity.setBussType("SEND_POI");
//        msgPolicyConfigEntity.setSerials(Arrays.asList("G06", "G06-A"));
//        msgPolicyConfigEntity.setForwardConfigs(forwardConfigs);
//        msgPolicyConfigEntity.setRetryConfig(tspMsgRetryConfig);
//        MsgPolicyConfigEntity msgPolicyConfig = policyConfigRepository.save(msgPolicyConfigEntity);
//        return msgPolicyConfig.getId();
//    }
//
//    /**
//     * 重试策略配置表
//     *
//     * @return
//     */
//    private TspMsgRetryConfig buildTspMsgRetryConfigTable() {
//        return new TspMsgRetryConfig(DELAY_TIME, INTERVAL_TIME, RETRY_TIMES);
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
//     * 转发目标配置 RabbitMQ
//     *
//     * @return
//     */
//    private List<MsgForwardConfig> buildTspMsgRabbitMQConfigTable() {
//        MsgForwardConfig down = new MsgForwardConfig();
//        down.setProtocol(ProtocolConstants.RABBITMQ);
//        down.setTarget("vcp");
//        down.setRoutingKey("vcp");
//
//        List<MsgForwardConfig> idList = new ArrayList<>();
//        idList.add(down);
//        return idList;
//    }
//
//
//    /**
//     * 发布事件DTO
//     *
//     * @param sendMsgDTO
//     * @param tspMsgRetryConfig
//     * @param serial
//     * @param forwardConfig
//     * @return
//     */
//    private ReceiveMsgDTO buildReceiveMsgDTO(SendMsgDTO sendMsgDTO,
//                                             TspMsgRetryConfig tspMsgRetryConfig,
//                                             String serial,
//                                             MsgForwardConfig forwardConfig) {
//        ReceiveMsgDTO receiveMsgDTO = new ReceiveMsgDTO();
//        BeanUtils.copyProperties(sendMsgDTO, receiveMsgDTO);
//        receiveMsgDTO.setProtocol(forwardConfig.getProtocol());
//        receiveMsgDTO.setForwardConfig(forwardConfig);
//        receiveMsgDTO.setSendTimes(1);
//        receiveMsgDTO.setSerial(serial);
//        return receiveMsgDTO;
//    }
//
//    /**
//     * 删除消息发送策略配置表
//     *
//     * @param policyConfigId
//     */
//    private void deletePolicyData(String policyConfigId) {
//        policyConfigRepository.deleteById(policyConfigId);
//    }
//
//    /**
//     * 根据消息id删除数据
//     *
//     * @param msgId
//     */
//    private void deleteMsgData(String msgId) {
//        Query query = Query.query(Criteria.where("msg_id").is(msgId));
//        mongoTemplate.remove(query, MsgSendRecordEntity.class);
//        mongoTemplate.remove(query, TspMsgState.class);
//    }
//
//}
