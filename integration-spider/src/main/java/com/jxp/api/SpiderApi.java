package com.jxp.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.dto.bo.SingleAddressResp;
import com.jxp.dto.bo.SpiderTaskResp;
import com.jxp.dto.bo.TaskAddressReq;
import com.jxp.service.SpiderApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:16
 */

@Slf4j
@RestController
public class SpiderApi {

    @Resource
    private SpiderApiService spiderApiService;

    @PostMapping("/parse")
    public ResponseEntity<SingleAddressResp> parseSingleAddress(@Validated @RequestBody SingleAddressReq req) {
        return ResponseEntity.ok(spiderApiService.parse(req, "zhangsan01"));
    }

    @PostMapping("/task/parse")
    public ResponseEntity<SpiderTaskResp> taskParse(@Validated @RequestBody TaskAddressReq req) {
        return ResponseEntity.ok(spiderApiService.taskParseRun(req, "zhangsan01"));
    }

    @PostMapping("/download")
    public ResponseEntity<String> download(@Validated @RequestBody SingleAddressReq req) {
        return ResponseEntity.ok(spiderApiService.downloadFile(req, "zhangsan01"));
    }
}
