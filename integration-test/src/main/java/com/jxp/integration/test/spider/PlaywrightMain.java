package com.jxp.integration.test.spider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import com.jxp.integration.test.util.DownloadFileUtil;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ScreenshotType;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 11:02
 */
@Slf4j
public class PlaywrightMain {

    private static final String BASE_URL = "https://www.36kr.com/newsflashes/2368027471505793";

    private static final Path path = Paths.get("/Users/jiaxiaopeng/");

    public static void main(String[] args) {
        test3();
    }

    public static void test1() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(5000));
            BrowserContext browserContext = browser.newContext(new NewContextOptions()
                    .setViewportSize(1920, 1080));
            Page page = browserContext.newPage();
            page.navigate(BASE_URL);
            page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG)
                    .setPath(path));
        }
    }

    public static void test2() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(1000));
            BrowserContext browserContext = browser.newContext(new NewContextOptions()
                    .setViewportSize(1920, 1080));
            Page page = browserContext.newPage();
            page.onRequest(request -> System.out.println(">> " + request.method() + " " + request.url()));
            page.onResponse(response -> System.out.println("<<" + response.status() + " " + response.url()));
            page.navigate("https://www.baidu.com/");
            page.locator("#kw").fill("公众号");
            page.locator("#kw").press("Enter");
            String value = page.getAttribute("#kw", "value");
            Assert.equals(value, "公众号");
            System.out.println(page.url());

        }
    }

    public static void test3() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(1000));
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true));

            Page page1 = browserContext.newPage();
            //            page.onDialog(dialog -> dialog.dismiss());

            //            page.waitForTimeout(60_000); = thread.sleep
            page1.navigate("http://www.netbian.com/2560x1440/", new Page.NavigateOptions().setTimeout(120 * 1000));
            log.info("pageInfo1:{},url:{}", page1.toString(),page1.url());

            Page page2 = browserContext.waitForPage(() -> {
                page1.getByAltText("可爱女生帽子高清美女壁纸背景图片").click();
                log.info("open a new tab2");
            });

            // 注册事件
            page2.waitForLoadState();
            log.info("pageInfo2:{},url:{}", page2.toString(),page2.url());


            Page page3 = browserContext.waitForPage(() -> {
                log.info("open a new tab3");
                page2.getByAltText("可爱女生帽子高清美女壁纸背景图片").click();
            });

            page3.waitForLoadState();
            log.info("pageInfo3:{},url:{}", page3.toString(),page3.url());


            // 这里为什么会超时？难道一定要下载的流文件？
            Page page4 = browserContext.waitForPage(() -> {
                log.info("open a new tab4");
                page3.getByAltText("可爱女生帽子高清美女壁纸背景图片").click();
            });

            String content = page4.content();
            log.info("content:{}", content);
            DownloadFileUtil.downloadByHutool(page4.url(),"/Users/jiaxiaopeng/","222.jpg");
            // page4.waitForLoadState();  图片下载文件流有问题
            page4.waitForTimeout(5_000);
            log.info("pageInfo4:{},url:{}", page4.toString(),page4.url()); // http://img.netbian.com/file/2023/0918/2022362nAbF.jpg


            //这里是运行时打断点使用，方便调试（适用于喜欢用录制回放生成脚本的同学）
            //page.pause();
            //后退操作
            //            page.goBack();
            //前进操作
            //            page.goForward();
            //刷新操作
            //            page.reload();
        }
    }

    public static void test4() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(1000));
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true));

            Page page1 = browserContext.newPage();
            //            page.onDialog(dialog -> dialog.dismiss());

            //            page.waitForTimeout(60_000); = thread.sleep
            page1.navigate("http://www.netbian.com/2560x1440/", new Page.NavigateOptions().setTimeout(120 * 1000));
            log.info("pageInfo1:{},url:{}", page1.toString(), page1.url());

            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in"));
//            page1.getAttribute(AriaRole.LINK,new Page.GetAttributeOptions().);
        }
    }

    public static void test5() {

    }

    public static void test6() {

    }
}
