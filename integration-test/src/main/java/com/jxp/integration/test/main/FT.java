package com.jxp.integration.test.main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-03-13 15:30
 */

@Slf4j
public class FT {


    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
//        Integer integer = supplyAsync();
//        log.info("result:{}", integer);
//        String s = flow2();
//        log.info("result:{}", s);
        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
            return f1 + ":" + f2 + "：哈哈";
        });
        log.info("result:{}", future.join());
    }

    public static void runAsync() {
        /**
         * 无返回值
         */
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        });
    }

    public static Integer supplyAsync() {
        /**
         * supplyAsync有返回值
         * whenComplete能感知异常，能感知结果，但没办法给返回值
         * exceptionally能感知异常，不能感知结果，能给返回值。相当于，如果出现异常就返回这个值
         * 异常是存在于异步当中的, 不能被主线程捕获
         */
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10 / 1;
            System.out.println("运行结果：" + i);
            return i;
        }).whenComplete((res, excption) -> {
            //whenComplete虽然能得到异常信息，但是没办法修改返回值
            System.out.println("whenComplete...结果是：" + res + ";异常是：" + excption);
            // return 9; 无法返回，会报错
        }).handle((res, excption) -> {
            //handle能拿到返回结果，也能得到异常信息，也能修改返回值
            System.out.println("handle...结果是：" + res + ";异常是：" + excption);
            if (excption != null) {
                return 0;
            } else {
                return res * 2;
            }
        }).exceptionally(throwable -> {
            //exceptionally能感知异常，而且能返回一个默认值，相当于，如果出现异常就返回这个值
            System.out.println("exception");
            return 10;
        });
        return future.join();
    }

    // 串行任务，第一个执行完执行第二个
    public static String flow2() throws InterruptedException, ExecutionException, TimeoutException {
        /**
         * thenApplyXXX 能接收上一次的执行结果，又可以有返回值
         * .thenApplyAsync(res -> {
         *      System.out.println("任务2启动了..." + res);
         *      return "hello " + res;
         *  }, executor);
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1开始当前线程" + Thread.currentThread().getId());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.out.println("任务1运行结果：" + 5);
            return 5;
        }).thenApplyAsync(res -> {
            System.out.println("任务2开始当前线程" + Thread.currentThread().getId());
            System.out.println("任务2得到的值..." + res);
            return "hello " + res;
        });

        return future.get(5, TimeUnit.SECONDS);
    }

   public static CompletableFuture<Object> future01 =CompletableFuture.supplyAsync(() -> {
        System.out.println("任务1线程开始" + Thread.currentThread().getId());
       System.out.println("当前线程名字" + Thread.currentThread().getId());
        int i = 10 / 4;
        System.out.println("任务1结束：");
        return i;
    });
    public static CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
        System.out.println("任务2线程开始" + Thread.currentThread().getId());
        System.out.println("当前线程名字" + Thread.currentThread().getId());
        try {
            Thread.sleep(3000);
            System.out.println("任务2结束：");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    });


    public static ThreadPoolExecutor getThreadPoolExecutor() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                availableProcessors,
                availableProcessors,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(9999),
                new ThreadFactoryBuilder().setNameFormat("custom-thread-pool-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
