package com.jxp.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-12 11:09
 */

@Slf4j
public class ThreadDemo {


    public static void main(String[] args) throws Exception {
        MyThread t1 = new MyThread();
        t1.start();

        MyRunnable myRunnable = new MyRunnable();
        Thread t2 = new Thread(myRunnable);
        t2.start();

        MyCallable myCallable = new MyCallable();
        // 要获取返回值，借助Future来执行
        FutureTask<String> futureTask = new FutureTask(myCallable);
        String result = futureTask.get();
        log.info("result:{}", result);
        // 或者借助线程池来执行
        Future<String> submit = Executors.newFixedThreadPool(1)
                .submit(myCallable);
        String result2 = submit.get();
        log.info("result2:{}", result2);
    }

    static class MyThread extends Thread {
        public void run() {
            System.out.println("MyThread is running");
        }
    }

    static class MyRunnable implements Runnable {
        public void run() {
            System.out.println("MyRunnable is running");
        }
    }

    static class MyCallable implements Callable<String> {
        public String call() throws Exception {
            log.info("log MyCallable is running");
            return "MyCallable is running";
        }
    }
}
