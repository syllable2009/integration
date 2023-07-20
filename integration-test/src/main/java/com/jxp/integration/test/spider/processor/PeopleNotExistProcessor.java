package com.jxp.integration.test.spider.processor;

import java.io.File;

import com.google.common.collect.Lists;
import com.jxp.integration.test.spider.pipeline.PeopleNotExistPipeline;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class PeopleNotExistProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .addHeader("referer", "https://this-person-does-not-exist.com")
            .addHeader("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/112.0.0.0 Safari/537.36");

    private final static String BASE_URL =
            "https://this-person-does-not-exist.com/new?time={}&gender=female&age=26-35&etnic=asian";

    @SneakyThrows
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
        // List<String> CaseNo = new JsonPathSelector("$.list[*].CaseNo").selectList(page.getRawText());
        Json json = page.getJson();
        String src = json.jsonPath("$.src").get();
        String name = json.jsonPath("$.name").get();
        //        log.info("html:{}",s);
        // 部分二：定义如何抽取页面信息，并保存下来
        page.putField("src", src);
        page.putField("name", name);
        //        if (page.getResultItems().get("name") == null) {
        //            //skip this page
        //            page.setSkip(true);
        //        }
        //        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        String path = "/Users/jiaxiaopeng/this-person-does-not-exist";
        boolean exist = FileUtil.exist(path);
        if (!exist) {
            FileUtil.mkdir(path);
        }
        String downloadUrl = "https://this-person-does-not-exist.com" + src;
        // 下载地址
        HttpUtil.downloadFile(downloadUrl, path + File.separator + name);

        // 部分三：从页面发现后续的url地址来抓取
        //        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)")
        //        .all());
        int sleep = RandomUtil.randomInt(3300, 11000);
        Thread.sleep(sleep);
        log.info("random sleep,{}", sleep);
        log.info("addTargetRequests");
        page.addTargetRequests(Lists.newArrayList(StrUtil.format(BASE_URL, System.currentTimeMillis())));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws InterruptedException {
        String url = StrUtil.format(BASE_URL, System.currentTimeMillis());
        log.info("start spider:{}", url);
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // 可配置代理
//        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("localhost",8080)));
        Spider.create(new PeopleNotExistProcessor())
                //从"https://github.com/code4craft"开始抓
                //                .addUrl("https://github.com/code4craft/webmagic")
                .addUrl(url)
                //开启5个线程抓取
                .thread(1)
                .setDownloader(httpClientDownloader)
                .addPipeline(new PeopleNotExistPipeline())
                //                .setDownloader()
                //启动爬虫
                .run();
    }
}
