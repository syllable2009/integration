package com.jxp.integration.test.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 11:30
 */
@Configuration
@Slf4j
public class PlaywrightConfig {

    // 唯一的playwright对象
    private static Playwright playwright = null;
    // 访问内网的浏览器对象
    private static Browser chromiumBrowser = null;
    // 代理海外的浏览器对象
    private static Browser chromiumProxyBrowser = null;
    // 访问内网的浏览器上下文对象
    private static BrowserContext chromiumBrowserContext = null;
    // 访问海外网站的浏览器上下文对象
    private static BrowserContext chromiumProxyBrowserContext = null;

    public static final Path COOKIE_PATH = Paths.get("/var/tmp/cookies.json");
    // load new Browser.NewContextOptions().setStorageStatePath(Paths.get("/Users/cookies.json"))
    // save BROWSER_CONTEXT.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("cookies.json")));

    public static final NavigateOptions PAGE_NAV_OPTIONS = new NavigateOptions()
            .setWaitUntil(WaitUntilState.LOAD)
            .setTimeout(30_000);

    private static List<String> PROXY_CONFIG = Lists.newArrayList();

    @Bean(name = "playwright")
    public Playwright playwright() {
        if (null == playwright) {
            log.info("PlaywrightConfig init playwright");
            playwright = Playwright.create();
        }
        return playwright;
    }

    @Bean(name = "chromiumBrowser")
    public Browser chromiumBrowser() {
        if (null == chromiumBrowser) {
            log.info("PlaywrightConfig init chromiumBrowser");
            chromiumBrowser = playwright().chromium()
                    .launch(getBrowserOptions());
        }
        return chromiumBrowser;
    }

    public BrowserType.LaunchOptions getBrowserOptions() {
        System.setProperty("webdriver.chrome.logfile", "/home/log/chromedriver.log");
        return new LaunchOptions()
                .setTimeout(30_000)
                .setSlowMo(1000)
                .setHeadless(false)
                .setChromiumSandbox(false)
                .setDevtools(false)
                .setArgs(Lists.newArrayList(
                        "--lang=zh-CN",
                        "--disable-dev-shm-usage",
                        "--disable-gpu",
                        "--disable-notifications",
                        "--disable-cache",
                        "--disable-web-security",
                        "--disk-cache-size=1",
                        "--media-cache-size=52428800",
                        "--disable-3d-apis",
                        "--disable-infobars",
                        "--disable-sync",
                        "--log-path=/home/log/chromedriver.log",
                        "--log-level=INFO"));
    }

    @Bean(name = "chromiumProxyBrowserContext")
    public BrowserContext chromiumProxyBrowserContext() {
        if (null == chromiumProxyBrowserContext) {
            log.info("PlaywrightConfig init chromiumProxyBrowserContext");
            NewContextOptions browserContextOptions = getBrowserContextOptions(true);
            chromiumProxyBrowserContext = chromiumBrowser().newContext(browserContextOptions);
        }
        return chromiumProxyBrowserContext;
    }

    @Bean(name = "chromiumBrowserContext")
    public BrowserContext chromiumBrowserContext() {
        if (null == chromiumBrowserContext) {
            log.info("PlaywrightConfig init chromiumBrowserContext");
            NewContextOptions browserContextOptions = getBrowserContextOptions(false);
            chromiumBrowserContext = chromiumBrowser().newContext(browserContextOptions);
        }
        return chromiumBrowserContext;
    }

    public Browser.NewContextOptions getBrowserContextOptions(Boolean ifProxy) {
        NewContextOptions newContextOptions = new NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("zh-CN");
        if (Files.exists(COOKIE_PATH)) {
            newContextOptions.setStorageStatePath(COOKIE_PATH);
        }
        if (BooleanUtils.isNotTrue(ifProxy)) {
            return newContextOptions;
        }
        if (CollectionUtils.isNotEmpty(PROXY_CONFIG)) {
            newContextOptions.setProxy(PROXY_CONFIG.get(0));
        }
        return newContextOptions;
    }

    // 获取上下文对象
    public static BrowserContext getChromiumBrowserContext(Boolean ifForeignProxy) {
        if (BooleanUtils.isTrue(ifForeignProxy)) {
            return chromiumProxyBrowserContext;
        }
        return chromiumBrowserContext;
    }

    // 获取新tab页面对象，用完一定要记得关闭，否则占用内存
    public static Page getChromiumBrowserPage(Boolean ifForeignProxy) {
        return getChromiumBrowserContext(ifForeignProxy).newPage();
    }

    // 关闭浏览器
    public static void closeChromium() {
        log.info("start close playwright");
        if (null != chromiumBrowserContext) {
            chromiumBrowserContext.close();
        }
        if (null != chromiumProxyBrowserContext) {
            chromiumProxyBrowserContext.close();
        }
        if (null != chromiumBrowser) {
            chromiumBrowser.close();
        }
        if (null != playwright) {
            playwright.close();
        }
    }
}
