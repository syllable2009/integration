package com.jxp.integration.test.plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.jxp.integration.base.tool.IPUtil;
import com.jxp.integration.base.tool.JacksonUtils;
import com.jxp.integration.test.limiter.LimitConfig;
import com.jxp.integration.test.limiter.LimiterManager;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 14:42
 * 限流配置信息举例说明，如下所示
 * "ip.rate.limit" :   "1-150"                             ------>   IP限流配置信息
 * "/api/v1/member/addMembersInSpace" :   "60-5"           ------>   URI限流配置信息
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 3)
@Component
public class HttpApiRateLimitFilter extends OncePerRequestFilter {

    //    private static final RateLimiter rateLimiter = RateLimiter.create(10.0); // 设置每秒最多处理10个请求
    private static final Map<String, LimitConfig> rateLimitMap = ImmutableMap.of();
    private static final LimitConfig DEFAULT_LIMIT_CONFIG = LimitConfig.builder()
            .localIpLimiter(true)
            .localIpRate(0.5)
            .localUriLimiter(true)
            .localUriRate(0.5)
            .redisIpLimiter(true)
            .redisIpPerSecond(30)
            .redisIpExpire(60)
            .redisUriLimiter(true)
            .redisUriPerSecond(30)
            .redisUriExpire(60)
            .build();

    @Resource(name = "redisLimiter")
    LimiterManager redisLimiter;

    @Resource(name = "guavaLimiter")
    LimiterManager guavaLimiter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("HttpApiRateLimitFilter");
        // 判断是local还是redis
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        LimitConfig limitConfig =
                rateLimitMap.getOrDefault(StrUtil.concat(true, method, ":", requestURI), DEFAULT_LIMIT_CONFIG);
        if (!BooleanUtil
                .or(limitConfig.getLocalIpLimiter(), limitConfig.getLocalUriLimiter(), limitConfig.getRedisIpLimiter(),
                        limitConfig.getRedisUriLimiter())) {
            filterChain.doFilter(request, response);
            return;
        }
        String ip = IPUtil.getIpAddr(request);
        if (BooleanUtil.or(limitConfig.getLocalIpLimiter(), limitConfig.getLocalUriLimiter())) {
            // 本地可以改造成单机限流
            if (guavaLimiter.tryAccess(limitConfig, ip, requestURI, method)) {
                String userId = RequestContext.getUserId();
                log.warn("userId->{},通过ip->{},请求method:{},请求URI->{},local限流,不允许访问", userId, ip, method,
                        request.getRequestURI());
                setRateLimitResponse(response);
                return;
            }
        }
        if (BooleanUtil.or(limitConfig.getRedisIpLimiter(), limitConfig.getRedisUriLimiter())) {
            if (redisLimiter.tryAccess(limitConfig, ip, requestURI, method)) {
                String userId = RequestContext.getUserId();
                log.warn("userId->{},通过ip->{},请求method:{},请求URI->{},redis限流,不允许访问", userId, ip, method,
                        request.getRequestURI());
                setRateLimitResponse(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setRateLimitResponse(@NotNull HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pw = response.getWriter();
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("code", 403);
        ret.put("message", "当前请求量过大，请稍后访问");
        pw.println(JacksonUtils.toJsonStr(ret));
        pw.flush();
    }

}
