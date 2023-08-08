package com.jxp.integration.test.spider;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-18 10:05
 */
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page {
    private Long aid;
    private String id;
    private String title;
    private String link;
    private String description;
    private String domain;
    private String biztype;
    private String bizid;
    private String category; // 分类
    private String md5;
    private String vector;
    private String cover;
    private Integer state;
    private String folderId; // 文件夹id
    private Integer size; // 失效检测
    private String processor; // 解析组件名称


    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/Users/jiaxiaopeng/Downloads/chromedriver_mac64-2/chromedriver");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        chromeOptions.addArguments("--user-data-dir=/Users/Default");

        //        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--start-maximized",
                "allow-running-insecure-content", "--test-type");
        // 不加参数容易403
        chromeOptions.addArguments("--remote-allow-origins=*");
        // 配置参数禁止显示“Chrome正在受到自动软件的控制
        chromeOptions.addArguments("--disable-infobars");
        // **隐身模式**
//        chromeOptions.addArguments("--incognito");
        // 隐藏滚动条, 应对一些特殊页面
        chromeOptions.addArguments("--hide-scrollbars");
        // 阻止弹出窗口
        chromeOptions.setExperimentalOption("excludeSwitches",
                Arrays.asList("disable-popup-blocking"));
        WebDriver driver = new ChromeDriver(chromeOptions);
        try {
            // Navigate to Url
            driver.get("https://www.selenium.dev/documentation/webdriver/elements/information/");
            log.info("title:{}", driver.getTitle());
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        } finally {
            driver.quit();
        }
    }
}
