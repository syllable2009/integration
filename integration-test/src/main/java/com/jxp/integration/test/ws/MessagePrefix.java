package com.jxp.integration.test.ws;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import lombok.Getter;

/**
 * 消息前缀枚举
 *
 * @author ddd
 * Created on 2021-12-14
 */
@Getter
public enum MessagePrefix {

    /**
     * 心跳请求（客户端发起）
     */
    REQUEST_HEARTBEAT('1'),
    /**
     * 心跳请求Ack（服务端发起）
     */
    RESPONSE_HEARTBEAT('2'),
    /**
     * 业务事件请求（客户端发起）
     */
    REQUEST_BUSINESS('3'),
    /**
     * 业务事件请求Ack（服务端发起）
     */
    RESPONSE_BUSINESS('4');

    private final char prefix;

    private static final Map<Character, MessagePrefix> PREFIX_MAP = Arrays.stream(
            MessagePrefix.values())
            .collect(Collectors.toMap(MessagePrefix::getPrefix, Function.identity()));

    MessagePrefix(char prefix) {
        this.prefix = prefix;
    }

    @Nullable
    public static MessagePrefix of(char prefix) {
        return PREFIX_MAP.get(prefix);
    }
}
