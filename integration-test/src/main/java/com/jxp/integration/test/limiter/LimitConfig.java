package com.jxp.integration.test.limiter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-06 16:11
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LimitConfig {

    private String uri;

    private String method;

    private Boolean localIpLimiter;
    private Double localIpRate;

    private Boolean localUriLimiter;
    private Double localUriRate;

    private Boolean redisIpLimiter;

    // 最多的访问限制次数
    private long redisIpPerSecond;
    //过期时间也可以理解为单位时间，单位秒
    private long redisIpExpire;

    private Boolean redisUriLimiter;

    // 最多的访问限制次数
    private long redisUriPerSecond;
    //过期时间也可以理解为单位时间，单位秒
    private long redisUriExpire;

    private String msg;
}
