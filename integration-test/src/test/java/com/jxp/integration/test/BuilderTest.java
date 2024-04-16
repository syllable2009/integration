package com.jxp.integration.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-12-06 15:39
 */
@Slf4j
public class BuilderTest {

    public static final String PROCESS_AUX_COMMAND = "ps aux --sort -rss";
    public static void main(String[] args) {
        executeCommand("ps -ef | grep 'java'");
        log.info("---------------------------");
        executeCommand(PROCESS_AUX_COMMAND);

        String s = StringUtils.substringAfterLast(
                "https://ssss.com/bs2/docsfile/spider/6220e8a80eb6407e86f59ae4dae4481cd", "/");
        log.info("s:{}", s);
    }


    public static void executeCommand(String command) {
        if (StringUtils.isBlank(command)) {
            return;
        }
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor(3, TimeUnit.SECONDS);
            logInputStream(process.getInputStream(), log::info);
        } catch (Exception e) {
            log.warn("execute cmd error. cmd:{}", command, e);
        }
    }

    public static void logInputStream(InputStream in, Consumer<String> logConsumer) throws IOException {
        if (in == null) {
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                logConsumer.accept(line);
            }
        } finally {
            try {
                br.close();
            } catch (IOException ignore) {
                log.info("logInputStream close exception");
            }
        }
    }
}
