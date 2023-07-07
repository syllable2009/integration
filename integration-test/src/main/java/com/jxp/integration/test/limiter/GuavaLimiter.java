package com.jxp.integration.test.limiter;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import cn.hutool.core.util.NumberUtil;
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
        return false;
    }

    @Override
    public boolean rateLimitByUri(LimitConfig config, String uri, String method) {
        String key = uri + ":" + method;
        RateLimiter rateLimiter = rateLimiterMap.get(key);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(NumberUtil.div(30,60));
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
        if (tryAccessByIp(config, ip)) {
            return true;
        }
        return rateLimitByUri(config, uri, method);
    }
}
