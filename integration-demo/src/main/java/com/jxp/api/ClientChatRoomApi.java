package com.jxp.api;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.component.chatroom.client.NettyClient;
import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.enums.MsgActiveEnum;
import com.jxp.component.chatroom.enums.MsgStateEnum;
import com.jxp.component.chatroom.enums.MsgTypeEnum;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-01 15:35
 */
@Slf4j
@RestController
@RequestMapping("/client")
public class ClientChatRoomApi {

    @Resource
    private NettyClient nettyClient;


    @GetMapping("/send")
    @ResponseBody
    public ResponseEntity<Object> send(@RequestParam("userId") String userId,
            @RequestParam("msg") String msg) {
        nettyClient.send(Invocation.builder()
                .type(MsgTypeEnum.Chat.getCode())
                .state(MsgStateEnum.Sending.getCode())
                .active(MsgActiveEnum.Active.getCode())
                .uuid(IdUtil.fastSimpleUUID())
                .fromId("someone")
                .toId(userId)
                .content(msg)
                .timeStamp(System.currentTimeMillis())
                .build());
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/sendAll")
    @ResponseBody
    public ResponseEntity<Boolean> sendAll(@RequestParam("msg") String msg) {
        nettyClient.send(Invocation.builder()
                .type(MsgTypeEnum.Chat.getCode())
                .state(MsgStateEnum.Sending.getCode())
                .active(MsgActiveEnum.Active.getCode())
                .uuid(IdUtil.fastSimpleUUID())
                .fromId("someone")
                .toId("all")
                .content(msg)
                .timeStamp(System.currentTimeMillis())
                .build());
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
