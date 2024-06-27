package com.jxp.component.chatroom;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;


/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 16:42
 */

@Slf4j
@Component
@ChannelHandler.Sharable // 注解，标记这个 ChannelHandler 可以被多个 Channel 使用
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Resource
    private NettyChannelManager channelManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            // Reply to client
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Received your message -> " + textWebSocketFrame.text()));
        } else {
            // Invalid message type
            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
        }
    }

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



}
