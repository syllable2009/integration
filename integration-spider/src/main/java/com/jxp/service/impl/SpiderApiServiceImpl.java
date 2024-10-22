package com.jxp.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.jxp.dto.bo.CrawlerDomainConfig;
import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.jxp.dto.bo.CrawlerTaskDataConfig;
import com.jxp.dto.bo.RecommendCrawlerTaskData;
import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.dto.bo.SingleAddressResp;
import com.jxp.dto.bo.SpiderTaskResp;
import com.jxp.dto.bo.TaskAddressReq;
import com.jxp.service.SpiderApiService;
import com.jxp.service.SpiderHelper;
import com.jxp.service.SpiderTaskHelper;
import com.jxp.webmagic.CustomSelector;
import com.jxp.webmagic.DefaultProcessor;
import com.jxp.webmagic.FileDownloader;
import com.jxp.webmagic.LoginService;
import com.jxp.webmagic.PlaywrightDownloader;
import com.jxp.webmagic.TaskProcessor;
import com.jxp.webmagic.processor.ProcessorFactory;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:29
 */

@Slf4j
@Service
public class SpiderApiServiceImpl implements SpiderApiService {

    @Resource
    private Pipeline defaultPipeline;
    @Resource
    private LoginService loginService;
    @Resource
    private FileDownloader fileDownloader;
    @Resource
    private CustomSelector customSelector;
    @Resource
    private ProcessorFactory processorFactory;
    @Resource
    private Map<String, CrawlerMetaDataConfig> crawlerMetaDataConfigMap;
    @Resource
    private Map<String, CrawlerTaskDataConfig> crawlerTaskDataConfigMap;
    @Resource
    private Map<String, CrawlerDomainConfig> crawlerDomainDataConfigMap;

    @Override
    public SingleAddressResp parse(SingleAddressReq req, String userId) {
        // 参数校验
        req.setUrl(StrUtil.trim(req.getUrl()));
        String url = req.getUrl();
        log.info("------>spider start parse,url:{},userId:{}", url, userId);
        if (StringUtils.isBlank(url)) {
            log.error("<------spider stop parse,url is blank,url:{},userId:{}", url, userId);
            return null;
        }
        req.setUserId(userId);
        if (StringUtils.isBlank(req.getBase())) {
            req.setBase("singleAddress");
        }
        // 获取域，后续根据域拦截，统计或者查找解析器
        String domain = UrlUtils.getDomain(url);
        req.setDomain(domain);

        // 获取配置
        CrawlerMetaDataConfig config = getMetaConfig(req);
        // 获取解析器
        DefaultProcessor defaultProcessor = getMetaProcessor(req, config, null);
        // 获取下载器
        Downloader downloader = getMetaDownloader(req);
        // 获取pipeline
        Pipeline pipeline = getMetaPipeline();

        // 流程框架处理
        SingleAddressResp processorData = parseRun(req, downloader, defaultProcessor, pipeline);
        if (null == processorData) {
            log.error("<------spider parse fail,processorData is blank,url:{},userId:{}", url, userId);
            return null;
        }
        // 完善数据，这里是给前端同步返回的，真正的数据处理在pipeline处理
        fillData(processorData, req, domain, config);
        // 摘要
        improveDesc(processorData, req, config);
        // 统一在此处理封面,bean被spring管理，不想和webmagic框架混合
        improveCoverPic(processorData, req, config);
        // 下载并转换额外的图片附件，每个请求享有单独的超时时间
        downloadPicList(processorData);
        log.info("<------spider end parse,url:{},userId:{}", url, userId);
        return processorData;
    }

    private CrawlerMetaDataConfig getMetaConfig(SingleAddressReq req) {
        CrawlerMetaDataConfig config = null;
        String configName = StrUtil.isNotBlank(req.getConfigName()) ? req.getConfigName() : req.getDomain();
        // 按照domain获取配置，结合请求对象构造最终的配置对象，优先级：default < kconf < request
        if (StrUtil.isNotBlank(configName)) {
            // 后台按照域配置的解析器，如果有不同的分类，需要在请求中指定
            config = crawlerMetaDataConfigMap.get(configName);
        }
        if (null == config) {
            config = crawlerMetaDataConfigMap.get("default");
            configName = "default";
        }
        req.setConfigName(configName);
        return config;
    }

    private DefaultProcessor getMetaProcessor(SingleAddressReq req, CrawlerMetaDataConfig config, Site site) {
        // 解析器扩展，可自定义解析器
        final String processorName = StrUtil.isNotBlank(req.getProcessorName()) ? req.getProcessorName() : req.getDomain();
        DefaultProcessor defaultProcessor = processorFactory.getDefaultProcessor(processorName);
        if (defaultProcessor == null) {
            defaultProcessor = DefaultProcessor.builder().build();
        }
        defaultProcessor.setConfig(config);
        defaultProcessor.setSite(site);
        defaultProcessor.setReq(req);
        defaultProcessor.setFileDownloader(fileDownloader);
        defaultProcessor.setSelector(customSelector);
        req.setProcessorName(defaultProcessor.getName());
        return defaultProcessor;
    }

