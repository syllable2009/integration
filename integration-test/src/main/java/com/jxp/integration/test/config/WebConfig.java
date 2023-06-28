package com.jxp.integration.test.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.jxp.integration.test.plugin.AccessLogInterceptor;
import com.jxp.integration.test.plugin.IdentifyInterceptor;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 10:01
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AccessLogInterceptor accessLogInterceptor;
    @Resource
    private IdentifyInterceptor identifyInterceptor;

    private static final List<String> EXCLUDE_PATH_PATTERNS = Arrays.asList(
            "/",
            "/doc.html",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources",
            "/favicon.ico",
            "/health",
            "/error"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accessLogInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
        registry.addInterceptor(identifyInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }


    /**
     * i18n获取多语言设置的bean
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.CHINA);  //默认地区
        cookieLocaleResolver.setCookieName("lang");   //cookie中取的地区的key
        return cookieLocaleResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
