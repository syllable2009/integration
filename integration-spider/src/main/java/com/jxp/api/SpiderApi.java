package com.jxp.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.dto.bo.SingleAddressResp;
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
        return ResponseEntity.ok(null);
    }
}
