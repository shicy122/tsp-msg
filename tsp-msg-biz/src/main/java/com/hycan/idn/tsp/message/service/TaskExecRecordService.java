package com.hycan.idn.tsp.message.service;

import com.hycan.idn.tsp.message.constant.MsgSendStatusConstant;
import com.hycan.idn.tsp.message.entity.mongo.TaskExecRecordEntity;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.PushTaskDetailReqVO;
import com.hycan.idn.tsp.message.pojo.basetask.PushTaskDetailRspVO;
import com.hycan.idn.tsp.message.pojo.basetask.StatisticsRspDTO;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import com.hycan.idn.tsp.message.utils.ThreadPoolUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-28 17:16
 */
@Slf4j
@Service
public class TaskExecRecordService {

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
    private AtomicInteger updateExecuteCounter = new AtomicInteger();

    /**
     * 上一条日志的时间戳
     */
    private final AtomicLong updateLastLogTime = new AtomicLong(0L);

    private final BlockingQueue<UpdateExecStatusDTO> updateQueue = new LinkedBlockingDeque<>(MAX_BATCH_SIZE);

    private final ScheduledExecutorService updateStatusExecutorService =
            ThreadPoolUtil.newFixedScheduledExecutorService(4, "update_exec_record_status");

    @Resource
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initBatchSaveMsgExecutor() {
        updateStatusExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (updateQueue.size() >= MAX_BATCH_SIZE) {
                    batchUpdateStatus();
                } else {
                    int counter = updateExecuteCounter.incrementAndGet();
                    if (!updateQueue.isEmpty() && counter > 200) {
                        batchUpdateStatus();
                        // 重新初始计数器
                        updateExecuteCounter = new AtomicInteger();
                    }
                }
            } catch (Exception e) {
                log.error("执行批量更新任务执行状态异常, 详情=[{}]", ExceptionUtil.getBriefStackTrace(e));
            }
        }, 10, 100, TimeUnit.MILLISECONDS);
    }

    public void updateTaskExecRecord(String recordId, String msgId) {
        Query query = Query.query(Criteria.where("id").is(recordId));
        Update update = new Update()
                .set(TaskExecRecordEntity.STATUS, MsgSendStatusConstant.SENDING)
                .set(TaskExecRecordEntity.MSG_ID, msgId)
                .set(TaskExecRecordEntity.SEND_TIME, LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, TaskExecRecordEntity.class);
    }

    public PageRspVO<PushTaskDetailRspVO> listPushDetail(PushTaskDetailReqVO reqVO) {
        Pageable pageable = PageRequest.of(reqVO.getCurrent() - 1, reqVO.getSize());
        Criteria criteria = Criteria.where(TaskExecRecordEntity.BASE_TASK_ID).is(reqVO.getBaseTaskId());
        if (reqVO.getSerialId() != null) {
            criteria.and(TaskExecRecordEntity.SERIAL_ID).is(reqVO.getSerialId());
        }
        if (reqVO.getVin() != null) {
            criteria.and(TaskExecRecordEntity.VIN).is(reqVO.getVin());
        }
        if (reqVO.getStatus() != null) {
            criteria.and(TaskExecRecordEntity.STATUS).is(reqVO.getStatus());
        }
        Query query = Query.query(criteria).with(pageable);
        List<PushTaskDetailRspVO> results = mongoTemplate.find(query, PushTaskDetailRspVO.class, TaskExecRecordEntity.COLLECTION_NAME);
        Long count = mongoTemplate.count(query, TaskExecRecordEntity.class);
        return PageRspVO.of(results, reqVO.getCurrent(), reqVO.getSize(), count);
    }

    public List<StatisticsRspDTO> showPushStatistic(Long baseTaskId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(TaskExecRecordEntity.BASE_TASK_ID).is(baseTaskId)),
                Aggregation.group(TaskExecRecordEntity.STATUS)
                        .count().as(TaskExecRecordEntity.COUNT));

        AggregationResults<StatisticsRspDTO> results = mongoTemplate.aggregate(
                aggregation, TaskExecRecordEntity.COLLECTION_NAME, StatisticsRspDTO.class);
        return results.getMappedResults();
    }

    public void addBlockingQueue(String msgId, String status) {
        try {
            if (updateQueue.size() >= MAX_BATCH_SIZE) {
                long now = System.currentTimeMillis();
                if (now - updateLastLogTime.get() < MAX_LOG_INTERVAL_TIME) {
                    updateLastLogTime.set(now);
                    log.warn("任务执行记录队列容量超过阈值!");
                }
            }
            updateQueue.put(UpdateExecStatusDTO.of(msgId, status));
        } catch (InterruptedException e) {
            log.error("任务执行记录阻塞队列添加数据异常={}", ExceptionUtil.getAllExceptionStackTrace(e));
        }
    }

    private void batchUpdateStatus() {
        List<UpdateExecStatusDTO> list = new ArrayList<>();
        updateQueue.drainTo(list, MAX_BATCH_SIZE);
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TaskExecRecordEntity.class);
        list.forEach(item ->
                operations.updateMulti(
                        Query.query(Criteria.where(TaskExecRecordEntity.MSG_ID).is(item.getMsgId())),
                        Update.update(TaskExecRecordEntity.STATUS, item.getStatus())));
        operations.execute();
    }

    @Data
    static class UpdateExecStatusDTO {
        private String msgId;
        private String status;

        public static UpdateExecStatusDTO of(String msgId, String status) {
            UpdateExecStatusDTO dto = new UpdateExecStatusDTO();
            dto.setMsgId(msgId);
            dto.setStatus(status);
            return dto;
        }
    }
}
