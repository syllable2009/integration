package com.jxp.integration.test.limiter;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import com.google.common.collect.Lists;

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
        return false;
    }

    @Override
    public boolean rateLimitByUri(LimitConfig config, String uri, String method) {
        String key = uri + ":" + method;
        Long count = stringRedisTemplate
                .execute(redisScript, Lists.newArrayList(key), String.valueOf(config.getPermitsPerSecond()),
                        String.valueOf(config.getExpire()));
        log.info("Access try count is {} for key={}", count, key);
        if (count != null && count == 0) {
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
