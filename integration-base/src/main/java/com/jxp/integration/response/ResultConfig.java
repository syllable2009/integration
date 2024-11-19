package com.jxp.integration.response;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-11-19 11:01
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "result", name = "enable", havingValue = "true")
public class ResultConfig {

    private ApplicationContext applicationContext;


    @Autowired
    public ResultConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        scanAndHandleAnnotatedMethods();
    }

    private void scanAndHandleAnnotatedMethods() {
        final String[] beanNamesForType = applicationContext.getBeanNamesForType(ResultCode.class);
        Arrays.stream(beanNamesForType)
                .map(applicationContext::getBean)
                .filter(bean -> bean instanceof ResultCode)
                .forEach(e -> {
                    Arrays.stream(e.getClass().getFields())
                            .filter(f -> f.isAnnotationPresent(ExceptionCode.class))
                            .forEach(f -> {
                                ExceptionCode a = f.getAnnotation(ExceptionCode.class);
                                Result.I18N_MSG_MAP.put(a.code(), I18nVo.builder()
                                        .enUS(a.enUS())
                                        .zhCN(a.zhCN())
                                        .build());
                            });
                });
    }
}
