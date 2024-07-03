package com.hycan.idn.tsp.message;

import com.hycan.idn.tsp.common.feign.annotation.EnableTspFeignClients;
import com.hycan.idn.tsp.common.job.annotation.EnableTspXxlJob;
import com.hycan.idn.tsp.common.security.annotation.EnableTspResourceServer;
import com.hycan.idn.tsp.common.storage.annotation.EnableObjectStorage;
import com.hycan.idn.tsp.common.swagger.annotation.EnableTspSwagger2;
import io.mongock.runner.springboot.EnableMongock;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

/**
 * @author pig archetype
 * <p>
 * 项目启动类
 */
@Slf4j
@EnableTspXxlJob
@EnableScheduling
@EnableMongock
@EnableAsync
@EnableTspSwagger2
@EnableTspFeignClients
@EnableTspResourceServer
@EnableDiscoveryClient
@EnableObjectStorage
@MapperScan(basePackages = "com.hycan.idn.tsp.message.repository.mysql")
@EnableMongoRepositories(basePackages = "com.hycan.idn.tsp.message.repository.mongo")
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class TspMessageApplication {
    /**
     * 主方法
     *
     * @param args arg游戏
     */
    public static void main(String[] args) {
        SpringApplication.run(TspMessageApplication.class, args);
        log.info("========== 启动成功 {} ==========", LocalDateTime.now());
    }
}
