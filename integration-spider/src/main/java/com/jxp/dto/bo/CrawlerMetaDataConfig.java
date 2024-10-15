package com.jxp.dto.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:43
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CrawlerMetaDataConfig {

    private String name;
    private String domain;
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

    private String descriptionType;
    private String descriptionMethod;
    private String description;

    // 第三方业务id
    private String thirdIdMethod;
    private String thirdId;

    // 自定义封面
    private String sourceIdentity;
    private String mediaType;
    private String maintainer;

    // 是否需要登录
    private Boolean ifNeedLogin;
    private Boolean ifProxy;

    // 是否需要下载附件
    private Boolean ifDownloadPic;
    private String picMethod;
    private String picPrefix;
    private String pic;

    // 是否开启自动发布
    private Boolean autoPublish;

    // 额外的解析规则，此值会存放到一个特定的map-json中
    private List<CrawlerConfigRow> extParseList;
}
