package com.jxp.integration.test.main;

import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-10-19 14:55
 */
@Slf4j
public class SpringBootTest {

    public static void main(String[] args) {
        Class<?> aClass = classLoader("com.jxp.integration.test.main.HtmlUnit", null);
        log.info("aClass:{}", aClass);
        Class<?> bClass = classLoader("com.jxp.integration.base.tool.EnvUtils", null);
        log.info("bClass:{}", bClass);
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        log.info("stackTrace:{}",stackTrace);
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        // 默认的11个监听器
        LoggingApplicationListener loggingApplicationListener = new LoggingApplicationListener();
        simpleApplicationEventMulticaster.addApplicationListener(loggingApplicationListener);
        ApplicationContext ap = new AnnotationConfigApplicationContext();
        simpleApplicationEventMulticaster.multicastEvent(new ContextStartedEvent(ap));
    }

    public static Class<?> classLoader(String className, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            log.error("classLoader fail,className:{}", className, e);
        }
        return null;
    }

    @Nullable
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
        }

        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                }
            }
        }

        return cl;
    }
}
