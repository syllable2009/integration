package com.jxp.component.chatroom.client;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.InvocationDecoder;
import com.jxp.component.chatroom.codec.InvocationEncoder;
import com.jxp.component.chatroom.handle.MessageDispatcher;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
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

    private static final Integer READ_TIMEOUT_SECONDS = 0;

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                // 空闲检测
                .addLast(new IdleStateHandler(0, 10, 0))
                .addLast(new InvocationEncoder())
                .addLast(new InvocationDecoder())
                .addLast(messageDispatcher)
                // 客户端处理器
                .addLast(nettyClientHandler);
    }
}
