package com.jxp.integration.test.spider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-18 10:05
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page {
    private Long aid;
    private String id;
    private String title;
    private String link;
    private String description;
    private String domain;
    private String biztype;
    private String bizid;
    private String category; // 分类
    private String md5;
    private String vector;
    private String cover;
    private Integer state;
    private String folderId; // 文件夹id
}
