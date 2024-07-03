package com.hycan.idn.tsp.message.feign;

import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.common.security.annotation.Inner;
import com.hycan.idn.tsp.message.pojo.MqttTopicsRspDTO;
import com.hycan.idn.tsp.message.service.AvntMqttService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 获取MQTT列表接口
 *
 * @author liangwenqi
 * @datetime 2022-06-10 09:14
 */
@Api(value = "MqttController", tags = "MQTT模块")
@Validated
@RestController
@RequestMapping("/tsp-api/msg-center-svc/v1")
public class AvntMqttFeignImpl implements RemoteMsgMqttService {

    @Resource
    private AvntMqttService avntMqttService;

    /**
     * AVNT获取topic列表
     * @param clientId 客户机id
     * @param token    令牌
     * @return {@link R}<{@link MqttTopicsRspDTO}>
     */
    @Inner
    @ApiOperation("AVNT获取topic列表接口")
    @ApiImplicitParam(name = "Authentication", value = "Authentication",
            required = true, dataType = "String", paramType = "header")
    @GetMapping("/mqtt-topics")
    @Override
    public R<MqttTopicsRspDTO> getMqttTopics(@RequestParam("clientId") String clientId,
                                         @RequestParam("token") String token, String from) {

        return R.ok(avntMqttService.getMqttTopics(clientId, token));
    }
}
