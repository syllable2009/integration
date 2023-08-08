package com.jxp.integration.test.spider.processor;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-03 20:37
 */

@Slf4j
public class TestWebclient {

    //文件版本,防止多线程缓存文件和用户文件共享,导致创建错误
    private static AtomicInteger fileSerial = new AtomicInteger(0);

//    public static void main(String[] args) throws Exception {
//
//        //        Runtime rt = Runtime.getRuntime();
//        ////        //        String[] commands = {"ls", "-lah"};
//        ////        String[] commands = {"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
//        ////                " --headless", " --disable-gpu", " --screenshot", "--window-size=1280,1696",
//        ////                " https:///article/5728"};
//        ////
//        ////        Process proc = rt.exec(commands);
//        ////
//        ////        BufferedReader stdInput = new BufferedReader(new
//        ////                InputStreamReader(proc.getInputStream()));
//        ////
//        ////        // Read the output from the command
//        ////        System.out.println("Here is the standard output of the command:\n");
//        ////        String s = null;
//        ////        while ((s = stdInput.readLine()) != null) {
//        ////            System.out.println(s);
//        ////        }
////        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
//        WebRequest webRequest = new WebRequest(new URL("https:///detail?entryId=1eaa22c8ce32426af0abd90081adcf46&uuid=41b42952fd694488b177e53c758b2d77"));
//        webRequest.setAdditionalHeader("Cookie",
//                "_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; "
//                        + "hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; "
//                        + "did=web_b0b09f485cdbba02cc31ba7871dcf1359720; "
//                        + "Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1.1678333927.1.1"
//                        + ".1678333945.42.0.0; intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; "
//                        + "logged_out_marketing_header_id"
//                        +
//                        "=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0");
//        webRequest.setAdditionalHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
//        WebClient webClient = test3();
//        Page page = webClient.getPage(webRequest);
//        webClient.waitForBackgroundJavaScript(30000); // 该方法阻塞
//        if (page != null && page.isHtmlPage()){
//        }
////
//        WebResponse webResponse = page.getWebResponse();
//        log.info("result:{}", webResponse.getContentAsString());
////        test2();
//    }

//    public static WebClient test3() throws Exception {
//        //            WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setTimeout(600 * 1000);
//        webClient.getOptions().setRedirectEnabled(true);
//        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//        webClient.getOptions().setUseInsecureSSL(true);
//        webClient.getOptions().setActiveXNative(false);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.getOptions().setCssEnabled(true);
//        webClient.setJavaScriptTimeout(600 * 1000);
//        webClient.waitForBackgroundJavaScript(600 * 1000);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.setRefreshHandler(new ImmediateRefreshHandler());
//        //            Assert.assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());
//    return webClient;
//    }

    private final static String driver = "webdriver.chrome.driver";
    private final static String chromeDriver =
            "/Users/jiaxiaopeng/Downloads/chromedriver_mac64_114/chromedriver";
    //    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";

    public static void test() {
        System.out.println("打开浏览器进行操作");
        System.setProperty(driver, chromeDriver);
        //获取控制 打开浏览器
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();//浏览器最大化
        //超时等待30秒
        Duration duration = Duration.ofSeconds(30);
        driver.manage().timeouts().implicitlyWait(duration);
        //跳转到百度浏览器
        driver.get("http://www.baidu.com/");
        //操作浏览器 获取到输入框
        WebElement kk = driver.findElement(By.id("kw"));
        //然后向百度输入框输入selenium java
        kk.sendKeys("如也");
        //通过元素属性id=su找到百度一下搜索按钮
        WebElement btn = driver.findElement(By.id("su"));
        //对按钮进行点击操作
        btn.click();
        String pageSource = driver.getPageSource();
        log.info("pageSource:{}", pageSource);
    }

