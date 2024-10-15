package com.jxp.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;
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
    private Map<String, CrawlerMetaDataConfig> crawlerMetaDataConfigMap;
    @Resource
    private Map<String, CrawlerTaskDataConfig> crawlerTaskDataConfigMap;

    @Override
    public SingleAddressResp parse(SingleAddressReq req, String userId) {
        // 参数校验
        req.setUrl(StrUtil.trim(req.getUrl()));
        @NotEmpty String url = req.getUrl();
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
        // 域白名单管理: whitelistSwitch=true表示打开白名单控制
        // 重复校验：url和link,thirdId为了解决相同地址下不同的id的问题
//        RecommendCrawlerMetaData dbMetaData =
//                recommendCrawlerMetaDataService.getByUrlOrLink(domain, url, req.getThirdId());
        // 根据domain获取配置
        CrawlerMetaDataConfig config = null;
        String processor = StrUtil.isNotBlank(req.getProcessor()) ? req.getProcessor() : domain;
        // 按照domain获取配置，结合请求对象构造最终的配置对象，优先级：default < kconf < request
        if (StrUtil.isNotBlank(processor)) {
            // 后台按照域配置的解析器，如果有不同的分类，需要在请求中指定
            config = crawlerMetaDataConfigMap.get(processor);
        }
        if (null == config) {
            config = CrawlerMetaDataConfig.builder()
                    .name("default")
                    .build();
        } else {
            // 设置解析器名字
            config.setName(processor);
        }
        config.setDomain(domain);
        // 准备请求配置
        SingleAddressResp processorData = parseRun(req, config, null);
        if (null == processorData) {
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
        // db存储
        if (null != processorData) {
            LocalDateTime now = LocalDateTime.now();
            // 图片落库
        } else {
            log.error("spider fail,processorData is null,url:{},userId:{}", url, userId);
        }
        log.info("<------spider end parse,url:{},userId:{}", url, userId);
        return processorData;
    }

    @Override
    public SingleAddressResp parseRun(SingleAddressReq req, CrawlerMetaDataConfig config, Site site) {
        SpiderHelper spiderHelper = SpiderHelper.builder()
                .req(req)
                .downloader(PlaywrightDownloader.builder()
                        .loginService(loginService)
                        .config(config)
                        .build())
                .processor(DefaultProcessor.builder()
                        .config(config)
                        .site(site)
                        .req(req)
                        .fileDownloader(fileDownloader)
                        .selector(customSelector)
                        .build())
                .pipeline(defaultPipeline)
                .build();
        try {
            spiderHelper.run();
            DefaultProcessor processor = (DefaultProcessor) spiderHelper.getProcessor();
            if (null == processor) {
                return null;
            }
            return processor.getProcessorData();
        } catch (Exception e) {
            log.info("spider exception,url:{},aid:{}", req.getUrl(), e);
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
        SpiderTaskHelper spiderHelper = SpiderTaskHelper.builder()
                .taskData(taskData)
                .downloader(PlaywrightDownloader.builder()
                        .loginService(loginService)
                        .config(CrawlerMetaDataConfig.builder()
                                .ifNeedLogin(false)
                                .ifProxy(false)
                                .domain(config.getDomain())
                                .build())
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
        if (null == req || StrUtil.isBlank(req.getUrl())) {
            log.error("spider task handle fail,data not found");
            return null;
        }
        String domain = UrlUtils.getDomain(req.getUrl());
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
        config.setDomain(domain);
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
