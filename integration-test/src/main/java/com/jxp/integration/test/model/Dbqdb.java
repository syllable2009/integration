package com.jxp.integration.test.model;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-14 17:28
 */
@Data
@Slf4j
public class Dbqdb {

    private int id;
    private String title;
    private String thumbnail;
    private int width;
    private int height;
    private String font;
    private int fontSize;
    private String position;
    private boolean dynamic;
    private String image;

    public static void main(String[] args) {
        Map<String, Integer> tagRepeatability = ImmutableMap.of("1", 2, "2", 10, "3", 5, "4", 1);
        tagRepeatability.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Entry.comparingByValue()))
                .forEach(e -> log.info("k:{},v:{}", e.getKey(), e.getValue()));
    }
}
