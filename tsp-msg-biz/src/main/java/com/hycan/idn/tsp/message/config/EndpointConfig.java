package com.hycan.idn.tsp.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 图文渲染url配置
 *
 * @author liangliang
 * @datetime 2024-02-20 14:32
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "endpoint")
public class EndpointConfig {
    /**
     * CDN加速地址
     */
    private String cdn;
    
    /**
     * TSP地址
     */
    private String tspMsgHost;

    /**
     * 文件上传OBS路径
     */
    private String obsPathPrefix;
}
