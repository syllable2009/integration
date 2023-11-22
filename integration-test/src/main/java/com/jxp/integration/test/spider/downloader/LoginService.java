package com.jxp.integration.test.spider.downloader;

import static com.jxp.integration.test.config.PlaywrightConfig.COOKIE_PATH;
import static com.jxp.integration.test.config.PlaywrightConfig.PAGE_NAV_OPTIONS;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

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

    public Boolean globelLogin(String domain, Page page, String url, CrawlerMetaDataConfig config, Response response) {
        // 每一个域都必须写自己的登录逻辑，有的还需要额外跳转一次防止跨域不自动进行跳转
        switch (domain) {
            case "*.com":
                if (!page.url().startsWith("https://apsso.*.com")
                        && !page.url().startsWith("https://sso.corp.*.com")
                ) {
                    log.info("LoginService login done:{},url:{}", domain, url);
                    return true;
                }
                for (int i = 0; i < 3; i++) {
                    log.info("LoginService login start,domain:{},url:{},time:{}", domain, url, i + 1);
                    if (BooleanUtils.isTrue(loginKSSO(page, url, config))) {
                        log.info("LoginService login success,domain:{},url:{},time:{}", domain, page.url(), i + 1);
                        // 问答，在sso登录以后跳转到了列表页，无法跳转到详细页面,需要再重新跳转一次
                        response = page.navigate(url, PAGE_NAV_OPTIONS);
                        page.context().storageState(new BrowserContext.StorageStateOptions().setPath(COOKIE_PATH));
                        log.info("LoginService login success navigate:{},{}，{}", domain, i + 1, page.url());
                        return true;
                    }
                }
                log.error("LoginService login fail,domain:{},url:{}", domain, url);
                break;
            default:
                log.error("LoginService login not support login,domain:{},url:{}", domain, url);
                return false;
        }
        return false;
    }

    public Boolean loginKSSO(Page page, String url, CrawlerMetaDataConfig config) {
        log.info("start loginKSSO,page.url:{},url:{}", page.url(), url);
        page.navigate("https://sso.corp.*.com");
        ElementHandle ssoSubmit = page.waitForSelector("xpath=//*[@id=\"ssoSubmit\"]");
        ElementHandle ssoUsername = page.waitForSelector("xpath=//*[@id=\"ssoUsername\"]");
        ElementHandle ssoPassword = page.waitForSelector("xpath=//*[@id=\"ssoPassword\"]");
        // 登录按钮存在
        boolean submitVisible = ssoSubmit.isVisible();
        if (submitVisible) {
            ssoUsername.fill("");
            ssoPassword.fill("");
            ssoSubmit.click();
            log.info("click loginKSSO,submitVisible:{},url:{}", submitVisible, page.url());
        }
        // 登录之后再去验证一次，防止成功不自动跳转
        page.navigate(url);
        if (page.url().startsWith("https://apsso.corp.*.com")
                || page.url().startsWith("https://sso.corp.*.com")) {
            log.info("end loginKSSO fail,submitVisible:{},url:{}", submitVisible, page.url());
            return false;
        }
        log.info("end loginKSSO success,submitVisible:{},url:{}", submitVisible, page.url());
        return true;
    }
}
