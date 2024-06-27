package com.jxp.component.chatroom.client;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:25
 */

@Slf4j
@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {


    @Resource
    private NettyClientHandler nettyClientHandler;

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                // 客户端处理器
                .addLast(nettyClientHandler)
                ;
    }
}
