package com.jxp.component.chatroom.handle;

import com.jxp.component.chatroom.codec.Invocation;

import io.netty.channel.Channel;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 21:54
 */
public interface MsgHandle {

    String getType();

    void execute(Channel channel, Invocation invocation);

}
