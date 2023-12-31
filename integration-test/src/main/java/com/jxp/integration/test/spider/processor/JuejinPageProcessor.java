package com.jxp.integration.test.spider.processor;

import com.jxp.integration.test.spider.pipeline.JuejinPipeline;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class JuejinPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .addHeader("referer", "https://github.com")
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

        Json json = page.getJson();
        if (null != json) {
            log.info("json:{}", json);
        }
        Html html = page.getHtml();
        if (null == html) {
            log.error("html is null,{}", page.getUrl());
            return;
        }
        Selectable select = html.xpath("//body");
        page.putField("title", StrUtil.trim(html.xpath("//body//h1[@class=article-title]/text()").get()));
        page.putField("author", StrUtil.trim(select.xpath("//div[@class=author-name]//a//span/text()").get()));
        page.putField("date", StrUtil.trim(select.xpath("//div[@class=meta-box]//time/text()").get()));
        page.putField("content", select.xpath("//div[@itemprop=articleBody]//div//p/text()").all());

        //        log.info("html:{}",s);
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


        String requestBody = "{\"id_type\":2,\"client_type\":2608,\"sort_type\":200,\"cursor\":\"1\",\"limit\":20}";

        Request request = new Request(
                "https://api.juejin.cn/recommend_api/v1/article/recommend_all_feed?aid=2608&uuid=6920105144927929863"
                        + "&spider=0");
        request.setMethod("POST");
        HttpRequestBody body = new HttpRequestBody();
        body.setContentType("application/json");
        body.setBody(requestBody.getBytes());
        request.setRequestBody(body);
        Spider.create(new JuejinPageProcessor())
                .addRequest(request)
                //从"https://github.com/code4craft"开始抓
                //                .addUrl("https://github.com/code4craft/webmagic")
                //                .addUrl("https://juejin.cn/post/7213598216884486204")
                //开启5个线程抓取
                .thread(1)
                .addPipeline(new JuejinPipeline())
                //启动爬虫
                .run();
    }
}
