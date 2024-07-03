package com.hycan.idn.tsp.message.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import static com.hycan.idn.tsp.message.event.listener.ChangeStatusListener.AVNT_ONLINE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-05-13 14:39
 */
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ChangeStatusListenerTest {

   @Resource
   private ChangeStatusListener listener;

    @Test
    void sendMsg() {
        listener.sendMsg("LMWU21S51P1S00057", AVNT_ONLINE);
    }
}