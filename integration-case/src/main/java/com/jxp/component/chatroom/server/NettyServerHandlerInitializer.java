package com.jxp.component.chatroom.server;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 17:28
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> implements ApplicationContextAware {

    @Value("${netty.websocket.path}")
    private String path;
    @Value("${netty.websocket.max-frame-size}")
    private long maxFrameSize;

    // 心跳超时时间
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    private ApplicationContext applicationContext;

    @Resource
    private WebsocketMessageHandler websocketMessageHandler;

    /**
     * 在每一个客户端与服务端建立完成连接时，服务端会创建一个 Channel 与之对应。此时，NettyServerHandlerInitializer 会进行执行 #initChannel(Channel c) 方法，进行自定义的初始化。
     * @param ch
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    protected void initChannel(Channel ch) {
        // <1> 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline channelPipeline = ch.pipeline();
        // <2> 添加一堆 NettyServerHandler 到 ChannelPipeline 中
        channelPipeline.addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(65536))
                .addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        if (msg instanceof FullHttpRequest) {
                            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
                            String uri = fullHttpRequest.uri();
                            if (!uri.equals(path)) {
                                // Not websocket uri, return 404
                                ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND))
                                        .addListener(ChannelFutureListener.CLOSE);
                                return;
                            }
                        }
                        super.channelRead(ctx, msg);
                    }
                })
                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler(path, null, true, maxFrameSize))
                // 服务端处理器
//                .addLast(applicationContext.getBean(WebsocketMessageHandler.class));       // Get handler from IOC
                .addLast(websocketMessageHandler);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
