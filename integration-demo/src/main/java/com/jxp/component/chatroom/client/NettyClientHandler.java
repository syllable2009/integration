package com.jxp.component.chatroom.client;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.AuthMessageContent;
import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.handle.MsgHandle;
import com.jxp.component.chatroom.handle.MsgHandleContainer;
import com.jxp.util.NettyAttrUtil;
import com.jxp.util.SpringBeanFactory;

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

    @Resource
    private MsgHandleContainer msgHandleContainer;
    private static Long HEARTBEAT_INTERVAL = 10 * 3 * 1000L;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("[Client][channelActive],id:{}", ctx.channel().id());
        // 模拟客户端来认证
        log.info("[Client][模拟用户身份认证],id:{}", ctx.channel().id());
        ctx.writeAndFlush(Invocation.builder()
                .type("Auth")
                .content(JSONUtil.toJsonStr(AuthMessageContent.builder()
                        .userId("jiaxiaopeng")
                        .password("******")
                        .token("12345678910")
                        .build()))
                .build());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("[Client][channelInactive],id:{}", ctx.channel().id());
        // 一旦服务端 down 机、或者是与客户端的网络断开则会回调客户端的 channelInactive 事件。
        // IdleStateHandler会把之前开启的定时任务都给取消掉。所以就不会再有任何的定时任务执行了，也就不会有机会执行这个重连业务。
        // 得有一个单独的线程来判断是否需要重连，不依赖于 IdleStateHandler
        final NettyClient nettyClient = SpringBeanFactory.getBean(NettyClient.class);
        nettyClient.reconnect(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) throws Exception {
        log.info("[Client][channelRead0],invocation:{}", JSONUtil.toJsonStr(invocation));
        NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
        final MsgHandle messageHandler = msgHandleContainer.getMessageHandler(invocation.getType());
        messageHandler.execute(ctx.channel(), invocation);
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
                // 多久没有读到server就重连，很多场景下为了效率，服务器不响应ping消息
                final Long readerTime = NettyAttrUtil.getReaderTime(ctx.channel());
                final long now = System.currentTimeMillis();
                if (now - readerTime > HEARTBEAT_INTERVAL) {
                    final NettyClient nettyClient = SpringBeanFactory.getBean(NettyClient.class);
                    nettyClient.reconnect(false);
                }
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
