package com.jxp.integration.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;

/**
 * @author jiaxiaopeng
 * Created on 2023-10-24 16:18
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Integration-test")
                        .description("Just for fun")
                        .version("v1.0.0")
                        .license(new License()
                                .name("许可协议")
                                .url("localhost:8081"))
                        .contact(new Contact()
                                .name("syllable")
                                .email("123@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("测试")
                        .url("localhost:8081"));
    }
}
