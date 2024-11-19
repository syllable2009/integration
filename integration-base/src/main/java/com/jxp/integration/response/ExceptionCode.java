package com.jxp.integration.response;

/**
 * @author jiaxiaopeng
 * Created on 2024-11-19 10:56
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // 可以用于方法和类
@Retention(RetentionPolicy.RUNTIME) // 在运行时可用
public @interface ExceptionCode {

    int code();

    String message();

    String zhCN();

    String enUS();
}
