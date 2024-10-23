package com.jxp.commonjson;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-23 17:15
 */
@AllArgsConstructor
@Getter
public enum BlockTypeEnum {
    Link("Link", "链接"),
    MotionTopic("MotionTopic", "at话题"),
    Text("Text", "文本，空格也算文本"),
    Newline("Newline", "换行"),
    Picture("Picture", "图片，是个列表"),
    ;

    private static final Map<String, BlockTypeEnum> VALUE_MAP =
            Arrays.stream(BlockTypeEnum.values())
                    .collect(Collectors
                            .toMap(BlockTypeEnum::getCode, Function.identity()));

    private String code;
    private String desc;

    public static BlockTypeEnum of(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        return VALUE_MAP.get(code);
    }
}
