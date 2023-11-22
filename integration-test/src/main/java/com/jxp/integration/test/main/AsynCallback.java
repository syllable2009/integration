package com.jxp.integration.test.main;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-10-24 11:29
 * CompletableFuture提供了方法大约有50多个，单纯一个个记忆，是很麻烦的，因此将其划分为以下几类：
 * 创建类
 * <p>
 * completeFuture 可以用于创建默认返回值
 * runAsync 异步执行，无返回值
 * supplyAsync 异步执行，有返回值
 * anyOf 任意一个执行完成，就可以进行下一步动作
 * allOf 全部完成所有任务，才可以进行下一步任务
 * <p>
 * 状态取值类
 * <p>
 * join 合并结果，等待
 * get 合并等待结果，可以增加超时时间;get和join区别，join只会抛出unchecked异常，get会返回具体的异常
 * getNow 如果结果计算完成或者异常了，则返回结果或异常；否则，返回valueIfAbsent的值
 * isCancelled
 * isCompletedExceptionally
 * isDone
 * <p>
 * 控制类 用于主动控制CompletableFuture的完成行为
 * <p>
 * complete
 * completeExceptionally
 * cancel
 * <p>
 * 接续类 CompletableFuture 最重要的特性，没有这个的话，CompletableFuture就没意义了，用于注入回调行为。
 * <p>
 * thenApply, thenApplyAsync
 * thenAccept, thenAcceptAsync
 * thenRun, thenRunAsync
 * thenCombine, thenCombineAsync
 * thenAcceptBoth, thenAcceptBothAsync
 * runAfterBoth, runAfterBothAsync
 * applyToEither, applyToEitherAsync
 * acceptEither, acceptEitherAsync
 * runAfterEither, runAfterEitherAsync
 * thenCompose, thenComposeAsync
 * whenComplete, whenCompleteAsync
 * handle, handleAsync
 * exceptionally
 * <p>
 * 上面的方法很多，我们没必要死记硬背，按照如下规律，会方便很多，记忆规则：
 * <p>
 * 以Async结尾的方法，都是异步方法，对应的没有Async则是同步方法，一般都是一个异步方法对应一个同步方法。
 * 以Async后缀结尾的方法，都有两个重载的方法，一个是使用内容的forkjoin线程池，一种是使用自定义线程池
 * 以run开头的方法，其入口参数一定是无参的，并且没有返回值，类似于执行Runnable方法。
 * 以supply开头的方法，入口也是没有参数的，但是有返回值
 * 以Accept开头或者结尾的方法，入口参数是有参数，但是没有返回值
 * 以Apply开头或者结尾的方法，入口有参数，有返回值
 * 带有either后缀的方法，表示谁先完成就消费谁
 */

@Slf4j
public class AsynCallback {

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        // 同步场景下匿名内部类或Lambda的方式实现回调
        //        performAction(s -> System.out.println(s));
        // 异步回调实例
        //        Runnable runnable = () -> System.out.println("回调代码被执行");
        //        AsynCallback.performAsynchronousAction(runnable);
        MyThread1 t1 = new MyThread1();
        t1.start();
        // 创建一个线程，并将任务提交给线程执行
        MyThread2 t2 = new MyThread2();
        Thread thread = new Thread(t2);
        thread.start();
        //jdk1.0但这两种方式创建的线程是属于<三无产品>：没有参数 没有返回值 没办法抛出异常
        // jdk1.5出现了Callable 是一个泛型接口
        Callable<String> callable = () -> {
            // Perform some computation
            //            Thread.sleep(2000);
            return "Return some result";
        };

        Future<String> submit = executorService.submit(callable);


        //        try {
        //            // 阻塞
        //            String s = submit.get();
        //        } catch (InterruptedException interruptedException) {
        //            interruptedException.printStackTrace();
        //        } catch (ExecutionException e) {
        //            e.printStackTrace();
        //        }

        // 包含了接口的所有功能，将runnable的实现了返回值
        //        FutureTask futureTask;

        // jdk1.8
        // CompletionService
        //        ExecutorService executorService = Executors.newCachedThreadPool();
        //        CompletionService csRef = new ExecutorCompletionService(executorService);
        // CompletableFuture
        //        在Java 8中, 新增加了一个包含50个方法左右的类:
        //        CompletableFuture，结合了Future的优点，提供了非常强大的Future
        //        的扩展功能，可以帮助我们简化异步编程的复杂性，提供了函数式编程的能力，可以通过回调的方式处理计算结果，并且提供了转换和组合CompletableFuture的方法。

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("do something....");
            return "result";
        }, executorService);
        //        cf1.join();
        //        cf1.get();
        CompletableFuture<String> cf2 = cf1.thenApplyAsync((result) -> {
            System.out.println(Thread.currentThread() + " cf2 do something....");
            result += 2;
            return result;
        });

        log.info("cf1:{},cf2:{}", cf1.join(), cf2.join());

    }


    public static void m1() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            System.out.println("电饭煲开始做饭");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "白米饭";
        }).thenAccept(result -> {
            System.out.println("开始吃米饭");
        });

        System.out.println("我先去搞点牛奶和鸡蛋");
        future.join();
    }

    public static void performAction(Consumer<String> consumer) {
        System.out.println("执行特定的业务逻辑");
        consumer.accept("回调代码被执行");
    }

    public static void performAsynchronousAction(Runnable runnable) {
        System.out.println("执行特定的业务逻辑");
        new Thread(() -> {
            System.out.println("执行异步操作代码");
            runnable.run();
        }).start();
    }


    static class MyThread1 extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println("MyThread1");
        }
    }

    static class MyThread2 implements Runnable {

        @Override
        public void run() {
            System.out.println("MyThread2");
        }
    }

}
