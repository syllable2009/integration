package com.jxp.customer.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:47
 */
@Slf4j
@Service
public class Jedis {

    private static Jedis jedis = new Jedis();

    public static Jedis get() {
        return jedis;
    }

    public static String set(String var1, String var2, String var3, String var4, long var5) {
        return null;
    }

    public static void hmset(String var1, Map<String, String> var2) {
    }

    public static String hmget(String var1, Map<String, String> var2) {
        return null;
    }

    public static List<String> hmget(String var1, String... var2) {
        return null;
    }

    public static Map<String, String> hgetAll(String var1) {
        return null;
    }

    public static Long del(String var1) {
        return 1L;
    }
}
