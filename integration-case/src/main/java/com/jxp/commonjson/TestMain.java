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
        blocks.stream()
                .forEach(e -> parse(e));
        log.info("*****:{}", blocks);
    }

    public static void parse(Block block) {

        final BlockTypeEnum blockTypeEnum = BlockTypeEnum.of(block.getBlockType());
        switch (blockTypeEnum) {
            case Link:
                break;
            case MotionTopic:
                break;
            case Text:
                final TextBlock tb = (TextBlock) block;
                log.info("TextBlock:{}", tb.getContent());
                break;
            case Newline:
                break;
            case Picture:
                final PictureBlock pb = (PictureBlock) block;
                log.info("Picture:{}", pb.getUrl());
                break;
            default:
                break;
        }
    }
}
