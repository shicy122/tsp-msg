package com.hycan.idn.tsp.message.utils;

import com.hycan.idn.common.core.util.JsonUtil;
import com.hycan.idn.tsp.message.cache.PayloadSecretCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AESUtilTest {

    @Resource
    private PayloadSecretCache payloadSecretCache;

    @Test
    public void testPwd() {
        final String canonical = "LMWU11S87P1S00108_+E0WU000161P6Y0003000_20240418";
        String secretKey = SHA256Util.signWithHmacSha256("20240418", canonical);
        System.out.println(secretKey);
    }

    @Test
    public void testMsg() throws Exception {
        final String msg = "iQyuajvTo0BNor6N1dwJfS/xZYAukFHjeBa/mEYKwz82QAAzJWHQQGiKTIEkdGkOg6Wons/9yHxmDlwy2uJeaISEwu9t0dHDzhdWrq1G2COcaIqZs9+35mE2Ctv8Aif0lJgm5YLsHcc+vpmeFgmJWHAWTKWBKWV5d5xbmbqYdfTX6JF2Yz7a/DouVHoOAOC4iZkypGwVfHWvhTpVmYDxr0GNVnha78M0gSjEdc7ZYq2S9L7EiWUBYLP+isZBPfn7fJnhAPNKCiTPd10fK2E5VAOAI0wD2dWeGgmmS9cI9JWczgGzONMUG7NNsZ8DI8jhh3POJswjx9xES6Nb2GZ1vb/mOX6gz3oIEQaCEX/Zj0Fo2+LF8805KDC/rf4v9UADxrYKc/itmci4GP2Z0Vme5wLETWJdit8fqnJEoa/zL2AIbJAXx+yK19ax8AYNBC/GoulESEiRHGvXJexyTCTkqOikzBcUG7qBLkyOtOwmeMn4EKo6+I7O9IvsxzVcLeVj";

        String mqttSecretKey = payloadSecretCache.getPayloadSecret("LMWU11S8XP1S00118");
        String data = AESUtil.decrypt(mqttSecretKey, msg);
        log.info("解密DATA={}", data);
    }
}