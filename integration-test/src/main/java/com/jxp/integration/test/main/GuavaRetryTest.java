package com.jxp.integration.test.main;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-08 10:34
 */

@Slf4j
public class GuavaRetryTest {


    public static void main(String[] args) {
//        Function<Object, Object> objectObjectFunction = Functions.forMap(Maps.newHashMap());
//        objectObjectFunction.apply("");
        //        guavaRetry(2);
        log.info(":::::{}","本文是关于我开发工程师必备的异常排查技能的摘要。文章介绍了四种内存分析工具的评测结".length());
    }


    public static String guavaRetry(Integer num) {
        Retryer<String> retryer = RetryerBuilder.<String> newBuilder()
                //无论出现什么异常，都进行重试
                .retryIfException()
                //返回结果为 error时，进行重试
                .retryIfResult(result -> Objects.equals(result, "error"))
                //重试等待策略：等待 2s 后再进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //重试停止策略：重试达到 3 次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("RetryListener: 第" + attempt.getAttemptNumber() + "次调用");
                    }
                })
                .build();
        try {
            retryer.call(() -> testGuavaRetry(num));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }

    public static String testGuavaRetry(Integer num){
        return "error";
    }
}
