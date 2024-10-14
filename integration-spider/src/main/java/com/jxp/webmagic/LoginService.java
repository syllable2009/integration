package com.jxp.webmagic;


import org.springframework.stereotype.Service;

import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.microsoft.playwright.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录服务，用于处理在浏览器爬虫过程中遇到的需要登录的网站
 *
 * @author jiaxiaopeng
 * Created on 2023-10-25 10:38
 */
@Slf4j
@Service
public class LoginService {

    // 全局的登录校验器
    // null代表已经登录过，true代表本次登录成功，false带表登录失败
    public Boolean globelLogin(String domain, Page page, String url, CrawlerMetaDataConfig config) {
        return true;
    }
}
