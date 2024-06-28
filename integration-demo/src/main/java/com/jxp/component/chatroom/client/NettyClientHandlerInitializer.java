package com.jxp.component.chatroom.client;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.codec.InvocationDecoder;
import com.jxp.component.chatroom.codec.InvocationEncoder;
import com.jxp.component.chatroom.handle.MessageDispatcher;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:25
 */

@Slf4j
@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {

    @Resource
    private MessageDispatcher messageDispatcher;
    @Resource
    private NettyClientHandler nettyClientHandler;

    private static final Integer READ_TIMEOUT_SECONDS = 10;

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                // 空闲检测
                .addLast(new IdleStateHandler(5, 5, 10))
                .addLast(new InvocationEncoder())
                .addLast(new InvocationDecoder())
//                .addLast(messageDispatcher)
                // 客户端处理器
                .addLast(nettyClientHandler);
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
        log.info("[userEventTriggered],evt:{}", evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读超时");
                // 这里可以进行相应的处理，比如发送心跳包或者关闭连接
                log.info("[userEventTriggered]发送一次心跳");
                ctx.writeAndFlush(Invocation.builder()
                                .type("Heartbeat")
                                .message("Ping")
                                .build())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("写超时");
                // 这里可以进行相应的处理，比如发送心跳包
            } else if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("读写超时");
                // 这里可以进行相应的处理，比如发送心跳包或者关闭连接
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
