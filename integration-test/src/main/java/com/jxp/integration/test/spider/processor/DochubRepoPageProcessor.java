package com.jxp.integration.test.spider.processor;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;


/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class DochubRepoPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            //            .addHeader("referer", "https://github.com")
            .addHeader("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/112.0.0.0 Safari/537.36");

    //用来存储cookie信息
    private Set<Cookie> cookies;

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {

        // 选择器
        //        <h1 class="article-title" data-v-066b2e60="">
        //                多端登录如何实现踢人下线
        //                <!----> <!----></h1>
        Html html = page.getHtml();
        if (null == html) {
            log.error("html is null,{}", page.getUrl());
            return;
        }
        //        List<Selectable> nodes = html.xpath("//body//div[@class=Box]//article").nodes();
        //        nodes.forEach(e -> {
        //            log.info("{}",
        //                    e.xpath("//h2/a/span/text()").get() + StrUtil.trim(e.xpath("//h2/a/text()").get()));
        //        });

        Selectable header = html.xpath("//body//div[@class=post_container_title]");
        String title = header.xpath("//h1/text()").get();
        String date = header.xpath("/p/span[1]/text()").get();
        log.info("title:{},date:{}", title, date);
        // 内容
        Selectable article = html.xpath("//body//div[@class=post_container]/article");
        List<Selectable> nodes = article.xpath("//p").nodes();

        // 获取所有的链接和图片
        List<String> linkList = html.xpath("//body//div[@class=post_box]//div[@class=post_def_title]//a/@href").all();

        List<String> imgList = html.xpath("//body//div[@class=post_box]//a[@class=post_def_left]/img/@src").all();

        //

        // 部分二：定义如何抽取页面信息，并保存下来
        //        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        //        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()")
        //        .toString());
        //        if (page.getResultItems().get("name") == null) {
        //            //skip this page
        //            page.setSkip(true);
        //        }
        //        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        // 部分三：从页面发现后续的url地址来抓取
        //        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)")
        //        .all());
    }

    @Override
    public Site getSite() {
        //将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(), cookie.getValue().toString());
        }
        return site;
    }

    //使用 selenium 来模拟用户的登录获取cookie信息
    public void login() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://passport.csdn.net/account/login");
        driver.findElement(By.xpath("//div[@class='main-select']//ul//li[2]")).click();

        // 防止页面未能及时加载出来而设置一段时间延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("all")).sendKeys("username");
        driver.findElement(By.id("password-number")).sendKeys(
                "password");
        driver.findElement(By.xpath("//form//div//div[@class='form-group']//div//button")).click();

        // 防止页面未能及时加载出来而设置一段时间延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取cookie信息
        cookies = driver.manage().getCookies();
        driver.close();
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver",
                "/Users/jiaxiaopeng/chromedriver");
        DochubRepoPageProcessor p = new DochubRepoPageProcessor();
        try {
            p.login();
        }catch (Exception e){
            log.info("error,msg:{}",e);
        }

//        Spider.create(new DochubRepoPageProcessor())
//                //从"https://github.com/code4craft"开始抓
//                //                .addUrl("https://github.com/code4craft/webmagic")
//                //                .addUrl("https://github.com/trending/java")
//                //                .addUrl("https://www.dochub.wiki/awesome-mac/")
//
//                .addUrl("https://www.dochub.wiki/")
//                //开启5个线程抓取
//                .thread(1)
//                .addPipeline(new JuejinPipeline())
//                //启动爬虫
//                .run();
    }
}
