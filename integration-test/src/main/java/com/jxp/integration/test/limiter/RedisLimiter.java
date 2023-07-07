package com.jxp.integration.test.limiter;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import com.google.common.collect.Lists;

import cn.hutool.core.util.BooleanUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-06 15:46
 */
@Data
@Slf4j
public class RedisLimiter implements LimiterManager {

    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
    }

    @Override
    public boolean tryAccessByIp(LimitConfig config, String ip) {
        String key = "limit:" + ip;
        Long count = stringRedisTemplate
                .execute(redisScript, Lists.newArrayList(key), String.valueOf(config.getRedisIpPerSecond()),
                        String.valueOf(config.getRedisIpExpire()));
        if (count != null && count == 0) {
            log.info("RedisLimiter tryAccessByIp fail,count:{},key={}", count, key);
            return true;
        }
        return false;
    }

    @Override
    public boolean rateLimitByUri(LimitConfig config, String uri, String method) {
        String key = method + ":" + uri;
        Long count = stringRedisTemplate
                .execute(redisScript, Lists.newArrayList(key), String.valueOf(config.getRedisUriPerSecond()),
                        String.valueOf(config.getRedisUriExpire()));
        if (count != null && count == 0) {
            log.info("RedisLimiter rateLimitByUri fail,count:{},key={}", count, key);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryAccess(LimitConfig config, String ip, String uri, String method) {
        if (BooleanUtil.isTrue(config.getRedisIpLimiter()) && tryAccessByIp(config, ip)) {
            return true;
        }
        return BooleanUtil.isTrue(config.getRedisUriLimiter()) && rateLimitByUri(config, uri, method);

    }
}