    public static void test2() {
        // 谷歌驱动
        System.setProperty(driver, chromeDriver);
        ChromeOptions options = new ChromeOptions();
        //        options.addArguments("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        options.addArguments("--headless"); //无浏览器模式
        options.addArguments("--disable-gpu"); // 谷歌文档提到需要加上这个属性来规避bug
        options.addArguments("--disable-software-rasterizer"); //禁用3D软件光栅化器
        options.addArguments("--no-sandbox");// 为了让linux root用户也能执行
        // 优化参数
        options.addArguments("--disable-dev-shm-usage"); //解决在某些VM环境中，/dev/shm分区太小，导致Chrome失败或崩溃
        //        options.addArguments("blink-settings=imagesEnabled=false"); //禁止加图片,如果爬取图片的话,这个不能禁用
        //        options.addArguments("--disable-images");
        // 允许所有请求
        options.addArguments("--remote-allow-origins=*");

        String tmpdir = System.getProperty("java.io.tmpdir");
        String dir = tmpdir + File.separator + "chrome_file_data_cache" + File.separator + fileSerial.incrementAndGet();
        File file1 = new File(dir + File.separator + "data");
        if (file1.exists()) {
            file1.mkdirs();
        }
        File file2 = new File(dir + File.separator + "cache");
        if (file2.exists()) {
            file1.mkdirs();
        }

        options.addArguments("--user-data-dir=" + file1.getAbsolutePath()); //解决打开页面出现data;空白页面情况,因为没有缓存目录
        options.addArguments("--disk-cache-dir=" + file2.getAbsolutePath()); //指定Cache路径
        //        options.addArguments("--incognito") ; //无痕模式
        options.addArguments("--disable-plugins"); //禁用插件,加快速度
        options.addArguments("--disable-extensions"); //禁用扩展
        options.addArguments("--disable-popup-blocking"); //关闭弹窗拦截
        options.addArguments("--ignore-certificate-errors"); //  禁现窗口最大化
        options.addArguments("--allow-running-insecure-content");  //关闭https提示 32位
        options.addArguments("--disable-infobars");  //禁用浏览器正在被自动化程序控制的提示,但是高版本不生效

        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML,"
                + " like Gecko) \"\n"
                + "                            + \"Chrome/112.0.0.0 Safari/537.36");
        options.addArguments(
                "--Cookie=_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; "
                        + "hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; "
                        + "did=web_b0b09f485cdbba02cc31ba7871dcf1359720; "
                        + "Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1.1678333927.1.1"
                        + ".1678333945.42.0.0; intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; "
                        + "logged_out_marketing_header_id"
                        +
                        "=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0");
        //实例化
        WebDriver webDriver = new ChromeDriver(options);
        String str =
                "_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; "
                        + "hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; "
                        + "did=web_b0b09f485cdbba02cc31ba7871dcf1359720; "
                        + "Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1.1678333927.1.1"
                        + ".1678333945.42.0.0; intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; "
                        + "logged_out_marketing_header_id"
                        +
                        "=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0";

        List<String> split = StrUtil.split(str, ";");
        split.forEach(e -> {
            List<String> split1 = StrUtil.split(e, "=");
            log.info("e:{}", split1.get(0), split1.get(1));
            webDriver.manage().addCookie(new Cookie(split1.get(0), split1.get(1)));
        });
//

        // 启动需要打开的网页
        webDriver.navigate().to("https:///article/7411");
        //        //操作浏览器 获取到输入框
        //        WebElement kk = webDriver.findElement(By.id("kw"));
        //        //然后向百度输入框输入selenium java
        //        kk.sendKeys("jiaxiaopeng");
        //        //通过元素属性id=su找到百度一下搜索按钮
        //        WebElement btn = webDriver.findElement(By.id("su"));
        //        //对按钮进行点击操作
        //        btn.click();
        Duration duration = Duration.ofSeconds(30);
        webDriver.manage().timeouts().implicitlyWait(duration);
        String pageSource = webDriver.getPageSource();
        log.info("pageSource:{}", pageSource);
    }
}
