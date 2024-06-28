package com.jxp.component.chatroom.server;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 16:42
 */

@Slf4j
@Component
@ChannelHandler.Sharable // 注解，标记这个 ChannelHandler 可以被多个 Channel 使用
public class NettyServerHandler extends SimpleChannelInboundHandler<Invocation> {

    @Resource
    private NettyChannelManager channelManager;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channelManager.remove(ctx.channel());
        log.info("channelInactive,remoteAddress:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channelManager.add(ctx.channel());
        log.info("channelActive,remoteAddress:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        // 从管理器中移除
        channelManager.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Invocation invocation) throws Exception {
        log.info("channelRead0:{}", invocation.getMessage());
    }
}
