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
            .addHeader("referer","https://github.com")
            .addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");



    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {

        // 选择器
//        <h1 class="article-title" data-v-066b2e60="">
//                多端登录如何实现踢人下线
//                <!----> <!----></h1>
        Html html = page.getHtml();
        if (null == html){
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
        site.addHeader("Cookie","_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; did=web_b0b09f485cdbba02cc31ba7871dcf1359720; Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1.1678333927.1.1.1678333945.42.0.0; intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; logged_out_marketing_header_id=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0");
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
