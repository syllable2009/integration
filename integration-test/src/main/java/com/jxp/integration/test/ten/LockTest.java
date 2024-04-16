package com.jxp.integration.test.ten;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-03-04 10:53
 */
@Slf4j
public class LockTest {

    private static Lock lock = new ReentrantLock();
    private final static Condition condition = lock.newCondition();


    public static void main(String[] args) throws InterruptedException {
        //        Thread t1 = new Thread(() -> {
        //            acquireLock();
        //        });
        //
        //        Thread t2 = new Thread(() -> acquireLock());
        //
        //        t1.start();
        //        t2.start();


        Thread t3 = new Thread(() -> {
            doSomething();
            notifyThread();
        });
        Thread t4 = new Thread(() -> {
            notifyThread();
        });

        t3.start();
        t4.start();
    }


    private static void acquireLock() {
        lock.lock();
        try {
            log.info("{} 获取到了锁", Thread.currentThread().getName());
            // 执行同步的代码块
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("{} 释放了锁", Thread.currentThread().getName());
            lock.unlock(); // 释放锁
        }
    }


    public static void doSomething() {
        lock.lock();
        try {
            log.info("{} 获取到了锁", Thread.currentThread().getName());
            Thread.sleep(2000);
            // 等待条件
            condition.await();
            // 执行其他操作
            log.info("{} 执行其他操作", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void notifyThread() {
        lock.lock();
        try {
            log.info("{} 获取到了锁", Thread.currentThread().getName());
            Thread.sleep(2000);
            // 唤醒线程
            log.info("{} 唤醒线程", Thread.currentThread().getName());
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
