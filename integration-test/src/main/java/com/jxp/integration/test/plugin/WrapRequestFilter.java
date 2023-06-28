package com.jxp.integration.test.plugin;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(urlPatterns = "/*", filterName = "wrapRequestFilter")
@Component
public class WrapRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("start WrapRequestFilter");
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        if (isFileUpload(request)) {
            filterChain.doFilter(request, wrappedResponse);
        } else {
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        }
        wrappedResponse.copyBodyToResponse();
    }

    private boolean isFileUpload(HttpServletRequest request) {
        return request.getContentType() != null
                && request.getContentType().equalsIgnoreCase("multipart/form-data");
    }
}
