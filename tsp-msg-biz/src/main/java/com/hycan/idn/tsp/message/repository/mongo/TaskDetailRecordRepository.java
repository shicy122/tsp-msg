package com.hycan.idn.tsp.message.repository.mongo;

import com.hycan.idn.tsp.message.entity.mongo.TaskDetailRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 消息任务
 *
 * @author liangliang
 * @date 2022/08/12
 */
@Repository
public interface TaskDetailRecordRepository extends MongoRepository<TaskDetailRecordEntity, String> {

    /**
     * 根据任务id、状态获取数据
     *
     * @param baseTaskId
     * @return
     */
    @Query("{'base_task_id' : ?0, 'exec_policy' : ?1, 'status' : 'ENABLE'}")
    TaskDetailRecordEntity findDetailRecord(Long baseTaskId, String execPolicy);

    /**
     * 根据任务id、状态获取数据
     *
     * @param baseTaskId
     * @return
     */
    @Query("{'base_task_id' : ?0, 'status' : 'ENABLE'}")
    TaskDetailRecordEntity findDetailRecord(Long baseTaskId);
}
