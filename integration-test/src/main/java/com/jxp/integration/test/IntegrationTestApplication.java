package com.jxp.integration.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@EnableAdminServer
@ComponentScan(basePackages = "com.jxp")
@SpringBootApplication
public class IntegrationTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationTestApplication.class, args);
    }

}
