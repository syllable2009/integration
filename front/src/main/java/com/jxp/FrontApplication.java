package com.jxp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import xyz.erupt.core.annotation.EruptScan;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-02 11:32
 */
@EruptScan
@Slf4j
@SpringBootApplication
public class FrontApplication {
    public static void main(String[] args) {
        final String property = System.getProperty("user.dir");
        log.info("user.dir:{}", property);
        SpringApplication.run(FrontApplication.class, args);
    }
}