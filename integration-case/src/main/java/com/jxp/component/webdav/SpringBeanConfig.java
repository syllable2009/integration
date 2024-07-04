package com.jxp.component.webdav;


import javax.servlet.Filter;

import org.springframework.context.annotation.Configuration;

import io.milton.servlet.SpringMiltonFilter;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-03 17:57
 */

@Configuration
public class SpringBeanConfig {

//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(getMiltonFilter());
//        registration.setName("MiltonFilter");
//        registration.addUrlPatterns("/webdav/*"); // ant为/webdav/**
////        registration.addInitParameter("milton.exclude.paths", "/myExcludedPaths,/moreExcludedPaths");
//        registration.addInitParameter("resource.factory.class",
//                "io.milton.http.annotated.AnnotationResourceFactory");
//        registration.addInitParameter("controllerPackagesToScan",
//                "com.jxp.component.webdav.controller");
////        registration.addInitParameter("milton.configurator",
////                "com.jxp.component.webdav.MiltonConfig");
//        registration.setOrder(1);
//        return registration;
//    }

    public Filter getMiltonFilter() {
        return new SpringMiltonFilter();
    }
}
