package com.jxp.component.chatroom.handle;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.enums.MsgTypeEnum;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 21:54
 */
@Slf4j
@Component
public class HeartbeatHandle implements MsgHandle {
    @Override
    public String getType() {
        return MsgTypeEnum.Heartbeat.getCode();
    }

    @Override
    public void execute(Channel channel, Invocation invocation) {
        log.info("[Server][HeartbeatHandle][execute],invocation:{}", invocation);
        channel.writeAndFlush(Invocation.builder()
                .type("Heartbeat")
                .content("Pong")
                .build());
    }
}
