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
public enum MsgActiveEnum {

    Recall("Recall", "撤回"),
    Delete("Delete", "删除"),
    Active("Active", "有效"),
    ;

    private static final Map<String, MsgActiveEnum> VALUE_MAP =
            Arrays.stream(MsgActiveEnum.values())
                    .collect(Collectors
                            .toMap(MsgActiveEnum::getCode, Function.identity()));

    private String code;
    private String name;

    public static MsgActiveEnum of(String code) {

        return VALUE_MAP.get(code);
    }
}
