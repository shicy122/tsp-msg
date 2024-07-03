package com.hycan.idn.tsp.message.exception;

import com.alibaba.csp.sentinel.Tracer;
import com.hycan.idn.tsp.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 消息中心全局异常处理
 * @BelongsProject: tsp-message-center-service
 * @BelongsPackage: com.hycan.idn.tsp.message.handler
 * @Author: liangwenqi
 * @CreateTime: 2022-06-17  14:24
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalExceptionHandler{

    /**
     * 业务异常处理
     *
     * @param e e
     * @return {@link R}
     */
    @ExceptionHandler(MsgBusinessException.class)
    public R tokenAnalysisException(MsgBusinessException e) {
        log.error("{}", e.getMessage());
        Tracer.trace(e);
        return R.restResult(e.getData(),  e.getCode(), e.getMessage());
    }
}
