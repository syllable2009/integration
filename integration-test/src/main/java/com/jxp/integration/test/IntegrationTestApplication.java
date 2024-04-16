package com.jxp.integration.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.jxp.integration.test.config.PlaywrightConfig;

//@EnableAdminServer
@ComponentScan(basePackages = "com.jxp")
@SpringBootApplication
public class IntegrationTestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(IntegrationTestApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                PlaywrightConfig.closeChromium();
            }
        });
    }
}
