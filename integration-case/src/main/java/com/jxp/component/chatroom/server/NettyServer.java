package com.jxp.component.chatroom.server;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 16:30
 */
@Order(1)
@Slf4j
@Component
public class NettyServer {

    @Value("${netty.websocket.port}")
    private int port;
    @Value("${netty.websocket.ip}")
    private String ip;

    private Channel serverChannel;

    // boss 线程组，用于服务端接受客户端的连接
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    // worker 线程组，用于服务端接受客户端的数据读写
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private NettyServerHandlerInitializer nettyServerHandlerInitializer;

    @PostConstruct
    public void run() throws Exception {

        // 创建 ServerBootstrap 对象，用于 Netty Server 启动
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置两个 EventLoopGroup 对象
        serverBootstrap.group(bossGroup, workerGroup);
        // 指定 Channel 为服务端 NioServerSocketChannel
        serverBootstrap.channel(NioServerSocketChannel.class);
        // 设置 Netty Server 的端口
        serverBootstrap.localAddress(new InetSocketAddress(this.ip, this.port));

        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)  // 服务端 accept 队列的大小
                .handler(new LoggingHandler(LogLevel.INFO))
//                .childOption(ChannelOption.SO_KEEPALIVE, true) //TCP Keepalive 机制，实现 TCP
                // 层级的心跳保活功能, TCP 自带的空闲检测机制，默认是 2 小时。这样的检测机制，从系统资源层面上来说是可以接受的,但是在业务层面，如果 2 小时才发现客户端与服务端的连接实际已经断开，会导致中间非常多的消息丢失，影响客户的使用体验
                .childOption(ChannelOption.TCP_NODELAY, true); // 允许较小的数据包的发送，降低延迟
        // childHandler指定处理新连接数据的读写处理逻辑, ChannelInitializer，它用于 Channel 创建时，实现自定义的初始化逻辑
        serverBootstrap.childHandler(nettyServerHandlerInitializer);
        // 绑定端口，并同步等待成功，即启动服务端
        ChannelFuture future = serverBootstrap.bind().sync();
        if (future.isSuccess()) {
            this.serverChannel = future.channel();
            log.info("[start][Netty Server 启动成功,ip:{},端口:{}]", ip, port);
        } else {
            log.error("[start][Netty Server 启动失败,ip:{},端口:{}]", ip, port);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("[stop][Netty Server 关闭,ip:{},端口:{}]", ip, port);
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();
        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
        if (this.serverChannel != null) {
            try {
                this.serverChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
