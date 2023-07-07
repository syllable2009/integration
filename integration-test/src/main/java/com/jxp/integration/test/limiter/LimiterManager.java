package com.jxp.integration.test.limiter;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-06 15:45
 */
public interface LimiterManager {

    boolean tryAccessByIp(LimitConfig config, String ip);

    boolean rateLimitByUri(LimitConfig config, String uri, String method);

    // true返回限流，false不限
    boolean tryAccess(LimitConfig config, String ip, String uri, String method);
}
