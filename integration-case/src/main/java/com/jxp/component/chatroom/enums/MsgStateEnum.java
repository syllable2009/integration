package com.jxp.component.chatroom.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 */
@AllArgsConstructor
@Getter
public enum MsgStateEnum {
    Sending("Sending", "待发送"),
    Send("Send", "已发送"),
    Deliver("Deliver", "已送达"),
    Read("Read", "已读"),
    ;

    private static final Map<String, MsgStateEnum> VALUE_MAP =
            Arrays.stream(MsgStateEnum.values())
                    .collect(Collectors
                            .toMap(MsgStateEnum::getCode, Function.identity()));

    private String code;
    private String name;

    public static MsgStateEnum of(String code) {
        return VALUE_MAP.get(code);
    }
}
