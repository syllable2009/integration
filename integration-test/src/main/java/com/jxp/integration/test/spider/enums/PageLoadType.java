package com.jxp.integration.test.spider.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型
 */
@NotNull
@AllArgsConstructor
@Getter
public enum PageLoadType {

    Current("Current", "当前页"),
    Blank("Blank", "新页面"),
    ;

    private static final Map<String, PageLoadType> VALUE_MAP =
            Arrays.stream(PageLoadType.values())
                    .collect(Collectors.toMap(PageLoadType::getCode, Function.identity()));

    private String code;
    private String name;

    public static PageLoadType of(String type) {
        return VALUE_MAP.get(type);
    }
}
