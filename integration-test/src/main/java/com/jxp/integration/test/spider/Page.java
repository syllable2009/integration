package com.jxp.integration.test.spider;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;

import cn.hutool.core.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.selector.Html;

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
        chromeOptions.setLogLevel(ChromeDriverLogLevel.OFF);
        chromeOptions.addArguments(
                "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/112.0.0.0 Safari/537.36");
        chromeOptions.addArguments("--user-data-dir=/Users/Default");

        chromeOptions.addArguments("--headless"); //无浏览器模式
        chromeOptions.addArguments("--disable-gpu"); // 谷歌文档提到需要加上这个属性来规避bug
        chromeOptions.addArguments("--disable-software-rasterizer"); //禁用3D软件光栅化器
        chromeOptions.addArguments("--no-sandbox");// 为了让linux root用户也能执行
        chromeOptions.addArguments("--disable-dev-shm-usage"); //解决在某些VM环境中，/dev/shm分区太小，导致Chrome失败或崩溃
        //        options.addArguments("blink-settings=imagesEnabled=false"); //禁止加图片,如果爬取图片的话,这个不能禁用

        chromeOptions.addArguments("--disable-plugins"); //禁用插件,加快速度
        chromeOptions.addArguments("--disable-extensions"); //禁用扩展
        chromeOptions.addArguments("--disable-popup-blocking"); //关闭弹窗拦截
        chromeOptions.addArguments("--ignore-certificate-errors"); //  禁现窗口最大化
        chromeOptions.addArguments("--allow-running-insecure-content");  //关闭https提示 32位

//        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("allow-running-insecure-content", "--test-type");
        // 不加参数容易403
        chromeOptions.addArguments("--remote-allow-origins=*");
        // 配置参数禁止显示“Chrome正在受到自动软件的控制
        chromeOptions.addArguments("--disable-infobars");
        //        options.addArguments("--incognito") ; //无痕模式
        // 隐藏滚动条, 应对一些特殊页面
        chromeOptions.addArguments("--hide-scrollbars");
        // 阻止弹出窗口
        chromeOptions.setExperimentalOption("excludeSwitches",
                Arrays.asList("disable-popup-blocking"));
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().window().setSize(new Dimension(1024,768));
        try {
            // Navigate to Url
            driver.get("https:///#/official/social/job-info/17047");
            log.info("title:{}", driver.getTitle());
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

            Html html = new Html(driver.getPageSource());

            String s = html.xpath("//*[@id=root]/div/div/div[2]/div[1]/div[2]/div[3]/div[2]/pre/text()").get();
            //            List<String> all = plainText.all();
            //            all.forEach(e -> log.info("jxp:{}", e));
            log.info("hahhahahha:{}", s);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            String js = "return navigator.userAgent";
            Object o = executor.executeScript(js);
            log.info("userAgent:{}", o.toString());

            File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // 放到指定路径下
            FileUtil.copy(file, FileUtil.file("/Users/jiaxiaopeng/shoot.png"), true);
        } finally {
            driver.quit();
        }
    }
}
