package com.jxp.integration.test.limiter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-06 15:46
 */

@Slf4j
public class GuavaLimiter implements LimiterManager {

    @Override
    public boolean tryAccessByIp(LimitConfig config, String ip) {
        return false;
    }

    @Override
    public boolean rateLimitByUri(LimitConfig config, String uri, String method) {
        return false;
    }

    @Override
    public boolean tryAccess(LimitConfig config, String ip, String uri, String method) {
        return false;
    }
}
