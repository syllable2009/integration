package com.jxp.integration.test.api;

import javax.annotation.Resource;

import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.integration.test.spider.service.SpiderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 15:54
 */
@Slf4j
@RestController
@RequestMapping("/api/download")
public class DownloadController {

    @Resource
    SpiderService spiderService;

//    @Download(source = "classpath:/static/index.html")
    @GetMapping("/classpath")
    public void classpath(ServerHttpResponse response) {
    }

}
