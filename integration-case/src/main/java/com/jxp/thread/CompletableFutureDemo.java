package com.jxp.thread;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.jxp.thread.ThreadDemo.MyCallable;
import com.jxp.thread.ThreadDemo.MyRunnable;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-12 11:50
 */

@Slf4j
public class CompletableFutureDemo {

    @SuppressWarnings("checkstyle:MagicNumber")
    public static void main(String[] args) throws Exception {
        MyRunnable myRunnable = new MyRunnable();

        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(myRunnable);
        voidCompletableFuture.get();
        Supplier<String> supplier = () -> {
            return String.valueOf(RandomUtil.randomChinese());
        };
        final CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(supplier);
        log.info("stringCompletableFuture:{}", stringCompletableFuture.get());

        MyCallable myCallable = new MyCallable();
        BiFunction<String, String, String> bf = (a, b) -> {
            return "";
        };
        CompletableFuture.supplyAsync(() -> {
                    return 1;
                })
                .whenComplete((r, e) -> {
                    log.info("e:{},r:{}", e, r);
                }).exceptionally(e -> {
                    return -1;
                });




    }
}
