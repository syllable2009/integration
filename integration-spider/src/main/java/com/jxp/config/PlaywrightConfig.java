package com.jxp.config;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.jxp.EnvUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:59
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "playwright", name = "enable", havingValue = "true")
public class PlaywrightConfig {

    // playwright对象
    private static Playwright playwright = null;
    // chromium浏览器对象
    private static Browser chromiumBrowser = null;
    // 访问浏览器上下文对象
    private static BrowserContext chromiumBrowserContext = null;
    // 访问浏览器代理上下文对象
    private static BrowserContext chromiumProxyBrowserContext = null;

    // 页面跳转参数
    public static final NavigateOptions PAGE_NAV_OPTIONS = new NavigateOptions()
            .setWaitUntil(WaitUntilState.LOAD)
            .setTimeout(30_000);

    @Bean(name = "playwright")
    @ConditionalOnMissingBean
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

    // 浏览器参数
    @SuppressWarnings("checkstyle:MagicNumber")
    public BrowserType.LaunchOptions getBrowserOptions() {
//        System.setProperty("webdriver.chrome.logfile","/home/web_server/springboot/log/chromedriver.log");
        return new BrowserType.LaunchOptions()
                .setTimeout(30_000)
                .setSlowMo(1000)
                .setHeadless(false)
                .setDevtools(false)
                .setChromiumSandbox(false)
                .setArgs(Lists.newArrayList(
//                        "--remote-debugging-port=9222",
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
                        "--log-path=/home/web_server/springboot/log/chromedriver.log",
                        "--log-level=INFO"));
    }

    // 上下文对象参数
    public NewContextOptions getProxy(Boolean ifProxy) {
        if (BooleanUtils.isTrue(ifProxy)) {
            if (EnvUtils.isLocal()) {
                return getBrowserContextOptions();
            }
//            List<String> foreign = PROXY_CONFIG.get().get("foreign");
//            if (CollectionUtils.isNotEmpty(foreign)) {
//                return getBrowserContextOptions().setProxy(foreign.get(0));
//            }
        }
        return getBrowserContextOptions();
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public Browser.NewContextOptions getBrowserContextOptions() {
        return new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("zh-CN");
    }


    @Bean(name = "chromiumProxyBrowserContext")
    public BrowserContext chromiumProxyBrowserContext() {
        if (null == chromiumProxyBrowserContext) {
            log.info("PlaywrightConfig init chromiumProxyBrowserContext");
            NewContextOptions browserContextOptions = getProxy(true);
            chromiumProxyBrowserContext = chromiumBrowser().newContext(browserContextOptions);
        }
        return chromiumProxyBrowserContext;
    }

    @Bean(name = "chromiumBrowserContext")
    public BrowserContext chromiumBrowserContext() {
        if (null == chromiumBrowserContext) {
            log.info("PlaywrightConfig init chromiumBrowserContext");
            NewContextOptions browserContextOptions = getProxy(false);
            chromiumBrowserContext = chromiumBrowser().newContext(browserContextOptions);
        }
        return chromiumBrowserContext;
    }

    public static BrowserContext getChromiumBrowserContext(Boolean ifForeignProxy) {
        if (BooleanUtils.isTrue(ifForeignProxy)) {
            return chromiumProxyBrowserContext;
        }
        return chromiumBrowserContext;
    }

    public static Page getChromiumBrowserPage(Boolean ifProxy) {
        return getChromiumBrowserContext(ifProxy).newPage();
    }

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
//                if (null != chromiumProxyBrowser) {
//                    chromiumProxyBrowser.close();
//                }
        if (null != playwright) {
            playwright.close();
        }
    }

    public static void clearCookie() {
        if (null != chromiumBrowserContext) {
            log.info("start clear chromiumBrowserContext cookies");
            chromiumBrowserContext.clearCookies();
        }
        if (null != chromiumProxyBrowserContext) {
            log.info("start clear chromiumBrowserContext cookies");
            chromiumProxyBrowserContext.clearCookies();
        }
    }
}
