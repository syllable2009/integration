package com.jxp.component.chatroom.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jxp.component.chatroom.codec.Invocation;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 17:53
 */
@Slf4j
public class NettyChannelManager {

    public static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance(
            "userId");

    /**
     * Channel 映射
     */
    public static final Map<ChannelId, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();
    /**
     * 用户与 Channel 的映射。
     *
     * 通过它，可以获取用户对应的 Channel。这样，我们可以向指定用户发送消息。
     */
    public static final Map<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static void add(Channel channel) {
        CHANNEL_MAP.put(channel.id(), channel);
        log.info("[add][一个连接({})加入]", channel.id());
    }

    public static void addUser(Channel channel, String userId) {
        Channel existChannel = CHANNEL_MAP.get(channel.id());
        if (existChannel == null) {
            log.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        // 设置属性
        channel.attr(CHANNEL_ATTR_KEY_USER).set(userId);
        // 添加到 userChannels
        USER_CHANNEL_MAP.put(userId, channel);
    }

    public static void remove(Channel channel) {
        // 移除 channels
        CHANNEL_MAP.remove(channel.id());
        // 移除 userChannels
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            USER_CHANNEL_MAP.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    public static void send(String userId, Invocation invocation) {
        // 获得用户对应的 Channel
        Channel channel = USER_CHANNEL_MAP.get(userId);
        if (channel == null) {
            log.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        // 发送消息,发送消息获取回调时拿到 success 只是告知我们消息写入了 TCP 缓冲区成功了而已
        channel.writeAndFlush(invocation);
//                .addListener((ChannelFutureListener) future -> {
//                    future.channel().writeAndFlush();
//                });
    }

    public static void sendAll(Invocation invocation) {
        for (Channel channel : CHANNEL_MAP.values()) {
            if (!channel.isActive()) {
                log.error("[send][连接({})未激活]", channel.id());
                return;
            }
            // 发送消息
            channel.writeAndFlush(invocation);
        }
    }
}
