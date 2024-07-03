package com.hycan.idn.tsp.message.facade;

import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.constant.RedisKeyConstants;
import com.hycan.idn.tsp.message.service.MsgSendRecordService;
import com.hycan.idn.tsp.message.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-25 15:43
 */
@Slf4j
@Component
public class RedissonFacade {

    /**
     * 布隆过滤器大小
     */
    private static final Integer BLOOM_FILTER_SIZE = 2000000;

    /**
     * 布隆过滤器 fpp
     */
    private static final Double BLOOM_FILTER_FPP = 0.01;

    /**
     * 初始化布隆过滤器
     */
    private RBloomFilter<String> bloomFilter;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private MsgSendRecordService msgSendRecordService;

    /**
     * 判断消息id+业务类型是否重复（捕获布隆过滤器初始化失败异常）
     *
     * @param msgId    消息ID
     * @param bussType 业务类型
     * @return true:重复 false:不重复
     */
    public boolean isDuplicateMsg(String msgId, String bussType) {
        final String msgKey = msgId + CommonConstants.FILE_JOINT + bussType;
        try {
            if (Objects.isNull(bloomFilter) || !bloomFilter.isExists()) {
                bloomFilter = redissonClient.getBloomFilter(RedisKeyConstants.BLOOM_FILTER_NAME);
                bloomFilter.tryInit(BLOOM_FILTER_SIZE, BLOOM_FILTER_FPP);
                log.info("初始化布隆过滤器, 结果={}", bloomFilter.isExists());
            }
            if (bloomFilter.contains(msgKey) && msgSendRecordService.isDuplicateMsg(msgId, bussType)) {
                return true;
            }

            bloomFilter.add(msgKey);
            if (bloomFilter.count() >= (BLOOM_FILTER_SIZE / 2)) {
                bloomFilter.delete();
                bloomFilter = redissonClient.getBloomFilter(RedisKeyConstants.BLOOM_FILTER_NAME);
                bloomFilter.tryInit(BLOOM_FILTER_SIZE, BLOOM_FILTER_FPP);
            }
        } catch (Exception e) {
            log.error("使用布隆过滤器校验消息ID重复异常, 详情={}", ExceptionUtil.getBriefStackTrace(e));
        }

        return false;
    }
}
