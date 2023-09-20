package com.jxp.integration.test.spider.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.jxp.integration.test.spider.domain.dto.CrawlerTaskDataConfig;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SpiderTaskResp;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:13
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class DefaultTaskProcessor implements PageProcessor {

    private Site site;

    // 返回对象
    private SpiderTaskResp processorData;

    // 配置的规则对象
    private CrawlerTaskDataConfig config;

    private RecommendCrawlerTaskData taskData;

    @Override
    public void process(Page page) {
        log.info("spider task start parse,url:{}", taskData.getLink());
        boolean downloadSuccess = page.isDownloadSuccess();
        if (!downloadSuccess) {
            log.error("spider task download page fail,url:{}", page.getUrl());
            return;
        }

        if (config == null) {
            log.error("spider task parse fail,config is null,url:{}", page.getUrl());
            return;
        } else {
            initConfig(this.config);
        }

        if (StringUtils.isBlank(taskData.getResponseContentType())) {
            taskData.setResponseContentType("text/html");
        }
        String contentType = taskData.getResponseContentType();
        List<String> content = Lists.newArrayList();
        List<String> cover = Lists.newArrayList();
        if (contentType.startsWith("application/json")) {
            Json json = page.getJson();
            if (null == json) {
                log.error("spider task json is null,{}", page.getUrl());
                return;
            }
            content = analysisByJsonPath(json, config.getLink());
            // 解析封面
            if (StringUtils.isNotBlank(config.getCover())) {
                cover = analysisByJsonPath(json, config.getCover());
            }
        } else if (contentType.startsWith("text/xml")) {
            // TODO xml解析
            return;
        } else {
            Html html = page.getHtml();
            if (null == html) {
                log.error("spider task parse fail,html is null,{}", page.getUrl());
                return;
            }
            if (StringUtils.isNotBlank(config.getLink())) {
                content = analysisElementList(html,
                        StringUtils.isBlank(config.getLinkMethod()) ? config.getMethod() : config.getLinkMethod(),
                        config.getLink());
            }
            // 解析封面
            if (StringUtils.isNotBlank(config.getCover())) {
                cover = analysisElementList(html,
                        StringUtils.isBlank(config.getCoverMethod()) ? config.getMethod() : config.getCoverMethod(),
                        config.getCover());
            }
        }
        if (CollectionUtils.isNotEmpty(content)) {
            if (StringUtils.isNotBlank(config.getPrefix())) {
                content = content.stream()
                        .map(i -> config.getPrefix() + i)
                        .collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(config.getCoverPrefix())) {
                cover = cover.stream()
                        .map(i -> config.getCoverPrefix() + i)
                        .collect(Collectors.toList());
            }
        } else {
            log.error("spider task parse fail,processor list is empty,url,{}", page.getUrl());
        }
        if (cover.size() < content.size()) {
            cover = null;
        }
        List<SingleAddressReq> singleAddressReqList = Lists.newArrayList();
        for (int i = 0; i < content.size(); i++) {
            singleAddressReqList.add(SingleAddressReq.builder()
                    .url(content.get(i))
                    .customCoverUrl(cover != null ? cover.get(i) : null)
                    .base("task")
                    .build());
        }
        processorData = SpiderTaskResp.builder()
                .singleAddressReqList(singleAddressReqList)
                .build();
        log.info("spider task success end process,url:{}", page.getUrl());
        return;
    }

    @Override
    public Site getSite() {
        if (null == site) {
            site = Site.me().setRetryTimes(3).setSleepTime(100)
                    .setRetrySleepTime(1000)
                    .setTimeOut(30 * 1000)
                    .addHeader("user-agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                                    + "Chrome/112.0.0.0 Safari/537.36");
        }
        return site;
    }


    private static List<String> analysisElementList(Html html, String method, String c) {
        if (StringUtils.equals("xpath", method)) {
            return html.xpath(c).all()
                    .stream()
                    .filter(e -> StringUtils.isNotBlank(e))
                    .collect(Collectors.toList());
        } else if (StringUtils.equals("css", method)) {
            return html.$(c, "text").all()
                    .stream()
                    .filter(e -> StringUtils.isNotBlank(e))
                    .collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }
    }

    private static List<String> analysisByJsonPath(Json json, String path) {
        if (json == null) {
            return Lists.newArrayList();
        }
        return json.jsonPath(path).all();
    }

    private static void initConfig(CrawlerTaskDataConfig config) {
        if (config == null) {
            return;
        }
        if (StringUtils.isBlank(config.getMethod())) {
            config.setMethod("xpath");
        }
    }
}
