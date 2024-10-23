package com.jxp.commonjson;

import java.util.List;

import com.jxp.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-23 17:36
 */

@Slf4j
public class TestMain {

    public static void main(String[] args) {
        String jsonArrayStr = "[{\"blockType\":\"Text\",\"content\":\"你是一个好人\"},{\"blockType\":\"Picture\",\"type\":\"png\",\"url\":\"https://123.com\"}]\n";
        final List<Block> blocks = JsonUtils.jsonToArray(jsonArrayStr, Block.class);
        log.info("*****:{}", blocks);
    }
}
