package com.hycan.idn.tsp.message.mongock;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity;
import com.hycan.idn.tsp.message.repository.mongo.MsgPolicyConfigRepository;
import io.mongock.api.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 集成Mongock，对MongoDB数据库做版本控制
 * 使用时要注意，各服务配置的id不能相同，建议以tsp+服务名作为前缀，order是执行顺序
 *
 * @author shichongying
 */
@Slf4j
@ChangeUnit(id = "init-msg-policy-config", order = "010")
public class PolicyConfigChangeUnit {

    private static final String FILE_PATH = "db/migration/mongo/init_msg_policy_config.json";

    /**
     * 执行前，需要准备的动作
     * 如不涉及，也需要建一个空方法
     */
    @BeforeExecution
    public void beforeExecution() {
    }

    /**
     * 执行前，需要回滚的动作
     * 如不涉及，也需要建一个空方法
     */
    @RollbackBeforeExecution
    public void rollbackBeforeExecution() {
    }

    /**
     * 需要执行的动作
     * 如不涉及，也需要建一个空方法
     */
    @Execution
    public void execution(final MongoTemplate mongoTemplate) throws IOException {
        ClassPathResource resource = new ClassPathResource(FILE_PATH);
        String jsonStr = IoUtil.read(resource.getInputStream(), StandardCharsets.UTF_8);

        List<MsgPolicyConfigEntity> configs = JSON.parseArray(jsonStr, MsgPolicyConfigEntity.class);
        mongoTemplate.insertAll(configs);
    }

    /**
     * 执行后，需要回滚的动作
     * 如不涉及，也需要建一个空方法
     */
    @RollbackExecution
    public void RollbackExecution() {
    }
}
