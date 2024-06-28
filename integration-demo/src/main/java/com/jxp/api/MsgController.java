package com.jxp.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.component.chatroom.client.NettyClient;
import com.jxp.component.chatroom.codec.Invocation;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-28 15:12
 */
@Slf4j
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private NettyClient nettyClient;

    @GetMapping(value = "/send")
    public ResponseEntity<Boolean> send(@RequestParam(value = "type", required = false,
            defaultValue = "Chat") String type,
            @RequestParam("msg") String msg) {

        nettyClient.send(Invocation.builder()
                .type(type)
                .message(msg)
                .build());
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
