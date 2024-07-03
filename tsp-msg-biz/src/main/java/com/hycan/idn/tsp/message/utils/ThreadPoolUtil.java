package com.hycan.idn.tsp.message.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "thread")
public class ThreadPoolUtil {

    public static final String MSG_MQTT_THREAD = "msg_mqtt_thread";
    public static final String MSG_RABBITMQ_THREAD = "msg_rabbitmq_thread";
    public static final String MSG_VIN_THREAD = "msg_vin_thread";

    private static final int DEFAULT_CAPACITY = 2000;

    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = (t, e) ->
            log.error("thread {} encounter a uncaught exception={}", t.getName(), e.toString());


    private int mqttCorePoolSize;
    private int mqttMaxPoolSize;
    private int rabbitmqCorePoolSize;
    private int rabbitmqMaxPoolSize;
    private int batchSaveCorePoolSize;
    private int batchSaveMaxPoolSize;
    /**
     * MQTT消息
     */
    @Bean(name = MSG_MQTT_THREAD)
    public Executor msgMqttThread() {
        return newThreadPoolExecutor(mqttCorePoolSize, mqttMaxPoolSize, MSG_MQTT_THREAD,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * RabbitMQ
     */
    @Bean(name = MSG_RABBITMQ_THREAD)
    public Executor msgRabbitmqThread() {
        return newThreadPoolExecutor(rabbitmqCorePoolSize, rabbitmqMaxPoolSize, MSG_RABBITMQ_THREAD,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ExecutorService newThreadPoolExecutor(int corePoolSize,
                                                        int maxPoolSize,
                                                        String threadName,
                                                        RejectedExecutionHandler handler) {
        return newThreadPoolExecutor(corePoolSize, maxPoolSize, DEFAULT_CAPACITY, threadName, handler);
    }

    public static ExecutorService newThreadPoolExecutor(int corePoolSize, int maxPoolSize, int capacity,
                                                        String threadName, RejectedExecutionHandler handler) {
        ThreadFactory factory = createThreadFactory(threadName);
        if (corePoolSize < 0) {
            corePoolSize = 1;
        }

        if (maxPoolSize < 0) {
            maxPoolSize = 1;
        }

        if (capacity < 0) {
            capacity = DEFAULT_CAPACITY;
        }

        if (Objects.isNull(handler)) {
            handler = new ThreadPoolExecutor.CallerRunsPolicy();
        }

        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(capacity), factory, handler);
    }

    public static ScheduledExecutorService newFixedScheduledExecutorService(int poolSize, String threadName) {
        ThreadFactory factory = Executors.defaultThreadFactory();
        if (!ObjectUtils.isEmpty(threadName)) {
            factory = createThreadFactory(threadName);
        }
        if (poolSize < 0) {
            poolSize = 1;
        }
        return new ScheduledThreadPoolExecutor(poolSize, factory);
    }

    /**
     * 创建一个线程工程，产生的线程格式为：Thread-threadName-1, Thread-threadName-2
     *
     * @param threadName 线程名
     * @return 线程工程，用于线程池创建可读的线程
     */
    private static ThreadFactory createThreadFactory(String threadName) {
        return new ThreadFactoryBuilder()
                .setNameFormat("Thread-" + threadName + "-%d")
                .setUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER)
                .setDaemon(false)
                .build();
    }
    @Bean(MSG_VIN_THREAD)
    public Executor threadPoolExecutor () {
        return newThreadPoolExecutor(
                mqttCorePoolSize,
                mqttMaxPoolSize,
                MSG_VIN_THREAD,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
