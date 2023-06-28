package com.jxp.integration.test.plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.jxp.integration.base.tool.IPUtil;
import com.jxp.integration.base.tool.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 14:42
 * 限流配置信息举例说明，如下所示
 * "ip.rate.limit" :   "1-150"                             ------>   IP限流配置信息
 * "/api/v1/member/addMembersInSpace" :   "60-5"           ------>   URI限流配置信息
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@Component
public class HttpApiRateLimitFilter extends OncePerRequestFilter {

    private static final RateLimiter rateLimiter = RateLimiter.create(10.0); // 设置每秒最多处理10个请求

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("HttpApiRateLimitFilter");
        if (this.rateLimit(request)) {
            setRateLimitResponse(response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean rateLimit(@NotNull HttpServletRequest request) {
        String ip = IPUtil.getIpAddr(request);
        String userId = RequestContext.getUserId();
        if (StringUtils.isEmpty(ip)) {
            log.warn("userId->{},通过未知ip->{},请求URI->{},直接限流,不允许访问", userId, ip, request.getRequestURI());
            return true;
        }
        // 解析的配置
        Map<String, String> rateLimitMap = Maps.newHashMap();
        return this.rateLimitByIp(rateLimitMap, ip)
                || this.rateLimitByUri(rateLimitMap, request.getRequestURI(), userId);
    }

    private boolean rateLimitByIp(Map<String, String> rateLimitMap, String ip) {
        return false;
    }

    private boolean rateLimitByUri(Map<String, String> rateLimitMap, String uri, String userId) {
        return false;
    }

    private void setRateLimitResponse(@NotNull HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pw = response.getWriter();
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("code", 403);
        ret.put("message", "limit");
        pw.println(JacksonUtils.toJsonStr(ret));
        pw.flush();
    }

}
