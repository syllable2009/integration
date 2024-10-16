package com.jxp.dto.bo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-16 10:30
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CrawlerDomainConfig {
    // 是否需要登录
    private String domain;
    private Boolean ifNeedLogin;
    private Boolean ifProxy;
    private Map<String, String> ext;
}
