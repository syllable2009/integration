package com.jxp.integration.test.spider.processor;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;


/**
 * @author jiaxiaopeng
 * Created on 2023-07-13 15:05
 */
@Slf4j
public class NetBianDownProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            //            .addHeader("referer", "https://github.com")
            .addHeader("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/112.0.0.0 Safari/537.36");

    private static String domain = "http://www.netbian.com";
    private static String BASE_URL = "http://www.netbian.com/meinv/index_{}.htm";
    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        Html html = page.getHtml();
        String s1 = html.xpath("/html/body/div[2]/div[2]/div[3]//div[@class='pic']/p/a/img/@src").get();
        String path = "/Users/jiaxiaopeng/netbian";
        HttpUtil.downloadFile(s1, path + File.separator + IdUtil.simpleUUID() + ".jpg");
    }

}
