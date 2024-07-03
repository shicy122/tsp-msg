package com.hycan.idn.tsp.message.job;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 修改时间过期的数据状态单元测试
 *
 * @author Shadow
 * @datetime 2024-04-23 14:33
 */
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class StatusExpireJobHandlerTest {

    @Resource
    private StatusExpireJobHandler statusExpireJobHandler;

    @Test
    void holidayConfigStatusJobHandler() {
        statusExpireJobHandler.holidayConfigStatusJobHandler();
    }

    @Test
    void baseTaskStatusJobHandler() {
        statusExpireJobHandler.baseTaskStatusJobHandler();
    }

    @Test
    void taskDetailStatusJobHandler() {
        statusExpireJobHandler.taskDetailStatusJobHandler();
    }
}