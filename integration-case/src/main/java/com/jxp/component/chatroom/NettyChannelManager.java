package com.jxp.component.chatroom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.Invocation;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 17:53
 */
@Slf4j
@Component
public class NettyChannelManager {

    private static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance(
            "userId");

    /**
     * Channel 映射
     */
    private Map<ChannelId, Channel> channels = new ConcurrentHashMap<>();
    /**
     * 用户与 Channel 的映射。
     *
     * 通过它，可以获取用户对应的 Channel。这样，我们可以向指定用户发送消息。
     */
    private Map<String, Channel> userChannels = new ConcurrentHashMap<>();

    public void add(Channel channel) {
        channels.put(channel.id(), channel);
        log.info("[add][一个连接({})加入]", channel.id());
    }

    public void addUser(Channel channel, String userId) {
        Channel existChannel = channels.get(channel.id());
        if (existChannel == null) {
            log.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        // 设置属性
        channel.attr(CHANNEL_ATTR_KEY_USER).set(userId);
        // 添加到 userChannels
        userChannels.put(userId, channel);
    }

    public void remove(Channel channel) {
        // 移除 channels
        channels.remove(channel.id());
        // 移除 userChannels
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    public void send(String userId, Invocation invocation) {
        // 获得用户对应的 Channel
        Channel channel = userChannels.get(userId);
        if (channel == null) {
            log.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        // 发送消息
        channel.writeAndFlush(invocation);
    }

    public void sendAll(Invocation invocation) {
        for (Channel channel : channels.values()) {
            if (!channel.isActive()) {
                log.error("[send][连接({})未激活]", channel.id());
                return;
            }
            // 发送消息
            channel.writeAndFlush(invocation);
        }
    }
}
