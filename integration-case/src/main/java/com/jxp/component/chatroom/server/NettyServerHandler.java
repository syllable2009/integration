package com.jxp.component.chatroom.server;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.handle.MsgHandle;
import com.jxp.component.chatroom.handle.MsgHandleContainer;
import com.jxp.util.NettyAttrUtil;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
    private MsgHandleContainer msgHandleContainer;
    private static Long HEARTBEAT_INTERVAL = 10 * 3 * 1000L;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[server][channelInactive]remoteAddress:{}", ctx.channel().remoteAddress());
        super.channelInactive(ctx);
        NettyChannelManager.remove(ctx.channel());
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        NettyChannelManager.add(ctx.channel());
        log.info("[server][channelActive],remoteAddress:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[server][exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        NettyChannelManager.remove(ctx.channel());
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) throws Exception {
        log.info("[server][channelRead0],invocation:{}", invocation);
        if (!StrUtil.equals("Auth", invocation.getType())) {
            // 安全验证
            if (!ctx.channel().hasAttr(NettyChannelManager.CHANNEL_ATTR_KEY_USER)) {
                log.error("[server][非法请求],invocation:{}", invocation);
                ctx.channel().close(); // 整个请求完关闭整个通道，以便释放资源
//                ctx.close(); // 关闭当前处理器上下文，以便数据数据流到下一个处理器
            }
        }
        // 更新用户最后的活跃时间
        NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
        final MsgHandle messageHandler = msgHandleContainer.getMessageHandler(invocation.getType());
        messageHandler.execute(ctx.channel(), invocation);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("[server][userEventTriggered],evt:{}", evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 这里可以进行相应的处理，比如发送心跳包或者关闭连接
                // 此处的超时时间应该设置的为客户端的3倍，以便客户端重新连接回来
                final Long readerTime = NettyAttrUtil.getReaderTime(ctx.channel());
                if (System.currentTimeMillis() - readerTime > HEARTBEAT_INTERVAL) {
                    log.info("[server][userEventTriggered]读超时，连接关闭");
                    NettyChannelManager.remove(ctx.channel());
                    ctx.channel().close();
                }
//                ctx.writeAndFlush(Invocation.builder()
//                                .type("Heartbeat")
//                                .message("Ping")
//                                .build())
//                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("[server][userEventTriggered]写超时");
                // 这里可以进行相应的处理，比如发送心跳包
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("[server][userEventTriggered]读写超时");
                // 这里可以进行相应的处理，比如发送心跳包或者关闭连接
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
