package com.jxp.integration.test.limiter;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * @author jiaxiaopeng
 * Created on 2023-07-06 15:48
 */
@Configuration
public class LimiterConfig {
//    @Resource
//    StringRedisTemplate stringRedisTemplate;

    @Bean(name = "guavaLimiter")
    @ConditionalOnProperty(name = "limit.local", havingValue = "true")
    public LimiterManager guavaLimiter() {
        return new GuavaLimiter();
    }

    @Bean(name = "redisLimiter")
    @ConditionalOnProperty(name = "limit.redis", havingValue = "true")
    public LimiterManager redisLimiter() {
        StringRedisTemplate stringRedisTemplate = Mockito.mock(StringRedisTemplate.class);
        RedisLimiter redisLimiter = new RedisLimiter();
        redisLimiter.init();
        redisLimiter.setStringRedisTemplate(stringRedisTemplate);
        return redisLimiter;
    }

}
