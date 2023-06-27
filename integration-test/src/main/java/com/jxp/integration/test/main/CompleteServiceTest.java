package com.jxp.integration.test.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-27 17:35
 */
@Slf4j
public class CompleteServiceTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testFuture();
//        testCompletionService();
        nChooseOne();
    }

    private static void nChooseOne() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        for (int i = 5; i > 0; i--) {
            completionService.submit(new Task(i));
        }
        String s = completionService.take()
                .get();
        log.info("it is return, result:{}", s);
        for (int i = 4; i > 0; i--){
            completionService.take().cancel(true);
        }

    }

    //结果的输出和线程的放入顺序 有关(如果前面的没完成，就算后面的哪个完成了也得等到你的牌号才能输出！)，so阻塞耗时
    public static void testFuture() throws InterruptedException, ExecutionException {
        long beg = System.currentTimeMillis();
        System.out.println("testFuture()开始执行：" + beg);
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> result = new ArrayList<Future<String>>();
        for (int i = 5; i > 0; i--) {
            Future<String> submit = executor.submit(new Task(i));
            result.add(submit);
        }
        executor.shutdown();
        for (int i = 0; i < 5; i++) {//一个一个等待返回结果
            Thread.sleep(500);
            System.out.println("线程" + i + "执行完成:" + result.get(i).get());
        }
        System.out.println("testFuture()执行完成:" + System.currentTimeMillis() + "," + (System.currentTimeMillis() - beg));
    }

    //结果的输出和线程的放入顺序 无关(谁完成了谁就先输出！主线程总是能够拿到最先完成的任务的返回值，而不管它们加入线程池的顺序)，so很大大缩短等待时间
    private static void testCompletionService() throws InterruptedException, ExecutionException {
        long beg = System.currentTimeMillis();
        System.out.println("testFuture()开始执行：" + beg);
        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        for (int i = 5; i > 0; i--) {
            completionService.submit(new Task(i));
        }
        executor.shutdown();
        for (int i = 0; i < 5; i++) {
            // 检索并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
            Future<String> future = completionService.take(); //这一行没有完成的任务就阻塞
            Thread.sleep(500);
            System.out.println("线程" + i + "执行完成:" + future.get());   // 这一行在这里不会阻塞，引入放入队列中的都是已经完成的任务
        }
        System.out.println("testFuture()执行完成:" + System.currentTimeMillis() + "," + (System.currentTimeMillis() - beg));
    }

    private static class Task implements Callable<String> {

        private volatile int i;
        private int s;

        public Task(int i) {
            s = RandomUtil.randomInt(2,10) * 1000;
            log.info("task call {} sleep {} ms", i, s);
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(s);
            return "任务 : " + this.i;
        }

    }


}
