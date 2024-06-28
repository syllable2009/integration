package com.jxp.component.chatroom.handle;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 22:00
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Resource
    private MsgHandleContainer msgHandleContainer;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) throws Exception {
        // <3.1> 获得 type 对应的 MessageHandler 处理器
        final MsgHandle messageHandler = msgHandleContainer.getMessageHandler(invocation.getType());
        final String message = invocation.getMessage();
        messageHandler.execute(ctx.channel(), message);
    }
}
