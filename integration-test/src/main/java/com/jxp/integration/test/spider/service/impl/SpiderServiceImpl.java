package com.jxp.integration.test.spider.service.impl;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SingleAddressResp;
import com.jxp.integration.test.spider.domain.dto.SpiderTaskResp;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;
import com.jxp.integration.test.spider.downloader.PlaywrightDownloader;
import com.jxp.integration.test.spider.helper.SpiderHelper;
import com.jxp.integration.test.spider.processor.PlaywrightProcessor;
import com.jxp.integration.test.spider.service.SpiderService;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 15:59
 */
@Service
@Slf4j
public class SpiderServiceImpl implements SpiderService {

    @Resource
    PlaywrightDownloader playwrightDownloader;
    @Resource
    private Pipeline defaultPipeline;

    @Override
    public SingleAddressResp parse(SingleAddressReq req) {
        // 参数校验
        req.setUrl(StringUtils.trim(req.getUrl()));
        @NotEmpty String url = req.getUrl();
        log.info("spider start request,url:{}", url);
        if (StringUtils.isBlank(url)) {
            log.info("spider stop request,url is blank", url);
            return null;
        }
        // 获取配置
        CrawlerMetaDataConfig config = null;
        String domain = UrlUtils.getDomain(url);
        // 准备请求配置
        SingleAddressResp processorData = parseRun(req, config, null);
        // 按照domain获取配置，结合请求对象构造最终的配置对象，优先级：default < kconf < request
        String processorName = config == null ? "default" : domain;
        processorData.setProcessor(processorName);
        fillData(processorData);
        log.info("spider end request,url:{}", url);
        return processorData;
    }

    private static void fillData(SingleAddressResp processorData) {

    }

    @Override
    public SingleAddressResp parseRun(SingleAddressReq req, CrawlerMetaDataConfig config, Site site) {
        SpiderHelper spiderHelper = SpiderHelper.builder()
                .req(req)
                .downloader(playwrightDownloader)
                .processor(PlaywrightProcessor.builder()
                        .config(config)
                        .site(site)
                        .req(req)
                        .build())
                .pipeline(defaultPipeline)
                .build();
        spiderHelper.run();
        PlaywrightProcessor processor = (PlaywrightProcessor) spiderHelper.getProcessor();
        if (null == processor) {
            return null;
        }
        return processor.getProcessorData();
    }

    @Override
    public SpiderTaskResp taskSpiderRun(RecommendCrawlerTaskData req) {
        return null;
    }
}
