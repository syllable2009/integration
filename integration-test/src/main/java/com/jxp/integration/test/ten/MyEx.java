package com.jxp.integration.test.ten;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-12 17:48
 */

@Slf4j
public class MyEx {


    public static void main(String[] args) {
        String str1 = "https:///spider/32f914c9a58345bfb8fe412489c87d6f?src=VOcjc+5AmminY+m71tGVzzalrzGzXXzrfvZCjx+a&x-space-id=100000003";
        String str2 = "/spider/32f914c9a58345bfb8fe412489c87d6f?src=VOcjc+5AmminY+m71tGVzzalrzGzXXzrfvZCjx+a&x-space-id=100000003";
        String str3 = "https:///is-docsfile/spider/465dbdb49e384077a2dafdd05fd98c34";

        log.info("Str1:{}", exp(str1));
        log.info("Str2:{}", exp(str2));
        log.info("Str3:{}", exp(str3));

        String str = "夕阳西下，天空被染成了绚丽的橙红色。晚霞映照在湖面上，湖水仿佛燃烧起来一般，闪烁着金色的光芒。远处的山峦在夕阳的映照下，显得格外壮丽。夜幕降临，星星开始在天空中闪烁，月亮升起，洒下银色的月光。整个世界在这宁静的夜晚中显得格外美丽夕阳西下，天空被染成了绚丽的橙红色。晚霞映照在湖面上，湖水仿佛燃烧起来一般，闪烁着金色的光芒。远处的山峦在夕阳的映照下，显得格外壮丽。夜幕降临，星星开始在天空中闪烁，月亮升起，洒下银色的月光。整个世界在这宁静的夜晚中显得格外美丽夕阳西下，天空被染成了绚丽的橙红色。晚霞映照在湖面上，湖水仿佛燃烧起来一般，闪烁着金色的光芒。远处的山峦在夕阳的映照下，显得格外壮丽。夜幕降临，星星开始在天空中闪烁，月亮升起，洒下银色的月光。整个世界在这宁静的夜晚中显得格外美丽夕阳西下，天空被染成了绚丽的橙红色。晚霞映照在湖面上，湖水仿佛燃烧起来一般，闪烁着金色的光芒。远处的山峦在夕阳的映照下，显得格外壮丽。夜幕降临，星星开始在天空中闪烁，月亮升起，洒下银色的月光。整个世界在这宁静的夜晚中显得格外美丽夕阳西下，天空被染成了绚丽的橙红色。晚霞映照在湖面上，湖水仿佛燃烧起来一般，闪烁着金色的d";
        log.info("length:{}", str.length());

        LocalDateTime now = LocalDateTime.now();
//        1723539515000
        final long epochSecond = now.atZone(ZoneId.systemDefault()).toEpochSecond();
        log.info("t:{}", epochSecond);
    }


    private static String exp(String s) {
        String regex = "/spider[^?]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
