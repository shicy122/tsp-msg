package com.hycan.idn.tsp.message.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.utils.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-27 09:57
 */
@Slf4j
@Component
public class PayloadSecretCache {

    @Resource
    private VehBaseInfoCache vehBaseInfoCache;

    private final Cache<String, String> secretCache = Caffeine.newBuilder()
            .initialCapacity(500)
            .maximumSize(50000)
            .build();

    public String getPayloadSecret(String vin) {
        String secret = secretCache.getIfPresent(vin);
        if (StringUtils.isNotBlank(secret)) {
            return secret;
        }

        String cdcSn = vehBaseInfoCache.getCdcSnByVin(vin);
        String canonical = vin + CommonConstants.UNDERLINE + cdcSn;
        secret = SHA256Util.signWithHmacSha256(vin, canonical);
        if (StringUtils.isNotBlank(secret)) {
            secretCache.put(vin, secret);
            return secret;
        }

        return secret;
    }
}
