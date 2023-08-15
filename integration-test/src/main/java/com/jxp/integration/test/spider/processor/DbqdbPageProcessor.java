package com.jxp.integration.test.spider.processor;

import java.io.File;
import java.util.List;

import com.jxp.integration.test.model.Dbqdb;
import com.jxp.integration.test.spider.pipeline.JuejinPipeline;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;


/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class DbqdbPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            //            .addHeader("referer", "https://github.com")
            .addHeader("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/112.0.0.0 Safari/537.36");

    private static String BASE_URL = "http://www.dbbqb.com/api/template?start={}&limit={}";


    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {

        Json json = page.getJson();
        List<Dbqdb> dbqdbs = json.toList(Dbqdb.class);

        String path = "/Users/jiaxiaopeng/gaoxiaotupian";
        boolean exist = FileUtil.exist(path);
        if (!exist) {
            FileUtil.mkdir(path);
        }
        dbqdbs.forEach(e -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            HttpUtil.downloadFile(e.getThumbnail(), path + File.separator + e.getTitle() + ".gif");
        });


    }

    public static void main(String[] args) {

        String url = StrUtil.format(BASE_URL, 0, 120);

        Spider.create(new DbqdbPageProcessor())
                .addUrl(url)
                //开启5个线程抓取
                .thread(1)
                .addPipeline(new JuejinPipeline())
                //启动爬虫
                .run();
    }
}
