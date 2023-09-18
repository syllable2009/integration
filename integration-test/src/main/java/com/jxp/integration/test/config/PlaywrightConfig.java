package com.jxp.integration.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 11:30
 */
@Configuration
@Slf4j
public class PlaywrightConfig {

    public static Playwright PLAYWRIGHT = Playwright.create();

    // 饿汉式确保第一次请求加载速度
    public static Browser CHROMIUM =
            PLAYWRIGHT.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

    public static BrowserContext BROWSER_CONTEXT = CHROMIUM.newContext();

    @Bean
    public Browser browser() {
        if (null != CHROMIUM) {
            return CHROMIUM;
        }
        if (null == PLAYWRIGHT) {
            synchronized (PlaywrightConfig.class) {
                if (null == PLAYWRIGHT) {
                    PLAYWRIGHT = Playwright.create();
                }
            }
        }
        return PLAYWRIGHT.chromium().launch();
    }

    // 默认的上下浏览器请求上下文
    @Bean
    public BrowserContext browserContext() {
        if (null != BROWSER_CONTEXT) {
            return BROWSER_CONTEXT;
        }
        return browser().newContext();
    }

    public static void close() {
        BrowserContext browserContext = PlaywrightConfig.BROWSER_CONTEXT;
        if (null != browserContext) {
            browserContext.close();
        }
        Browser chromium = PlaywrightConfig.CHROMIUM;
        if (null != chromium) {
            chromium.close();
        }
        Playwright playwright = PlaywrightConfig.PLAYWRIGHT;
        if (null != playwright) {
            playwright.close();
        }
    }
}
