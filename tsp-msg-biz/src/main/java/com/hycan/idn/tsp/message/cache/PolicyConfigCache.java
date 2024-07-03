package com.hycan.idn.tsp.message.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity;
import com.hycan.idn.tsp.message.entity.mongo.MsgPolicyConfigEntity.ForwardConfig;
import com.hycan.idn.tsp.message.repository.mongo.MsgPolicyConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-25 17:23
 */
@Slf4j
@Component
public class PolicyConfigCache {

    @Resource
    private MsgPolicyConfigRepository msgPolicyConfigRepository;

    @Resource
    private VehBaseInfoCache vehBaseInfoCache;

    /**
     * 缓存消息策略配置表信息，key为业务类型_车型  value为List<表对象>
     */
    private final Cache<String, List<MsgPolicyConfigEntity.ForwardConfig>> forwardConfigCache = Caffeine.newBuilder()
            .initialCapacity(20)
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public List<ForwardConfig> getForwardConfigList(String bussType, String vin) {
        String serial = vehBaseInfoCache.getSerialByVin(vin);
        if (StringUtils.isEmpty(serial)) {
            log.warn("VIN=[{}]无关联的车系!",vin);
            return Collections.emptyList();
        }

        final String key = bussType + CommonConstants.UNDERLINE + serial;
        List<ForwardConfig> forwardConfigList = forwardConfigCache.getIfPresent(key);
        if (!CollectionUtils.isEmpty(forwardConfigList)) {
            return forwardConfigList;
        }

        List<MsgPolicyConfigEntity> policyConfigList = msgPolicyConfigRepository.findByBussTypeAndSerials(bussType, serial);
        if (CollectionUtils.isEmpty(policyConfigList)) {
            return Collections.emptyList();
        }

        for (MsgPolicyConfigEntity msgPolicyConfigEntity : policyConfigList) {
            forwardConfigList = msgPolicyConfigEntity.getForwardConfigs();
            forwardConfigCache.put(key, forwardConfigList);
            return forwardConfigList;
        }

        log.warn("消息转发配置不存在! 业务类型=[{}]、车系=[{}]", bussType, serial);
        return Collections.emptyList();
    }
}
