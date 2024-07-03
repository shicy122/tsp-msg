package com.hycan.idn.tsp.message.feign;

import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.message.constant.ServiceNameConstants;
import com.hycan.idn.tsp.message.pojo.AvntGuideRspDTO;
import com.hycan.idn.tsp.message.pojo.AvntReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;

/**
 * 获取MQTT Topic列表Feign接口
 *
 * @BelongsProject: tsp-message-center-service
 * @BelongsPackage: com.hycan.idn.tsp.message.feign
 * @Author: liangwenqi
 * @CreateTime: 2022-06-16 14:32
 * @Version: 1.0
 */
@FeignClient(contextId = "avntGuideFeignService", value = ServiceNameConstants.TSP_MESSAGE_CENTER_SERVICE)
public interface AvntGuideFeignService {

    @PostMapping("/avnt/v1/delivery/queryUrl")
    R<String> queryUrl(@RequestBody @Valid AvntReqDTO avntReqDTO, @RequestHeader(SecurityConstants.FROM) String from);

    @PostMapping("/avnt/v1/beginner/guideList")
    R<List<AvntGuideRspDTO>> guideList(@RequestBody @Valid AvntReqDTO avntReqDTO, @RequestHeader(SecurityConstants.FROM) String from);
}