    private Downloader getMetaDownloader(SingleAddressReq req) {
        return PlaywrightDownloader.builder()
                .loginService(loginService)
                .config(crawlerDomainDataConfigMap.getOrDefault(req.getDomain(),
                        CrawlerDomainConfig.builder().ifNeedLogin(false).ifNeedLogin(false).build()))
                .build();
    }

    private Pipeline getMetaPipeline() {
        return defaultPipeline;
    }

    @Override
    public SingleAddressResp parseRun(SingleAddressReq req, Downloader downloader,
            PageProcessor processor, Pipeline pipeline) {

        SpiderHelper spiderHelper = SpiderHelper.builder()
                .req(req)
                .downloader(downloader)
                .processor(processor)
                .pipeline(pipeline)
                .build();
        try {
            spiderHelper.run();
            DefaultProcessor executeProcessor = (DefaultProcessor) spiderHelper.getProcessor();
            if (null == executeProcessor) {
                return null;
            }
            return executeProcessor.getProcessorData();
        } catch (Exception e) {
            log.error("spider exception,url:{}", req.getUrl(), e);
        }
        return null;
    }

    @Override
    public SpiderTaskResp taskSpiderRun(RecommendCrawlerTaskData taskData, CrawlerTaskDataConfig config) {
        if (null == taskData) {
            log.error("spider task handle fail,data not found");
            return null;
        }
        if (null == config) {
            log.error("spider task handle fail,config not found,aid:{},domain:{},url:{}",
                    taskData.getAid(),
                    taskData.getDomain(),
                    taskData.getLink());
            return null;
        }

        String processorName = StrUtil.isNotBlank(taskData.getProcessor()) ? taskData.getProcessor() : UrlUtils.getDomain(taskData.getLink());
        CrawlerDomainConfig crawlerDomainConfig = crawlerDomainDataConfigMap.get(processorName);
        if (null == crawlerDomainConfig) {
            crawlerDomainConfig = crawlerDomainDataConfigMap.get("default");
        }

        SpiderTaskHelper spiderHelper = SpiderTaskHelper.builder()
                .taskData(taskData)
                .downloader(PlaywrightDownloader.builder()
                        .loginService(loginService)
                        .config(crawlerDomainConfig)
                        .build())
                .processor(TaskProcessor.builder()
                        .config(config)
                        .site(null)
                        .taskData(taskData)
                        .build())
                .pipeline(defaultPipeline)
                .build();
        try {
            spiderHelper.run();
            TaskProcessor processor = (TaskProcessor) spiderHelper.getProcessor();
            if (null == processor) {
                return null;
            }
            return processor.getProcessorData();
        } catch (Exception e) {
            log.error("spider task handle exception,aid:{}", taskData.getAid(), e);
        }
        return null;
    }

    @Override
    public SpiderTaskResp taskParseRun(TaskAddressReq req, String userId) {
        req.setUrl(StrUtil.trim(req.getUrl()));
        String url = req.getUrl();
        log.info("------>spider task start parse,url:{},userId:{}", url, userId);
        if (null == req || StrUtil.isBlank(url)) {
            log.error("<------spider task handle fail,data not found");
            return null;
        }
        String domain = UrlUtils.getDomain(url);
        CrawlerTaskDataConfig config = null;
        String processor = StrUtil.isNotBlank(req.getProcessor()) ? req.getProcessor() : domain;
        // 按照domain获取配置，结合请求对象构造最终的配置对象，优先级：default < kconf < request
        if (StrUtil.isNotBlank(processor)) {
            // 后台按照域配置的解析器，如果有不同的分类，需要在请求中指定
            config = crawlerTaskDataConfigMap.get(processor);
        }
        if (null == config) {
            log.error("spider task handle fail,config not found");
            return null;
        }
        return taskSpiderRun(RecommendCrawlerTaskData.builder()
                .aid(0L)
                .link(req.getUrl())
                .build(), config);
    }

    private void fillData(SingleAddressResp processorData, SingleAddressReq req, String domain, CrawlerMetaDataConfig config) {
        if (null == processorData) {
            return;
        }
        processorData.setBase(req.getBase());
        processorData.setDomain(domain);
        processorData.setUrl(req.getUrl());
        processorData.setIfRepeated(false);
        processorData.setUserId(req.getUserId());
        processorData.setConfigName(req.getConfigName());
        processorData.setProcessorName(req.getProcessorName());
    }

    private void improveDesc(SingleAddressResp processorData, SingleAddressReq req, CrawlerMetaDataConfig config) {
        if (null == processorData) {
            return;
        }
    }

    private void downloadPicList(SingleAddressResp processorData) {
        if (null == processorData || CollectionUtils.isEmpty(processorData.getPicList())) {
            return;
        }
    }

    private void improveCoverPic(SingleAddressResp processorData, SingleAddressReq req, CrawlerMetaDataConfig config) {
        if (null == processorData) {
            return;
        }
    }
}
