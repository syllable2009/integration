package com.jxp.integration.test.spider.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:03
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CrawlerMetaDataConfig {

    // 默认的解析方式：支持css，xpath1.0，css-list只适合content，不必做成配置
    private String method;
    // 原始链接
    private String link;
    private String linkMethod;
    private String linkPrefix;

    private String title;
    private String titleMethod;

    private String coverType;
    private String coverMethod;
    private String cover;
    private String coverPrefix;

    private String contentMethod;
    private String content;

    // 自定义封面
    private String sourceIdentity;
    private String maintainer;
}
