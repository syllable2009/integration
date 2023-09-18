package com.jxp.integration.test.spider;

import com.jxp.integration.test.spider.downloader.PlaywrightDownloader;
import com.jxp.integration.test.spider.pipeline.JuejinPipeline;
import com.jxp.integration.test.spider.processor.GithubRepoPageProcessor;

import us.codecraft.webmagic.Spider;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 11:02
 */
public class PlaywrightMain {

    private static final String BASE_URL = "https://www.36kr.com/newsflashes/2368027471505793";

    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor())
                .addUrl(BASE_URL)
                //开启5个线程抓取
                .thread(1)
                .setDownloader(new PlaywrightDownloader())
                .addPipeline(new JuejinPipeline())
                //启动爬虫
                .run();
    }
}
