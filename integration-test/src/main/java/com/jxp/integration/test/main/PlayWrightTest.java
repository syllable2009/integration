package com.jxp.integration.test.main;

import java.nio.file.Paths;
import java.util.Collections;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-11 10:17
 */

@Slf4j
public class PlayWrightTest {

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            // 默认情况下以无头模式运行浏览器,并减慢执行速度
            LaunchOptions launchOptions = new LaunchOptions().setHeadless(true).setSlowMo(100)
                    .setArgs(Collections.singletonList("--start-maximized"));
            Browser browser = playwright.chromium().launch(launchOptions);


//            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
//                    .setIgnoreHTTPSErrors(true)
//                    .setJavaScriptEnabled(true)
//                    //此处可以理解为设定指定窗口启动
//                    .setViewportSize(1980, 1080));
//            Page page1 = context.newPage();

            Page page = browser.newPage();
            page.navigate("https://www.qcc.com/");
            System.out.println(page.title());
            String content = page.content();
            log.info("hahaha:{}",content);
            // 截图
            page.screenshot(new ScreenshotOptions().setFullPage(true).setPath(Paths.get("/Users/jiaxiaopeng/example.png")));

            browser.close();
        }
    }
}
