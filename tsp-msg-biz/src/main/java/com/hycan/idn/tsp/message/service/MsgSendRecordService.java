package com.hycan.idn.tsp.message.service;

import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.entity.mongo.MsgSendRecordEntity;
import com.hycan.idn.tsp.message.pojo.SendMsgDTO;
import com.hycan.idn.tsp.message.repository.mongo.MsgSendRecordRepository;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.hycan.idn.tsp.message.utils.ThreadPoolUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 对消息内容表和消息发送状态表逻辑处理层
 *
 * @author Liuyingjie
 * @datetime 2022/10/16 0:13
 */
@Slf4j
@Service
public class MsgSendRecordService {

    /**
     * 日志打印间隔时间(毫秒)
     */
    private static final long MAX_LOG_INTERVAL_TIME = 1000;

    /**
     * 批量保存消息记录最大数量
     */
    private static final Integer MAX_BATCH_SIZE = 2000;

    /**
     * listener执行次数 计数器
     */
    private AtomicInteger saveExecuteCounter = new AtomicInteger();

    /**
     * listener执行次数 计数器
     */
    private AtomicInteger updateExecuteCounter = new AtomicInteger();

    /**
     * 上一条日志的时间戳
     */
    private final AtomicLong saveLastLogTime = new AtomicLong(0L);

    /**
     * 上一条日志的时间戳
     */
    private final AtomicLong updateLastLogTime = new AtomicLong(0L);

    private final BlockingQueue<MsgSendRecordEntity> saveQueue = new LinkedBlockingDeque<>(MAX_BATCH_SIZE);
    private final BlockingQueue<UpdateMsgStatusDTO> updateQueue = new LinkedBlockingDeque<>(MAX_BATCH_SIZE);

    private final ScheduledExecutorService saveRecordsExecutorService =
            ThreadPoolUtil.newFixedScheduledExecutorService(4, "save_msg_send_record");

    private final ScheduledExecutorService updateStatusExecutorService =
            ThreadPoolUtil.newFixedScheduledExecutorService(4, "update_msg_record_status");

    @Resource
    private MsgSendRecordRepository msgSendRecordRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initBatchSaveMsgExecutor() {
        saveRecordsExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (saveQueue.size() >= MAX_BATCH_SIZE) {
                    batchSaveRecords();
                } else {
                    int counter = saveExecuteCounter.incrementAndGet();
                    if (!saveQueue.isEmpty() && counter > 200) {
                        batchSaveRecords();
                        // 重新初始计数器
                        saveExecuteCounter = new AtomicInteger();
                    }
                }
            } catch (Exception e) {
                log.error("执行批量保存消息发送记录异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            }
        }, 10, 10, TimeUnit.MILLISECONDS);


        updateStatusExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (updateQueue.size() >= MAX_BATCH_SIZE) {
                    batchUpdateStatus();
                } else {
                    int counter = updateExecuteCounter.incrementAndGet();
                    if (!updateQueue.isEmpty() && counter > 500) {
                        batchUpdateStatus();
                        // 重新初始计数器
                        updateExecuteCounter = new AtomicInteger();
                    }
                }
            } catch (Exception e) {
                log.error("执行批量更新消息发送状态异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            }
        }, 10, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * 批量添加消息内容表
     */
    private void batchSaveRecords() {
        List<MsgSendRecordEntity> list = new ArrayList<>();
        saveQueue.drainTo(list, MAX_BATCH_SIZE);
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, MsgSendRecordEntity.class);
        list.forEach(item -> {
            Query query = Query.query(Criteria.where(MsgSendRecordEntity.MSG_ID).is(item.getMsgId()));
            Update update = Update.update(MsgSendRecordEntity.VIN, item.getVin())
                    .setOnInsert(MsgSendRecordEntity.MSG_ID, item.getMsgId())
                    .setOnInsert(MsgSendRecordEntity.STATUS, item.getStatus())
                    .set(MsgSendRecordEntity.RECORD_TIME, item.getRecordTime())
                    .setOnInsert(MsgSendRecordEntity.FAIL_TIMES, item.getFailTimes())
                    .set(MsgSendRecordEntity.REPORT_TIME, item.getReportTime())
                    .set(MsgSendRecordEntity.PAYLOAD, item.getPayload())
                    .set(MsgSendRecordEntity.BUS_TYPE, item.getBussType());
            operations.upsert(query, update);
        });

        operations.execute();
    }

    /**
     * 批量更新消息发送状态
     */
    private void batchUpdateStatus() throws InterruptedException {
        List<UpdateMsgStatusDTO> list = new ArrayList<>();
        updateQueue.drainTo(list, MAX_BATCH_SIZE);
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, MsgSendRecordEntity.class);
        list.forEach(item -> {
                    Query query = Query.query(Criteria.where(MsgSendRecordEntity.MSG_ID).is(item.getMsgId()));
                    Update update = Update.update(MsgSendRecordEntity.STATUS, item.getStatus())
                            .setOnInsert(MsgSendRecordEntity.MSG_ID, item.getMsgId());
                    if (MsgSendStatusConstant.FAILURE.equals(item.getStatus())) {
                        update.inc(MsgSendRecordEntity.FAIL_TIMES, 1);
                    } else {
                        update.setOnInsert(MsgSendRecordEntity.FAIL_TIMES, 0);
                    }
                    operations.upsert(query, update);
                });

        try {
            operations.execute();
        } catch (Exception e) {
            // 出现并发问题时，再执行一次
            TimeUnit.MILLISECONDS.sleep(100);
            operations.execute();
        }
    }

    public void addBlockingQueue(String msgId, String status) {
        try {
            if (updateQueue.size() >= MAX_BATCH_SIZE) {
                long now = System.currentTimeMillis();
                if (now - updateLastLogTime.get() < MAX_LOG_INTERVAL_TIME) {
                    updateLastLogTime.set(now);
                    log.warn("消息发送状态队列容量超过阈值!");
                }
            }
            updateQueue.put(UpdateMsgStatusDTO.of(msgId, status));
        } catch (InterruptedException e) {
            log.error("消息发送状态阻塞队列添加数据异常={}", ExceptionUtil.getAllExceptionStackTrace(e));
        }
    }

    public void addBlockingQueue(SendMsgDTO sendMsgDTO) {
        try {
            if (saveQueue.size() >= MAX_BATCH_SIZE) {
                long now = System.currentTimeMillis();
                if (now - saveLastLogTime.get() < MAX_LOG_INTERVAL_TIME) {
                    saveLastLogTime.set(now);
                    log.warn("消息发送记录队列容量超过阈值!");
                }
            }
            saveQueue.put(MsgSendRecordEntity.of(sendMsgDTO));
        } catch (InterruptedException e) {
            log.error("消息发送记录阻塞队列添加数据异常={}", ExceptionUtil.getAllExceptionStackTrace(e));
        }
    }

    /**
     * 判断消息是否重复
     *
     * @param msgId 消息ID
     * @return true:重复 false:不重复
     */
    public boolean isDuplicateMsg(String msgId, String bussType) {
        MsgSendRecordEntity entity = msgSendRecordRepository.findByMsgIdAndBussType(msgId, bussType);
        return Objects.nonNull(entity);
    }

    @Data
    static class UpdateMsgStatusDTO {
        private String msgId;
        private String status;

        public static UpdateMsgStatusDTO of(String msgId, String status) {
            UpdateMsgStatusDTO dto = new UpdateMsgStatusDTO();
            dto.setMsgId(msgId);
            dto.setStatus(status);
            return dto;
        }
    }
}
