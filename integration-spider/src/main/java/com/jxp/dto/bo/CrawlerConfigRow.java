package com.jxp.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:43
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CrawlerConfigRow {
    // 解析的id标识
    private String parseId;
    // 解析类型：对应aigc，none，图片摘要适用
    private String parseType;
    // 解析方式：对应xpath，css，jsonpath
    private String parseMethod;
    // 解析的表达式
    private String parseExpression;
    // 解析结果前缀，用于链接等
    private String parsePrefix;
}
