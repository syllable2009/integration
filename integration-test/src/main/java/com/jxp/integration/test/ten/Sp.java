package com.jxp.integration.test.ten;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-02-27 10:35
 */

@Slf4j
public class Sp {
    public static void main(String[] args) throws ClassNotFoundException {
        List<? extends Integer> objects = new ArrayList<>();


        Class<?> aClass = printStackTrace();
        log.info("class:{}",aClass.toString());

    }

    public static Class<?> printStackTrace() throws ClassNotFoundException {
        StackTraceElement[] stackTrace = (new RuntimeException()).getStackTrace();

        for (StackTraceElement e: stackTrace){
            log.info("stackTrace:{}#{}",e.getClassName(),e.getMethodName());
            if ("main".equals(e.getMethodName())) {
                return Class.forName(e.getClassName());
            }
        }
        return null;
    }

    public static <T, R> R test1(T t, R i) {
        System.out.println(t);
        return i;
    }

}
