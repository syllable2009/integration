package com.jxp.integration.test.spider.processor;

import java.util.List;

import com.jxp.integration.test.spider.pipeline.JuejinPipeline;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class GithubRepoPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
//            .addHeader("referer", "https://github.com")
            .addHeader("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/112.0.0.0 Safari/537.36");


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

        List<String> all = html.xpath("//div[@id=readme]//article/p/h1|h3|h2|div//tidyText()").all();

        log.info("html:{}",all);
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
        return site;
    }

    public static void main(String[] args) {
        //        System.setProperty("https.protocols", "TLSv1.2");
        Spider.create(new GithubRepoPageProcessor())
                //从"https://github.com/code4craft"开始抓
                //                .addUrl("https://github.com/code4craft/webmagic")
//                .addUrl("https://github.com/trending/java")
                .addUrl("https://github.com/redis/jedis")
                //开启5个线程抓取
                .thread(1)
                .addPipeline(new JuejinPipeline())
                //启动爬虫
                .run();
    }
}
