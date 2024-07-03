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
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-04-23 14:32
 */
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class RetrySendMsgJobHandlerTest {

    @Resource
    private RetrySendMsgJobHandler retrySendMsgJobHandler;

    @Test
    void retrySendFailureMsgHandler() {
        retrySendMsgJobHandler.retrySendFailureMsgHandler();
    }

}