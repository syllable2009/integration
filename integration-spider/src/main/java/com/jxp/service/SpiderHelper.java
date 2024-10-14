package com.jxp.service;

import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.webmagic.DefaultPipeline;
import com.jxp.webmagic.DefaultProcessor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:44
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpiderHelper {

    private SingleAddressReq req;

    private PageProcessor processor;

    private Downloader downloader;

    private Pipeline pipeline;

    public void run() {
        if (null == downloader) {
            downloader = new HttpClientDownloader();
        }
        if (null == processor) {
            processor = new DefaultProcessor();
        }
        if (null == pipeline) {
            pipeline = new DefaultPipeline();
        }
        // processor请使用DefaultPipeline，会带入配置和对象转化
        Spider.create(processor)
                .addUrl(req.getUrl())
                //开启1个线程抓取
                .thread(1)
                .setDownloader(downloader)
                .addPipeline(pipeline)
                //启动爬虫
                .run();
    }
}
