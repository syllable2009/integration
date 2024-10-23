package com.jxp.api;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jxp.commonjson.Block;
import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.enums.MsgActiveEnum;
import com.jxp.component.chatroom.enums.MsgStateEnum;
import com.jxp.component.chatroom.enums.MsgTypeEnum;
import com.jxp.component.chatroom.server.NettyChannelManager;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-07-01 15:35
 */
@Slf4j
@RestController
@RequestMapping("/server")
public class ServerChatRoomApi {

    @GetMapping("/onlineList")
    @ResponseBody
    public ResponseEntity<Object> onlineList() {
        final Set<String> userIds = NettyChannelManager.USER_CHANNEL_MAP.keySet();
        return ResponseEntity.ok(userIds);
    }

    @GetMapping("/send")
    @ResponseBody
    public ResponseEntity<Object> send(@RequestParam("userId") String userId,
            @RequestParam("msg") String msg) {
        NettyChannelManager.send(userId, Invocation.builder()
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
    public ResponseEntity<Object> sendAll(
            @RequestParam("msg") String msg) {
        NettyChannelManager.sendAll(Invocation.builder()
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

    @PostMapping("/jsonArray")
    @ResponseBody
    public ResponseEntity<Object> jsonArray(@RequestBody List<Block> blockList) {
        return ResponseEntity.ok(blockList);
    }
}
