package com.jxp.integration.test.limiter;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import cn.hutool.core.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-06 15:46
 */

@Slf4j
public class GuavaLimiter implements LimiterManager {

    private static final Map<String, RateLimiter> rateLimiterMap = Maps.newConcurrentMap();

    @Override
    public boolean tryAccessByIp(LimitConfig config, String ip) {
        String key = "limit:" + ip;
        RateLimiter rateLimiter = rateLimiterMap.get(key);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(config.getLocalIpRate());
            rateLimiterMap.putIfAbsent(key, rateLimiter);
        }
        boolean acquire = rateLimiter.tryAcquire();
        log.info("GuavaLimiter tryAccessByIp fail,rate:{},ip:{}", rateLimiter.getRate(), ip);
        if (!acquire) {
            return true;
        }
        return false;
    }

    @Override
    public boolean rateLimitByUri(LimitConfig config, String uri, String method) {
        String key = uri + ":" + method;
        RateLimiter rateLimiter = rateLimiterMap.get(key);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(config.getLocalUriRate());
            rateLimiterMap.putIfAbsent(key, rateLimiter);
        }
        boolean acquire = rateLimiter.tryAcquire();
        log.info("GuavaLimiter rateLimitByUri fail,rate:{},key:{}", rateLimiter.getRate(), key);
        if (!acquire) {
            return true;
        }
        return false;
    }

    @Override
    public boolean tryAccess(LimitConfig config, String ip, String uri, String method) {
        if (BooleanUtil.isTrue(config.getLocalIpLimiter()) && tryAccessByIp(config, ip)) {
            return true;
        }
        return BooleanUtil.isTrue(config.getLocalUriLimiter()) && rateLimitByUri(config, uri, method);
    }
}
