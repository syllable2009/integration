package com.jxp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 14:54
 */
@RestController
@SpringBootApplication
public class AllApplication {
    public static void main(String[] args) {
        SpringApplication.run(AllApplication.class, args);
    }

    @GetMapping(value = {"/", "/health"})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}