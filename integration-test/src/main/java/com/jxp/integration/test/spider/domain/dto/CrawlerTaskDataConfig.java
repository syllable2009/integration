package com.jxp.integration.test.spider.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 17:28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CrawlerTaskDataConfig {

    // 默认的解析方式：支持css，xpath1.0，css-list只适合content，不必做成配置
    private String method;
    private String link; //$.data[*].item_info.article_info.article_id
    private String linkMethod; //jsonpath
    private String prefix; // https://juejin.cn/post/

    private String coverMethod;
    private String cover;
    private String coverPrefix;
}
