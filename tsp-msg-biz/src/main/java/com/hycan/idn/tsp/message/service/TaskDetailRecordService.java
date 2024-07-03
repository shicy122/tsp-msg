package com.hycan.idn.tsp.message.service;

import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.entity.mongo.TaskDetailRecordEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-28 17:16
 */
@Service
public class TaskDetailRecordService {

    @Resource
    private MongoTemplate mongoTemplate;

    public void updateDetailRecordForbidden(List<Long> baseTaskIdList) {
        Query query = Query.query(Criteria.where(TaskDetailRecordEntity.BASE_TASK_ID).in(baseTaskIdList));
        Update update = new Update().set(TaskDetailRecordEntity.STATUS, BizDataStatusConstants.FORBIDDEN);
        mongoTemplate.updateMulti(query, update, TaskDetailRecordEntity.class);
    }
}
