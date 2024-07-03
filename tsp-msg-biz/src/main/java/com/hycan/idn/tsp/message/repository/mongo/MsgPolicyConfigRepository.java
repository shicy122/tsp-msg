package com.hycan.idn.tsp.message.repository.mongo;

import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @BelongsProject: tsp-message-center-service
 * @BelongsPackage: com.hycan.idn.tsp.message.dao
 * @Author: liangwenqi
 * @CreateTime: 2022-06-20  11:56
 * @Version: 1.0
 */
@Repository
public interface MsgPolicyConfigRepository extends MongoRepository<MsgPolicyConfigEntity, String> {

    /**
     * 根据车系和业务类型获取数据DIRECTION不为down
     *
     * @param bussType
     * @param serials
     * @return
     */
    @Query("{$and:[{'bussType':?0}, {'serials':?1}]}")
    List<MsgPolicyConfigEntity> findByBussTypeAndSerials(String bussType, String serials);

    /**
     * 根据车型号 协议类型获取相关数据
     *
     * @param serials
     * @param protocol
     * @return
     */
    @Query("{$and:[{'serials':?0},{'forward_configs': {'$elemMatch': {'protocol': ?1 }}}]}")
    List<MsgPolicyConfigEntity> findBySerialsAndProtocol(String serials, String protocol);

}
