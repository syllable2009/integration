package com.jxp.integration.test.spider.processor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.jxp.integration.test.spider.pipeline.JuejinPipeline;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;


/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class NetBianProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            //            .addHeader("referer", "https://github.com")
            .addHeader("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/112.0.0.0 Safari/537.36");

    private static String domain = "http://www.netbian.com";
    private static String BASE_URL = "http://www.netbian.com/meinv/index_{}.htm";
    private static AtomicInteger atomicInteger = new AtomicInteger(28);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {

        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//*[@id=\"main\"]/div[4]/ul/li/a").nodes();

        nodes.forEach(e -> {
            String s = e.xpath("/a/@href").get();
            String newUrl = domain + s;
            Spider.create(new NetBianDownProcessor())
                    .addUrl(newUrl)
                    .thread(5)
                    .addPipeline(new JuejinPipeline())
                    .run();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            }

        });
        String target = StrUtil.format(BASE_URL, atomicInteger.addAndGet(1));
        log.info("add target: {}", target);
        page.addTargetRequest(target);
    }

    public static void main(String[] args) {
        String target = StrUtil.format(BASE_URL, atomicInteger.addAndGet(1));
        log.info("start target: {}", target);
        Spider.create(new NetBianProcessor())
                .addUrl(target)
                //开启5个线程抓取
                .thread(1)
                .addPipeline(new JuejinPipeline())
                //启动爬虫
                .run();
    }
}
