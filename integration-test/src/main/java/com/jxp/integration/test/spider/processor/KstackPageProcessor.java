package com.jxp.integration.test.spider.processor;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;
import com.jxp.integration.test.spider.pipeline.JuejinPipeline;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.PhantomJSDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class KstackPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .addHeader("referer", "https://github.com")
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");


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


        log.info("html:{}", html.get());
        // 部分二：定义如何抽取页面信息，并保存下来
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name") == null) {
//            //skip this page
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        // 部分三：从页面发现后续的url地址来抓取
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
    }

    @Override
    public Site getSite() {
        site.addHeader("Cookie", "_did=");
        return site;
    }

    public static void main(String[] args) {
//        System.getProperties().setProperty("webdriver.chrome.driver","/Users/jiaxiaopeng/chromedriver");
        Spider.create(new KstackPageProcessor())
                //从"https://github.com/code4craft"开始抓
//                .addUrl("https://github.com/code4craft/webmagic")
                .addUrl("https:///article/7411")
//                .addUrl("https:///tech/api/article/7411?articleStatus=PUBLISHED")
                //开启5个线程抓取
                .thread(1)
                .setDownloader(new PhantomJSDownloader("/Users/jiaxiaopeng/phantomjs-2.1.1-macosx/bin/phantomjs"))
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new JuejinPipeline())
                //启动爬虫
                .run();
    }

    private void login() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://www.example.com/login");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", "user"));
        params.add(new BasicNameValuePair("password", "password"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Header[] headers = response.getHeaders("Cookies");

    }
}
