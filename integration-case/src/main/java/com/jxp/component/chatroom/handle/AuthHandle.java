package com.jxp.component.chatroom.handle;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 21:53
 */

@Slf4j
@Component
public class AuthHandle implements MsgHandle {
    @Override
    public String getType() {
        return "Auth";
    }

    @Override
    public void execute(Channel channel, String message) {
        log.info("[Server][AuthHandle][execute],message:{}", message);
    }
}
