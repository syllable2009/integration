package com.jxp.component.chatroom.server;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.InvocationDecoder;
import com.jxp.component.chatroom.codec.InvocationEncoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 17:28
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    @Value("${netty.websocket.path}")
    private String path;
    @Value("${netty.websocket.max-frame-size}")
    private long maxFrameSize;

    // 心跳超时时间
    private static final Integer READ_TIMEOUT_SECONDS = 30;

    private static final Integer WRITE_TIMEOUT_SECONDS = 0;

    private static final Integer ALL_TIMEOUT_SECONDS = 0;

    @Resource
    private NettyServerHandler nettyServerHandler;

    /**
     * 在每一个客户端与服务端建立完成连接时，服务端会创建一个 Channel 与之对应。此时，NettyServerHandlerInitializer 会进行执行 #initChannel(Channel c) 方法，进行自定义的初始化。
     * @param ch
     */
    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:LineLength", "checkstyle:RegexpSingleline"})
    @Override
    protected void initChannel(Channel ch) {
        // <1> 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline channelPipeline = ch.pipeline();
        // <2> 添加一堆 NettyServerHandler 到 ChannelPipeline 中
        channelPipeline
                .addLast(new IdleStateHandler(READ_TIMEOUT_SECONDS, WRITE_TIMEOUT_SECONDS, ALL_TIMEOUT_SECONDS))
                .addLast(new InvocationEncoder())
                .addLast(new InvocationDecoder())

                // Protobuf消息通常带有一个长度前缀，用于指示消息的长度，负责读取这个长度前缀，并确定接下来要读取的消息的长度。
//                .addLast(new ProtobufVarint32FrameDecoder())
//                .addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance())) //是一个Netty的解码器，用于将字节流解码为Protobuf消息对象。它的作用是将接收到的字节数据解析成具体的Protobuf消息对象，方便后续的业务逻辑处理。
//                .addLast(new ProtobufVarint32LengthFieldPrepender()) //是一个Netty的编码器，用于在消息前面添加一个长度字段。这个长度字段使用Varint32编码格式，表示消息的字节长度。它的作用是帮助接收方知道每个消息的边界，从而正确地解析消息。
//                .addLast(new ProtobufEncoder())

//                .addLast(messageDispatcher)
//                .addLast(new HttpServerCodec())
//                .addLast(new ChunkedWriteHandler())
//                .addLast(new HttpObjectAggregator(65536))
//                .addLast(new ChannelInboundHandlerAdapter() {
//                    @Override
//                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                        if (msg instanceof FullHttpRequest) {
//                            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
//                            String uri = fullHttpRequest.uri();
//                            if (!uri.equals(path)) {
//                                // Not websocket uri, return 404
//                                ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND))
//                                        .addListener(ChannelFutureListener.CLOSE);
//                                return;
//                            }
//                        }
//                        super.channelRead(ctx, msg);
//                    }
//                })
//                .addLast(new WebSocketServerCompressionHandler())
//                .addLast(new WebSocketServerProtocolHandler(path, null, true, maxFrameSize))
                // 服务端处理器
//                .addLast(applicationContext.getBean(WebsocketMessageHandler.class));       // Get handler from IOC
                .addLast(nettyServerHandler);
    }
}
