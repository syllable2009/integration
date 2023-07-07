package com.jxp.integration.test.plugin;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 11:40
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@Slf4j
@Component
public class IdentifyInterceptor extends OncePerRequestFilter {

    public static final String DEBUG_TEST_USER_KEY = "testUserId";

    public static final String ANONYMOUS_USER_KEY = "I-Token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("IdentifyInterceptor");

        // 优先级 testUserId > 登录 > 匿名
        // 先要判断环境
        String env = "test";
        if (StrUtil.equalsAny(env, "test", "dev", "local")) {
            Context context = buildDebugContext(request);
            if (null != context) {
                RequestContext.setRequestContext(context);
                filterChain.doFilter(request, response);
                return;
            }
        }
        // 构建登录用户，这里缓存信息

        // 构建匿名用户
        Context context = buildAnonymousContext(request);
        if (null != context) {
            RequestContext.setRequestContext(context);
            filterChain.doFilter(request, response);
            return;
        }

        if (null == context) {
            RequestContext.setRequestContext(Context.builder()
                    .userId("Unkown")
                    .build());
        }
        filterChain.doFilter(request, response);
    }

    private static Context buildDebugContext(HttpServletRequest request) {
        String testUserId = request.getParameter(DEBUG_TEST_USER_KEY);
        if (StrUtil.isBlank(testUserId)) {
            return null;
        }
        return Context.builder()
                .userId(testUserId)
                .build();
    }

    private Context buildAnonymousContext(HttpServletRequest request) {
        String anonymousToken = getAnonymousToken(request);
        if (StrUtil.isBlank(anonymousToken)) {
            return null;
        }
        return Context.builder()
                .userId(anonymousToken)
                .anonymous(true)
                .build();
    }


    private static String getAnonymousToken(HttpServletRequest request) {
        Cookie cookie = ServletUtil.getCookie(request, ANONYMOUS_USER_KEY);
        if (cookie != null) {
            return cookie.getValue();
        }
        return request.getHeader(ANONYMOUS_USER_KEY);
    }
}
