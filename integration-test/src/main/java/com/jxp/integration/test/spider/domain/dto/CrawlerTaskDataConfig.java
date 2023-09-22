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

    // 解析链接
    private String link; //$.data[*].item_info.article_info.article_id
    private String linkMethod; //jsonpath
    private String linkPrefix; // https://juejin.cn/post/

    // 解析封面
    private String coverMethod;
    private String cover;
    private String coverPrefix;

    // 解析标题-as文件夹名称
    private String titileMethod;
    private String titile;

    // 第三方业务id
    private String thirdIdMethod;
    private String thirdId;
}
