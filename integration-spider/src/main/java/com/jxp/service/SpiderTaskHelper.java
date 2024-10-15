package com.jxp.service;

import static us.codecraft.webmagic.model.HttpRequestBody.ContentType.FORM;

import org.apache.commons.lang3.StringUtils;

import com.jxp.dto.bo.RecommendCrawlerTaskData;
import com.jxp.webmagic.DefaultPipeline;
import com.jxp.webmagic.TaskProcessor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant.Method;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-15 11:30
 */
@Builder
@Data
@AllArgsConstructor
public class SpiderTaskHelper {

    private RecommendCrawlerTaskData taskData;

    private PageProcessor processor;

    private Downloader downloader;

    private Pipeline pipeline;

    public void run() {
        if (null == downloader) {
            downloader = new HttpClientDownloader();
        }
        if (null == processor) {
            processor = TaskProcessor.builder()
                    .taskData(taskData)
                    .build();
        }
        if (null == pipeline) {
            pipeline = new DefaultPipeline();
        }
        Request request = new Request(taskData.getLink());

        String requestMethod = Method.GET;
        if (StringUtils.equals("POST", taskData.getRequestMethod())) {
            requestMethod = Method.POST;
            HttpRequestBody body = new HttpRequestBody();
            if (StringUtils.isBlank(taskData.getRequestContentType())) {
                taskData.setRequestContentType(FORM);
            }
            // 赋值POST特有的的请求ContentType
            body.setContentType(taskData.getRequestContentType());
            if (StringUtils.isNotBlank(taskData.getRequestBody())) {
                body.setBody(taskData.getRequestBody().getBytes());
            } else {
                body.setBody("".getBytes());
            }
            request.setRequestBody(body);
        }
        // 赋值请求METHOD
        request.setMethod(requestMethod);
        // processor请使用DefaultPipeline，会带入配置和对象转化
        Spider.create(processor)
                .addRequest(request)
                //开启1个线程抓取
                .thread(1)
                .setDownloader(downloader)
                .addPipeline(pipeline)
                //启动爬虫
                .run();
    }
}
