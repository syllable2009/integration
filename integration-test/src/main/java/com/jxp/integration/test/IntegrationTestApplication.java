package com.jxp.integration.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@ComponentScan(basePackages = "com.jxp")
@SpringBootApplication
public class IntegrationTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationTestApplication.class, args);
    }

}
