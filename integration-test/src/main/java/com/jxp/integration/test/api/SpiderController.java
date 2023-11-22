package com.jxp.integration.test.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SingleAddressResp;
import com.jxp.integration.test.spider.domain.dto.SpiderTaskResp;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;
import com.jxp.integration.test.spider.service.SpiderService;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 15:54
 */
@Slf4j
@RestController
@RequestMapping("/api/spider")
@Tag(name = "SpiderController", description = "爬虫api")
public class SpiderController {

    @Resource
    SpiderService spiderService;

    @Operation(summary = "单地址解析", description = "用于解析单个地址")
    @ApiOperation(value = "单地址解析")
    @PostMapping("/parse")
    public ResponseEntity<SingleAddressResp> parse(@Validated @RequestBody SingleAddressReq req) {
        return ResponseEntity
                .ok(spiderService.parse(req));
    }

    @ApiOperation(value = "任务列表地址解析")
    @PostMapping("/task/parse")
    public ResponseEntity<SpiderTaskResp> taskParse(@Validated @RequestBody RecommendCrawlerTaskData req) {
        return ResponseEntity.ok(spiderService.taskSpiderRun(req));
    }

    public static void main(String[] args) {
        String domain = UrlUtils.getDomain("https://github.com/trending/java");
        log.info("domain:{}",domain);
    }

}
