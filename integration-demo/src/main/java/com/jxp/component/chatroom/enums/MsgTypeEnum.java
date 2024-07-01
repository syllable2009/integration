package com.jxp.component.chatroom.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 */
@AllArgsConstructor
@Getter
public enum MsgTypeEnum {

    Auth("Auth", "登录鉴权"),
    Chat("Chat", "单聊消息"),
    ChatRoom("ChatRoom", "群聊消息"),
    Heartbeat("Heartbeat", "心跳消息"),
    Enter("Enter", "进入会话消息"),
    Leave("Leave", "离开会话消息"),
    Ack("Ack", "Ack消息"),
    Unkown("Unkown", "未知消息"),
    ;

    private static final Map<String, MsgTypeEnum> VALUE_MAP =
            Arrays.stream(MsgTypeEnum.values())
                    .collect(Collectors
                            .toMap(MsgTypeEnum::getCode, Function.identity()));

    private String code;
    private String name;

    public static MsgTypeEnum of(String code) {
        if (StrUtil.isBlank(code)) {
            return MsgTypeEnum.Unkown;
        }
        return VALUE_MAP.get(code);
    }
}
