package com.jxp.integration.test.spider.helper;

import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;
import com.jxp.integration.test.spider.downloader.PlaywrightDownloader;
import com.jxp.integration.test.spider.pipeline.DefaultPipeline;
import com.jxp.integration.test.spider.processor.PlaywrightProcessor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:11
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpiderTaskHelper {

    private RecommendCrawlerTaskData taskData;

    private PageProcessor processor;

    private Downloader downloader;

    private Pipeline pipeline;

    public void run() {
        if (null == downloader) {
            downloader = new PlaywrightDownloader();
        }
        if (null == processor) {
            processor = new PlaywrightProcessor();
        }
        if (null == pipeline) {
            pipeline = new DefaultPipeline();
        }
        Spider.create(processor)
                .addUrl(taskData.getLink())
                //开启1个线程抓取
                .thread(1)
                .setDownloader(downloader)
                .addPipeline(pipeline)
                //启动爬虫
                .run();
    }
}
