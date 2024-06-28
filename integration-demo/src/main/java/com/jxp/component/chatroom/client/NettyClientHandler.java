package com.jxp.component.chatroom.client;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;

import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:27
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<Invocation> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("[Client][channelActive],id:{}", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("[Client][channelInactive],id:{}", ctx.channel().id());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Invocation invocation) throws Exception {
        log.info("[Client][channelRead0],invocation:{}", JSONUtil.toJsonStr(invocation));
    }

    /**
     *  * Netty 提供了 IdleStateHandler 处理器，提供空闲检测的功能，在 Channel 的读或者写空闲时间太长时，将会触发一个 IdleStateEvent 事件。
     *  * 这样，我们只需要在 NettyClientHandler 处理器中，在接收到 IdleStateEvent 事件时，客户端向服务端端发送一次心跳消息。
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("[Client][userEventTriggered],evt:{}", evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 这里可以进行相应的处理，比如发送心跳包或者关闭连接
                log.info("[Client][userEventTriggered]读超时");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("[Client][userEventTriggered]写超时");
                // 这里可以进行相应的处理，比如发送心跳包
                ctx.writeAndFlush(Invocation.builder()
                        .type("Heartbeat")
                        .message("Ping")
                        .build());
//                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("[Client][userEventTriggered]读写超时");
                // 这里可以进行相应的处理，比如发送心跳包或者关闭连接
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
