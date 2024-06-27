package com.jxp.component.chatroom.codec;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * MessageToByteEncoder 是 Netty 定义的编码 ChannelHandler 抽象类，将泛型 <I> 消息编码转换成字节数组
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:51
 */
@Slf4j
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Invocation invocation, ByteBuf out) {
        // <2.1> 将 Invocation 转换成 byte[] 数组
        final String jsonStr = JSONUtil.toJsonStr(invocation);
        byte[] content = StrUtil.bytes(jsonStr, "UTF-8");
        // <2.2> 写入 length
        out.writeInt(content.length);
        // <2.3> 写入内容
        out.writeBytes(content);
        log.info("[encode][连接({}) 编码了一条消息({})]", ctx.channel().id(), jsonStr);
    }
}
