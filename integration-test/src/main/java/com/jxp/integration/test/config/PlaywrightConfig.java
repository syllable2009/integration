package com.jxp.integration.test.config;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.APIRequest.NewContextOptions;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
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
            PLAYWRIGHT.chromium()
                    .launch(new BrowserType.LaunchOptions().setTimeout(0).setSlowMo(1000).setHeadless(true));

    //    public static Browser FIREFOX =
    //            PLAYWRIGHT.firefox().launch(new BrowserType.LaunchOptions().setSlowMo(1000).setHeadless(false));
    //
    //    public static Browser WEBKIT =
    //            PLAYWRIGHT.webkit().launch(new BrowserType.LaunchOptions().setSlowMo(1000).setHeadless(false));

    // BrowserContext用于内存中隔离的浏览器配置文件，以确保它们不会相互干扰。
    public static BrowserContext BROWSER_CONTEXT =
            CHROMIUM.newContext(
                    new Browser.NewContextOptions().setStorageStatePath(Paths.get("/Users/jiaxiaopeng/cookies.json")));

    // save BROWSER_CONTEXT.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("cookies.json")));

    @Bean(name = "chromiumBrowser")
    public Browser chromiumBrowser() {
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
        return PLAYWRIGHT.chromium().launch(new BrowserType.LaunchOptions().setSlowMo(1000).setHeadless(false));
    }

    // 默认的上下浏览器请求上下文
    @Bean(name = "chromiumBrowserContext")
    public BrowserContext chromiumBrowserContext() {
        if (null != BROWSER_CONTEXT) {
            return BROWSER_CONTEXT;
        }
        return chromiumBrowser().newContext();
    }

    public static void closeChromium() {
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

    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/vnd.github.v3+json");
        APIRequestContext apiRequestContext = PLAYWRIGHT.request().newContext(new NewContextOptions()
                // All requests we send go to this API endpoint.
                .setBaseURL("https://api.github.com")
                .setExtraHTTPHeaders(headers));

        APIResponse apiResponse = apiRequestContext.get("https://api.github.com");
        apiResponse.text();
        apiResponse.ok();
    }
}
