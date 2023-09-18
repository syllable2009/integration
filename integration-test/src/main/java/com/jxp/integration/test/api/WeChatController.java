package com.jxp.integration.test.api;

import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/getToken")
    public ResponseEntity<Object> getToken() {
        // 测试地址:https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
        AccessTokenResult accessToken = weChatService
                .getAccessToken("client_credential", "wxc56760e99e4366d8", "fb0b37b2183b5424966d90e6f42d0191");
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok(true);
    }

    @GetMapping("/testPost")
    public ResponseEntity<Object> testPost() {
        String url = "https://tiger-api.helloworld.net/v1/home/getHomeBlogListByTag?action=10&pageNum=1&pageSize=10&uuid=51380297";
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String s = restTemplate.postForObject(url, request, String.class);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/testGet")
    public ResponseEntity<Object> testGet() {
        return ResponseEntity.ok(true);
    }

}
