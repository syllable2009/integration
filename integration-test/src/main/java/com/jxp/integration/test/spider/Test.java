package com.jxp.integration.test.spider;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 14:45
 */
@Slf4j
public class Test {

    public static void main(String[] args) throws InterruptedException {

        //        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        System.setProperty("webdriver.chrome.driver", "/Users/jiaxiaopeng/Downloads/chromedriver_mac64-2/chromedriver");
        // 谷歌驱动
        ChromeOptions options = new ChromeOptions();
        // 允许所有请求
        options.addArguments("--remote-allow-origins=*");
        //        options.addArguments("--headless"); //无浏览器模式
        options.addArguments("--disable-gpu"); // 谷歌文档提到需要加上这个属性来规避bug
        options.addArguments("--disable-software-rasterizer"); //禁用3D软件光栅化器
        options.addArguments("--no-sandbox");// 为了让linux root用户也能执行
        options.addArguments("--disable-dev-shm-usage"); //解决在某些VM环境中，/dev/shm分区太小，导致Chrome失败或崩溃
        options.addArguments("blink-settings=imagesEnabled=false"); //禁止加图片,如果爬取图片的话,这个不能禁用
        //        options.addArguments("--incognito") ; //无痕模式
        options.addArguments("--disable-plugins"); //禁用插件,加快速度
        options.addArguments("--disable-extensions"); //禁用扩展
        //        options.addArguments("--disable-popup-blocking"); //关闭弹窗拦截
        //        options.addArguments("--ignore-certificate-errors"); //  禁现窗口最大化
        options.addArguments("--allow-running-insecure-content");  //关闭https提示 32位
        options.addArguments("--disable-infobars");  //禁用浏览器正在被自动化程序控制的提示,但是高版本不生效


        //        options.addArguments("Cookie='enabledapps.uploader=0; _did=web_35512701325345A6;
        //        ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e;
        //        hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b;
        //        did=web_b0b09f485cdbba02cc31ba7871dcf1359720; Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476;
        //        _ga_VKXBFL78SD=GS1.1.1678333927.1.1.1678333945.42.0.0;
        //        intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6;
        //        logged_out_marketing_header_id
        //        =eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; didv=1689240364810; docs-gray-vodka=%7B%22type%22%3A%22reader%22%2C%22grayInfo%22%3A%7B%22html%22%3A%2222915521.html%22%7D%2C%22desc%22%3A%22%E6%8F%92%E4%BB%B6%E4%BA%8C%E6%9C%9F%EF%BC%8C%E6%94%AF%E6%8C%81%E8%8F%9C%E5%8D%95%22%2C%22demandId%22%3Anull%7D; docs-gray-tequila=%7B%22type%22%3A%22creator%22%2C%22grayInfo%22%3A%7B%22html%22%3A%2222850132.html%22%7D%2C%22desc%22%3A%22%E5%A4%9A%E9%80%89%E4%B8%8B%E6%8B%89%E5%88%97%E8%A1%A8%E9%9C%80%E6%B1%82%22%2C%22demandId%22%3Anull%7D; _ga_F6CM1VE30P=GS1.1.1691142494.8.1.1691142912.0.0.0; _ga=GA1.2.2027780773.1664422375; accessproxy_session=d09af50f-782d-47a4-b9c9-bf021bf7e45d; KXID=K_TKEKSPnVPD6DmHaqpOrjZLvFmxx4LR3yHOUYgWPbLDpMJa2LmXRSHDDZAby6Bh9VVY4ww7JAdpeaUaM4kFbT96V8Uy0KKU0IutMw7ekMEe3DGhJa7bAGgdV9QgZ_si_KqdE2xSfYuepSbmBiNIr-7HRbnuZboFpzY9Dn-uJ4k=; lbcookie=0'");


        WebDriver webDriver = new ChromeDriver(options);


        // 访问登录地址
        // 刷新页面
//        webDriver.navigate().refresh();
        Set<Cookie> cookies = webDriver.manage().getCookies();
        log.info("**********************************");
        cookies.forEach(e -> log.info("cookie:{},value:{}", e.getName(), e.getValue()));
        log.info("**********************************");

        //        webDriver.manage().addCookie(new Cookie("accessproxy_session",
        //        "8189deda-4b77-417b-992e-0f4e432d828e"));

        // 启动需要打开的网页
        //        webDriver.get("https://www.baidu.com");
        webDriver.navigate().to("https://#/official/social/?workLocationCode=domestic");

        //操作浏览器 获取到输入框
        //        WebElement kk = webDriver.findElement(By.id("kw"));
        //        //然后向百度输入框输入selenium java
        //        kk.sendKeys("jiaxiaopeng");
        //        //通过元素属性id=su找到百度一下搜索按钮
        //        WebElement btn = webDriver.findElement(By.id("su"));
        //对按钮进行点击操作
        //        btn.click();
        //浏览器窗口最大化
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
//        WebElement kk = webDriver.findElement(By.id("username_sso"));
//        kk.sendKeys("jiaxiaopeng");
//        WebElement kp = webDriver.findElement(By.id("password_sso"));
//        kp.sendKeys("Jxp@13261573576");
//        By xpath = By.xpath("//input[@class=btn btn-submit btn-block]");
//        WebElement element = xpath.findElement(webDriver);
//        element.click();
//        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        Set<Cookie> cookies2 = webDriver.manage().getCookies();
        log.info("**********************************");
        cookies2.forEach(e -> log.info("cookie:{},value:{}", e.getName(), e.getValue()));
        log.info("**********************************");
        //获取元素文本信息
//        String pageSource = webDriver.getPageSource();
        String js = "window.scrollTo(0, document.body.scrollHeight);";
//        new Actions(webDriver)
//                .moveToElement(webDriver.findElement(By.ByTagName("")))
        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        String data = webDriver.getPageSource();
        log.info("pageSource:{}", data);
        File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        // 放到指定路径下
        FileUtil.copy(file, FileUtil.file("/Users/jiaxiaopeng/shoot.png"), true);
//        webDriver.close();
        webDriver.quit();
        //        String urlStr = "https://mp.weixin.qq.com/s/BSjdf09Zu9gLscMRhtkXOA";
        //        URL url = URLUtil.url(urlStr);
        //        url.getHost();
        //        log.info("{}",url.getHost());
        //        String title;
        //        String link;
        //        String description;
        //        String domain;
        //        String biztype;
        //        String bizid;
        //        String category; // 分类
        //        String aid;
        //        String id;
        //        String md5;
        //        String vector;
        //        String cover;
        //        Integer state;  // 0 初始申请状态 // 1处理完成 2 删除

        //        String str =
        //                "_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; "
        //                        + "hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; "
        //                        + "did=web_b0b09f485cdbba02cc31ba7871dcf1359720; "
        //                        + "Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1
        //                        .1678333927.1.1"
        //                        + ".1678333945.42.0.0;
        //                        intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; "
        //                        + "logged_out_marketing_header_id"
        //                        +
        //                        "=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0";
        //
        //        List<String> split = StrUtil.split(str, ";");
        //        split.forEach(e ->
        //        {
        //            List<String> split1 = StrUtil.split(e, "=");
        //            log.info("e:{}", split1.get(0), split1.get(1));
        //        });


    }
}
