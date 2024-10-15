package com.jxp.webmagic;


import org.springframework.stereotype.Service;

import com.jxp.config.PlaywrightConfig;
import com.jxp.dto.bo.CrawlerLogin;
import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;

import cn.hutool.core.util.BooleanUtil;
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
        Boolean result = true;
        switch (domain) {
            case "abc.com":
                // 登录拦截标记
                result = needLoginAbc(page);
                if (BooleanUtil.isTrue(result)) {
                    return loginAbc(page, url, CrawlerLogin.builder().build());
                }
                return result;
            default:
                break;
        }
        return false;
    }


    private Boolean needLoginAbc(Page page) {
        // 登录标记
        if (!page.url().startsWith("http://www.abc.com/login.html")) {
            return null;
        }
        // 登录页拦截
        if (page.url().startsWith("http://www.abc.com/login.html")) {
            return true;
        }
        return false;
    }

    private Boolean loginAbc(Page page, String url, CrawlerLogin config) {
        log.info("LoginService login start loginABC,page.url:{},targetUrl:{}", page.url(), url);
        page.navigate("https://www.abc.com/login.html", PlaywrightConfig.PAGE_NAV_OPTIONS);
        Frame frame = page.frames().get(0).childFrames().get(0);
        ElementHandle submit = frame.waitForSelector("xpath=//*[@id=\"ssoSubmit\"]");
        ElementHandle ssoSubmit = page.waitForSelector("xpath=//*[@id=\"submit\"]");
        ElementHandle ssoUsername = page.waitForSelector("xpath=//*[@id=\"username\"]");
        ElementHandle ssoPassword = page.waitForSelector("xpath=//*[@id=\"password\"]");
        // 登录按钮存在
        boolean submitVisible = ssoSubmit.isVisible();
        if (submitVisible) {
            if (null == config) {
                config = CrawlerLogin.builder().build();
            }
            ssoUsername.fill(config.getStrOne());
            ssoPassword.fill(config.getStrTwo());
            ssoSubmit.click();
            log.info("LoginService login click loginABC,submitVisible:{},url:{}", submitVisible, page.url());
        }
        // 登录之后再去验证一次，防止成功不自动跳转
        log.info("LoginService login end loginABC success,submitVisible:{},url:{}", submitVisible,
                page.url());
        return true;
    }
}
