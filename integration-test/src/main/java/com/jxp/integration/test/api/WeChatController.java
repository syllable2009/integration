package com.jxp.integration.test.api;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.integration.wechat.model.AccessTokenResult;
import com.jxp.integration.wechat.service.WeChatService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 19:22
 */
@Slf4j
@RestController
@RequestMapping("/api/wechat")
public class WeChatController {

    @Resource
    WeChatService weChatService;
    @Resource
    Environment environment;

    @GetMapping("/getToken")
    public ResponseEntity<Object> getToken() {
        // 测试地址:https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
        AccessTokenResult accessToken = weChatService
                .getAccessToken("client_credential", "wxc56760e99e4366d8", "fb0b37b2183b5424966d90e6f42d0191");
        return ResponseEntity.ok(accessToken);
    }

}
