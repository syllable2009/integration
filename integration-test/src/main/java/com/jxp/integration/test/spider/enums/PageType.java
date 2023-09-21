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
public enum PageType {

    IndexList("IndexList", "列表页"),
    PageList("PageList", "详细列表页"),
    SingleElement("SingleElement", "单元素页"),
    Trigger("Trigger", "触发下载页"),
    Download("Download", "文件下载流页"),
    ;

    private static final Map<String, PageType> VALUE_MAP =
            Arrays.stream(PageType.values())
                    .collect(Collectors.toMap(PageType::getCode, Function.identity()));

    private String code;
    private String name;

    public static PageType of(String type) {
        return VALUE_MAP.get(type);
    }
}
