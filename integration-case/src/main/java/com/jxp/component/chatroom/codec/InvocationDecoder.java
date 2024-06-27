package com.jxp.component.chatroom.codec;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * ByteToMessageDecoder 是 Netty 定义的解码 ChannelHandler 抽象类，在 TCP Socket 读取到新数据时，触发进行解码
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:55
 */
@Slf4j
public class InvocationDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        // 标记当前读取位置
        in.markReaderIndex();
        // 判断能够读取的length长度
        if (in.readableBytes() <= 4) {
            return;
        }
        // 读取长度
        final int length = in.readInt();
        if (length <= 0) {
            return;
        }
        // 如果message不够可读，则退回原读取位置
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        // 读取内容
        final byte[] bytes = new byte[length];
        in.readBytes(bytes);
        // 解析成对象
        final Invocation bean = JSONUtil.toBean(StrUtil.str(bytes, "UTF-8"), Invocation.class);
        out.add(bean);
        log.info("[decode][连接({}) 解码了一条消息({})]", ctx.channel().id(), JSONUtil.toJsonStr(bean));
    }

}
