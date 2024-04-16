package com.jxp.integration.test.ten;

import java.nio.charset.Charset;
import java.util.BitSet;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-03-18 15:15
 */

@Slf4j
public class CharTest {
    public static void main(String[] args) {
        char c1 = '我'; // Java内存采用unicode来表示字符，java中的一个char是2个字节，一个中文或英文字符的unicode编码都占2个字节，但如果采用其他编码方式，一个字符占用的字节数则各不相同。
        log.info("c1:{}-{}", c1, Character.BYTES);
        String s1 = "我";
        byte[] bytes = s1.getBytes(Charset.forName("utf-8"));
        log.info("s1:{}-{}", s1, bytes.length);

        String s2 = "q中";
        byte[] byte2 = s2.getBytes(Charset.forName("utf-8"));
        log.info("s2:{}-{}", s2, byte2.length);

        Integer i = 4;
        log.info("i:{}-{}-{}", i, i.byteValue(), Integer.BYTES);
        // BitSet适合用于无重复，整数，常用于大数据场景或者日志统计。
        BitSet bs = new BitSet();
    }
}
