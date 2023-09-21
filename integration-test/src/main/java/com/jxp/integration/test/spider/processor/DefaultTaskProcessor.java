package com.jxp.integration.test.spider.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.jxp.integration.test.spider.domain.dto.CrawlerTaskDataConfig;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SpiderTaskResp;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
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
import us.codecraft.webmagic.utils.UrlUtils;

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
        log.info("spider task start parse,url:{}", page.getUrl());
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

        initTaskData(taskData);

        String contentType = taskData.getResponseContentType();
        List<String> link = Lists.newArrayList();
        List<String> cover = Lists.newArrayList();
        List<String> title = Lists.newArrayList();
        if (StrUtil.startWith(contentType, "application/json")) {
            Json json = page.getJson();
            if (null == json) {
                log.error("spider task json is null,{}", page.getUrl());
                return;
            }
            link = analysisByJsonPath(json, config.getLink());
            // 解析封面
            cover = analysisByJsonPath(json, config.getCover());
            // 解析标题
            title = analysisByJsonPath(json, config.getTitile());

        } else if (StrUtil.startWith(contentType, "text/xml")) {
            // TODO xml解析
            return;
        } else {
            Html html = page.getHtml();
            if (null == html) {
                log.error("spider task parse fail,html is null,{}", page.getUrl());
                return;
            }
            link = analysisElementList(html,
                    StringUtils.isBlank(config.getLinkMethod()) ? config.getMethod() : config.getLinkMethod(),
                    config.getLink());
            if (CollUtil.isEmpty(link)) {
                log.error("spider task parse fail,processor link list is empty,url,{}", page.getUrl());
                return;
            }
            if (StringUtils.isNotBlank(config.getLinkPrefix()) && CollUtil.isNotEmpty(link)) {
                link = link.stream()
                        .map(i -> URLUtil.completeUrl(config.getLinkPrefix(), i))
                        .collect(Collectors.toList());
            }
            // 解析封面
            cover = analysisElementList(html,
                    StringUtils.isBlank(config.getCoverMethod()) ? config.getMethod() : config.getCoverMethod(),
                    config.getCover());
            if (StringUtils.isNotBlank(config.getCoverPrefix()) && CollUtil.isNotEmpty(cover)) {
                cover = cover.stream()
                        .map(i -> URLUtil.completeUrl(config.getCoverPrefix(), i))
                        .collect(Collectors.toList());
            }

            title = analysisElementList(html,
                    StringUtils.isBlank(config.getTitileMethod()) ? config.getMethod() : config.getTitileMethod(),
                    config.getTitile());
        }

        if (cover.size() < link.size()) {
            cover = null;
        }

        if (title.size() < link.size()) {
            title = null;
        }

        List<SingleAddressReq> singleAddressReqList = Lists.newArrayList();
        String url = null;
        for (int i = 0; i < link.size(); i++) {
            url = link.get(i);
            singleAddressReqList.add(SingleAddressReq.builder()
                    .url(url)
                    .link(url)
                    .customCoverUrl(cover != null ? cover.get(i) : null)
                    .title(title != null ? title.get(i) : null)
                    .base("task")
                    .build());
        }
        processorData = SpiderTaskResp.builder()
                .singleAddressReqList(singleAddressReqList)
                .build();
        log.info("spider task success end process,url:{}", page.getUrl());
        return;
    }

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private static Site getDefaultSite() {
        return Site.me().setRetryTimes(3).setSleepTime(100)
                .setRetrySleepTime(1000)
                .setTimeOut(30 * 1000)
                .addHeader("user-agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                                + "Chrome/112.0.0.0 Safari/537.36");
    }

    @Override
    public Site getSite() {
        if (null == site) {
            site = getDefaultSite();
            site.addHeader("referer", UrlUtils.getHost(taskData.getLink()));
        }
        return this.site;
    }


    private static List<String> analysisElementList(Html html, String method, String c) {
        if (null == html || StrUtil.isBlank(c)) {
            return Lists.newArrayList();
        }
        List<String> ret = null;
        if (StringUtils.equals("xpath", method)) {
            ret = html.xpath(c).all();
        } else if (StringUtils.equals("css", method)) {
            ret = html.$(c, "text").all();
        } else {
            ret = Lists.newArrayList();
        }
        return ret.stream()
                .map(e -> StrUtil.trim(e))
                .collect(Collectors.toList());
    }

    private static List<String> analysisByJsonPath(Json json, String path) {
        if (json == null || StrUtil.isBlank(path)) {
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

    private static void initTaskData(RecommendCrawlerTaskData taskData) {
        if (StringUtils.isBlank(taskData.getResponseContentType())) {
            taskData.setResponseContentType("text/html");
        }
    }
}
