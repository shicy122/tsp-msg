package com.hycan.idn.tsp.message.repository.mongo;

import com.hycan.idn.tsp.message.entity.mongo.TaskExecRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息发送详情表
 *
 * @author Liuyingjie
 * @datetime :2022/8/30 15:54
 */
@Repository
public interface TaskExecRecordRepository extends PagingAndSortingRepository<TaskExecRecordEntity, String> {

    @Query("{'vin' : ?0, 'status' : 'UNSENT', 'enable_time' : {$lt : ?1}, 'disable_time' : {$gt : ?1}}")
    List<TaskExecRecordEntity> findUnsentTasksByVin(String vin, LocalDateTime now);

    @Query("{'status' : 'UNSENT', 'enable_time' : {$lt : ?0}, 'disable_time' : {$gt : ?0}}")
    Page<TaskExecRecordEntity> findUnsentTasksPage(LocalDateTime now, Pageable pageable);
}
