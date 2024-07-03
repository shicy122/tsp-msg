package com.hycan.idn.tsp.message.config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * mqtt配置
 *
 * @author liangliang
 * @datetime 2024-02-20 14:48
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttConfig {
	/** 出站通道 */
	public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";
	/** 输入通道 */
	public static final String INPUT_CHANNEL = "mqttInputChannel";

	/** 连接超时 */
	public static final Integer CONNECTION_TIMEOUT = 60;
	/** 长连接最大时长为120秒 */
	public static final Integer KEEP_ALIVE_INTERVAL = 120;

	/** mqtt客户端 */
	private static MqttClient client;

	/** 用户名 */
	private String username;

	/** 密码 */
	private String password;

	/** MQTT集群地址 */
	private String[] url;

	/** 订阅主题 */
	private String[] topics;

	/** MQTTX服务的k8s 接入地址 */
	private String mqttxEndpoint;

	/** 连接超时 */
	public Integer connectionTimeout = 60;

	/** 长连接最大时长为120秒 */
	public Integer keepAliveInterval = 120;

	/** 完成断开连接超时时间5秒 */
	public Integer completionTimeout = 5000;

	/** 重试间隔时间10秒 */
	public Integer recoveryInterval = 10000;
}
