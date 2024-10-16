package com.jxp.webmagic;

import com.jxp.dto.bo.CrawlerDomainConfig;
import com.microsoft.playwright.Page;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-16 10:50
 */
public interface LoginService {
    Boolean globelLogin(String domain, Page page, String url, CrawlerDomainConfig config);
}
