package com.hycan.idn.tsp.message.facade;

import com.hycan.idn.common.core.util.JsonUtil;
import com.hycan.idn.tsp.message.config.MqttConfig;
import com.hycan.idn.tsp.message.pojo.OkHttpRspDTO;
import com.hycan.idn.tsp.message.pojo.avntmqtt.UserInfoReqVO;
import com.hycan.idn.tsp.message.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author shichongying
 * @datetime 2023年 03月 04日 11:13
 */
@Slf4j
@Service
public class MqttxRemoteFacade {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String PWD_URI = "/api/v1/mqtt/encrypt/%s";

    private static final String SAVE_URI = "/api/v1/mqtt/user";

    @Resource
    private OkHttpUtil okHttpUtil;

    private final String endpoint;

    public MqttxRemoteFacade(MqttConfig mqttConfig) {
        this.endpoint = mqttConfig.getMqttxEndpoint();
    }

    /**
     * 调用MQTTX接口获取密码，直到获取成功
     *
     * @return 密码
     */
    public String searchEncryptPwd(String clientId) {
        while (true) {
            try {
                String uri = String.format(PWD_URI, clientId);
                Request request = new Request.Builder()
                        .get()
                        .url(endpoint + uri)
                        .build();
                OkHttpRspDTO response = okHttpUtil.request(request);
                if (Objects.nonNull(response) && response.isSuccess() && StringUtils.isNotEmpty(response.getBody())) {
                    return response.getBody();
                }
            } catch (Exception e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 保存鉴权信息
     */
    public boolean saveAuthInfo(String username, String password) {
        try {
            String uri = String.format(SAVE_URI);
            Request request = new Request.Builder()
                    .post(RequestBody.create(OkHttpUtil.JSON, JsonUtil.writeValueAsString(
                            UserInfoReqVO.builder()
                                    .username(username)
                                    .password(password)
                                    .build())))
                    .url(endpoint + uri)
                    .build();
            OkHttpRspDTO response = okHttpUtil.request(request);
            return Objects.nonNull(response) && response.isSuccess();
        } catch (Exception e) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }
}
