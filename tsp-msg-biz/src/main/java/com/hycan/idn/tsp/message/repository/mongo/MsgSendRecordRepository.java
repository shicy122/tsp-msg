package com.hycan.idn.tsp.message.repository.mongo;

import com.hycan.idn.tsp.message.entity.mongo.MsgSendRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liangwenqi
 * @datetime 2022-06-09  16:46
 */
@Repository
public interface MsgSendRecordRepository extends PagingAndSortingRepository<MsgSendRecordEntity, String> {
    /**
     * 通过消息id获取消息内容记录
     *
     * @param msgId 消息ID
     * @return 消息内容记录
     */
    @Query("{$and:[{'msg_id' : ?0}, {'buss_type' : ?1}]}")
    MsgSendRecordEntity findByMsgIdAndBussType(String msgId, String bussType);

    /**
     * 通过消息id+业务类型
     * @return
     */
    @Query("{$and:[{'status' : 'FAILURE'}, {'fail_times' : {$lte : 4}}]}")
    Page<MsgSendRecordEntity> findFailureMsg(Pageable pageable);
}

