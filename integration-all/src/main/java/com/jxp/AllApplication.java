package com.jxp;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.jxp.integration.response.Result;

import cn.hutool.core.thread.ThreadUtil;

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


    @GetMapping("de")
    public DeferredResult<Result<String>> deferredResult(@RequestParam("time") Long time,
            HttpServletResponse response) {
//        response.setStatus(HttpStatus.OK.value());
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=utf-8");
        DeferredResult<Result<String>> deferredResult = new DeferredResult<>(10000L, Result.ok(
                "2333 time out"));
        new Thread(() -> {
            ThreadUtil.sleep(time);
            deferredResult.setResult(Result.ok("yes yes "));
        }).start();

        return deferredResult;
    }
}