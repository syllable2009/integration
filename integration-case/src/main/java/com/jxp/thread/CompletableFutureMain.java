package com.jxp.thread;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-15 10:01
 */
@SuppressWarnings({"checkstyle:ConstantName", "checkstyle:MagicNumber", "checkstyle:RegexpSingleline"})
@Slf4j
public class CompletableFutureMain {

    static final CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
        ThreadUtil.sleep(3000L);
        log.info("3秒异步无返回任务");
    });

    static final CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> {
        ThreadUtil.sleep(5000L);
        log.info("5秒异步无返回任务");
    });

    static final CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> {
        ThreadUtil.sleep(3000L);
        log.info("3秒异步返回cf3任务");
        return "cf3";
    });

    static final CompletableFuture<String> cf4 = CompletableFuture.supplyAsync(() -> {
        ThreadUtil.sleep(5000L);
        log.info("5秒异步返回cf4任务");
        return "cf4";
    });

    static final CompletableFuture<String> cf5 = CompletableFuture.supplyAsync(() -> {
        String str = null;
        return str.toLowerCase();
    });

    static final CompletableFuture<Void> main1 = CompletableFuture.runAsync(() -> {
        log.info("main1无返回任务");
    });

    static final CompletableFuture<String> main2 = CompletableFuture.supplyAsync(() -> {
        log.info("main2有返回任务");
        return "main2";
    });

    static final Consumer<String> consumer = str -> {
        log.info("consumer,param:{}", str);
    };

    static final Function<String, String> func = i -> {
        log.info("func,param:{}", i);
        return "func:" + i;
    };

    @SuppressWarnings("checkstyle:LocalFinalVariableName")
    public static void main(String[] args) throws Exception {
        log.info("******************");
        final CompletableFuture get1 = cf3.acceptEither(cf4, consumer);
        get1.get();
        log.info("******************");
        final CompletableFuture<String> get2 = cf3.applyToEitherAsync(cf4, func);
        log.info("get2:{}", get2.get());
        log.info("******************");

        final String collect = Stream.of(cf3, cf4)
                .map(CompletableFuture::join)
                .collect(Collectors.joining("|"));
        log.info("collect:{}", collect);
        log.info("******************");
        CompletableFuture.allOf(cf3, cf4, cf5)
                .exceptionally(e -> {
                    log.info("ex:{}", e.getMessage());
                    return null;
                })
                .whenComplete((r, e) -> {
                    if (null != e) {
                        log.info("ec:{}", e.getMessage());
                    } else {
                        log.info("r:{}", r);
                    }

                })
                .get();

        // 原始流的每个元素通过映射函数转换后，形成一个新的流。
        List<String> words = Arrays.asList("hello", "world");
        final Stream<String> stringStream = words.stream()
                .map(s -> s.toLowerCase());

        List<List<String>> listOfLists = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("d", "e", "f"),
                Arrays.asList("g", "h", "i")
        );

        //原始流的每个元素通过映射函数转换后，形成多个流，这些流会被合并成一个单一的流。
        final Stream<String> stringStream1 = listOfLists.stream()
                .flatMap(List::stream);

        final FileChannel fileChannel = FileChannel.open(Path.of("./test.txt"), StandardOpenOption.READ);
        long fileSize = fileChannel.size();
        final long position = fileChannel.position();

        int numberOfWorkers = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfWorkers];
        final int MAX_CITIES = 10000;
        for (int i = 0; i < threads.length; ++i) {
            final int index = i;
            threads[i] = new Thread(() -> {
//                List<Result> results = new ArrayList<>(MAX_CITIES);
//                parseLoop(cursor, fileEnd, fileStart, results);
//                allResults[index] = results;
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

    }
}
