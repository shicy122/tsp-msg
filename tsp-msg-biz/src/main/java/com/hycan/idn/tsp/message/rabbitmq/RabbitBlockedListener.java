package com.hycan.idn.tsp.message.rabbitmq;

import com.rabbitmq.client.BlockedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-28 14:30
 */
@Slf4j
@Component
public class RabbitBlockedListener implements BlockedListener {

    /**
     * 阻塞状态
     */
    public static Boolean isBlocked = false;

    /**
     * 读写锁
     */
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public void handleBlocked(String s) {
        log.warn("======rabbitmq connection blocked, reason={}======", s);
        readWriteLock.writeLock().lock();
        try {
            isBlocked = true;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void handleUnblocked() {
        log.warn("======rabbitmq connection unblocked======");
        readWriteLock.writeLock().lock();
        try {
            isBlocked = false;
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }
}
