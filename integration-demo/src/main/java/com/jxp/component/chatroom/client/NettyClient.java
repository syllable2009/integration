package com.jxp.component.chatroom.client;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.Invocation;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:18
 */
@Slf4j
@Component
public class NettyClient {

    private EventLoopGroup eventGroup = new NioEventLoopGroup();

    @Value("${netty.websocket.port}")
    private int port;
    @Value("${netty.websocket.ip}")
    private String ip;
    private boolean ifReconnecting = false;
    private boolean ifStart = false;

    @Resource
    private NettyClientHandlerInitializer nettyClientHandlerInitializer;
    private volatile Channel clientChannel;

    @PostConstruct
    public void start() {
        try {
            Bootstrap b = new Bootstrap();
            b.group(eventGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(nettyClientHandlerInitializer);

            ChannelFuture f = b.connect(ip, port).sync();
            f.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        ifStart = true;
                        clientChannel = future.channel();
                        log.info("[start][Netty Client 启动成功,ip:{},端口:{}]", ip, port);
                    } else {
                        log.error("[start][Netty Client 启动失败,ip:{},端口:{}]", ip, port);
                        reconnect(true);
                    }
                }
            });
        } catch (Exception e) {
            log.error("[start][Client start fail],message:{}", e.getMessage(), e);
        }
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(eventGroup)
//                .channel(NioServerSocketChannel.class)
//                .handler(new LoggingHandler(LogLevel.INFO))
//                .handler(nettyClientHandlerInitializer);
//        final ChannelFuture future = bootstrap.connect(ip, port).sync();
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    clientChannel = future.channel();
//                    log.info("[start][Netty Client 启动成功,ip:{},端口:{}]", ip, port);
//                } else {
//                    log.error("[start][Netty Client 启动失败,ip:{},端口:{}]", ip, port);
//                    reconnect();
//                }
//            }
//        });
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public void reconnect(boolean ifFirst) {
        log.info("[reconnect][Netty Client reconnect,ip:{},端口:{}],ifFirst:{}", ip, port, ifFirst);
        ifStart = false;
        // 尝试重连三次
        for (int i = 0; i < 3; i++) {
            if (!ifStart && !ifReconnecting) {
                synchronized (this) {
                    ifReconnecting = true;
                    log.info("[reconnect][Netty Client reconnect {} time],result:{}", i + 1,
                            ifStart);
                    closeChannel();
                    start();
                    ifReconnecting = false;
                }
            }
            if (ifStart) {
                return;
            }
            ThreadUtil.sleep(10000);
        }
        log.info("[reconnect][Netty Client reconnect three times],result:{}", ifStart);
    }

    @PreDestroy
    public void shutdown() {
        log.info("[stop][Netty Clinet 关闭,ip:{},端口:{}]", ip, port);
        final Future<?> future = eventGroup.shutdownGracefully();
        try {
            future.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
        closeChannel();
    }

    private void closeChannel() {
        if (this.clientChannel != null) {
            try {
                this.clientChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Invocation invocation) {
        // 获得用户对应的 Channel
        if (clientChannel == null) {
            log.error("[send][连接不存在]");
            return;
        }
        if (!clientChannel.isActive()) {
            log.error("[send][连接({})未激活]", clientChannel.id());
            return;
        }
        // 发送消息
        clientChannel.writeAndFlush(invocation);
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().start();
    }
}
