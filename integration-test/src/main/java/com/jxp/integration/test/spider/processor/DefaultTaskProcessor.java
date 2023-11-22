package com.jxp.integration.test.spider.processor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.integration.test.spider.domain.dto.CrawlerConfigRow;
import com.jxp.integration.test.spider.domain.dto.CrawlerTaskDataConfig;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SpiderTaskResp;
import com.jxp.integration.test.spider.domain.dto.TaskProcessData;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;
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


    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    @SuppressWarnings("checkstyle:MagicNumber")
    private static Site getDefaultSite() {
        return Site.me().setRetryTimes(3).setSleepTime(200)
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
        }
        site.addHeader("referer", UrlUtils.getHost(taskData.getLink()));
        return this.site;
    }

    @SneakyThrows
    @Override
    public void process(Page page) {
        log.info("recommend spider task start parse,url:{}", page.getUrl());
        boolean downloadSuccess = page.isDownloadSuccess();
        if (!downloadSuccess) {
            log.info("recommend spider task download page fail,url:{}", page.getUrl());
            return;
        }

        if (config == null) {
            log.info("recommend spider task parse fail,config is null,url:{}", page.getUrl());
            return;
        } else {
            // task的配置会尤其是摘要图片的生成方式会透传给每条记录，因此task解析时类型和value会不同，类型会透传，根据value判断即可
            initConfig(this.config);
        }
        List<TaskProcessData> taskProcessData = null;
        String contentType = taskData.getResponseContentType();
        if (StringUtils.startsWith(contentType, "application/json")) {
            taskProcessData = processJson(page, config);
        } else if (StringUtils.startsWith(contentType, "text/xml")) {
            taskProcessData = processXml(page, config);
        } else {
            taskProcessData = processHtml(page, config);
        }
        // 数据校验
        if (CollectionUtils.isEmpty(taskProcessData)) {
            log.info("recommend spider task fail end process,taskProcessData is empty,url:{}", page.getUrl());
            return;
        }
        // 数据透传
        List<SingleAddressReq> singleAddressReqList = taskProcessData.stream()
                .map(e -> SingleAddressReq.builder()
                        .url(e.getLink())
                        .title(e.getTitle())
                        .description(e.getDesciption())
                        .customCoverUrl(e.getCover())
                        .loginId(taskData.getLoginId())
                        .userId(taskData.getUserId())
                        .base("task")
                        .descriptionType(config.getDescriptionType())
                        .customCoverType(config.getCoverType())
                        .ext(e.getExt())
                        .build())
                .collect(Collectors.toList());

        processorData = SpiderTaskResp.builder()
                .singleAddressReqList(singleAddressReqList)
                .build();

        log.info("recommend spider task success end process,url:{}", page.getUrl());
    }


    private static List<String> analysisElementList(Html html, String method, String c) {
        if (null == html || StringUtils.isBlank(c)) {
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
                .map(e -> StringUtils.trim(e))
                .filter(e -> StringUtils.isNotBlank(e))
                .collect(Collectors.toList());
    }

    private static List<String> analysisByJsonPath(Json json, String path) {
        if (json == null || StringUtils.isBlank(path)) {
            return Lists.newArrayList();
        }
        return json.jsonPath(path).all()
                .stream()
                .map(e -> StringUtils.trim(e))
                .filter(e -> StringUtils.isNotBlank(e))
                .collect(Collectors.toList());
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

    private static List<TaskProcessData> processHtml(Page page, CrawlerTaskDataConfig config) {
        List<TaskProcessData> ret = Lists.newArrayList();
        Html html = page.getHtml();
        if (null == html) {
            log.info("spider task parse fail,html is null,{}", page.getUrl());
            return ret;
        }
        List<TaskProcessData> taskProcessData = null;
        String group = config.getGroup();
        if (StringUtils.equals("nodeList", group)) {
            taskProcessData = parseByGroup(config, html);
        } else {
            // 此种方式只能解析链接，如果需要解析其他属性，参考nodeList配置，否则会出现字段取值和去空后的对应关系错乱问题
            taskProcessData = parseSimple(config, html);
        }
        return taskProcessData;
    }

    private static List<TaskProcessData> processXml(Page page, CrawlerTaskDataConfig c) {
        return Lists.newArrayList();
    }

    private static List<TaskProcessData> processJson(Page page, CrawlerTaskDataConfig config) {
        List<TaskProcessData> ret = Lists.newArrayList();
        Json json = page.getJson();
        if (null == json) {
            log.info("spider task processJson json is null,{}", page.getUrl());
            return ret;
        }
        List<String> linkList = analysisByJsonPath(json, config.getLink());
        if (CollectionUtils.isEmpty(linkList)) {
            return ret;
        }
        linkList.forEach(e -> ret.add(TaskProcessData.builder()
                .link(e)
                .build()));
        String linkPrefix = config.getLinkPrefix();
        if (StringUtils.isNotBlank(linkPrefix)) {
            ret.forEach(e -> {
                if (StringUtils.isNotBlank(e.getLink())) {
                    e.setLink(linkPrefix + e.getLink());
                }
            });
        }
        return ret;
    }


    private static List<TaskProcessData> parseSimple(CrawlerTaskDataConfig config, Html html) {
        List<TaskProcessData> ret = Lists.newArrayList();
        List<String> linkList = analysisElementList(html,
                StringUtils.isNotBlank(config.getLinkMethod()) ? config.getLinkMethod() : config.getMethod(),
                config.getLink());
        if (CollectionUtils.isEmpty(linkList)) {
            return ret;
        }
        linkList.forEach(e -> ret.add(TaskProcessData.builder()
                .link(e)
                .build()));
        String linkPrefix = config.getLinkPrefix();
        if (StringUtils.isNotBlank(linkPrefix)) {
            ret.forEach(e -> {
                if (StringUtils.isNotBlank(e.getLink())) {
                    e.setLink(linkPrefix + e.getLink());
                }
            });
        }
        return ret;
    }

    private static List<TaskProcessData> parseByGroup(CrawlerTaskDataConfig config, Html html) {
        List<TaskProcessData> ret = Lists.newArrayList();
        String groupMethod = config.getGroupMethod();
        if (StringUtils.isBlank(groupMethod)) {
            groupMethod = config.getMethod();
        }
        String groupValue = config.getGroupValue();
        if (StringUtils.isBlank(groupValue) || null == html) {
            return ret;
        }
        // 先解析出node节点列表
        List<Selectable> nodes = null;
        if (StringUtils.equals("xpath", groupMethod)) {
            nodes = html.xpath(groupValue).nodes();
        } else if (StringUtils.equals("css", groupMethod)) {
            nodes = html.$(groupValue, "text").nodes();
        } else {
            nodes = Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(nodes)) {
            return ret;
        }
        String method = config.getMethod();
        // 解析其他属性
        nodes.stream().filter(e -> null != e).forEach(e -> {
            String link = parseByNode(e,
                    StringUtils.isNotBlank(config.getLinkMethod()) ? config.getLinkMethod() : method,
                    config.getLink());
            if (StringUtils.isBlank(link)) {
                return;
            }
            ret.add(TaskProcessData.builder()
                    .link(link)
                    .title(parseByNode(e,
                            StringUtils.isNotBlank(config.getTitleMethod()) ? config.getTitleMethod() : method,
                            config.getTitle()))
                    .desciption(parseByNode(e,
                            StringUtils.isNotBlank(config.getDescriptionMethod()) ? config.getDescriptionMethod()
                                                                                  : method,
                            config.getDescription()))
                    .cover(parseByNode(e,
                            StringUtils.isNotBlank(config.getCoverMethod()) ? config.getCoverMethod() : method,
                            config.getCover()))
                    .ext(parseExt(e, config))
                    .build());
        });

        if (CollectionUtils.isEmpty(ret)) {
            return ret;
        }

        // 拼接链接前缀
        String linkPrefix = config.getLinkPrefix();
        if (StringUtils.isNotBlank(linkPrefix)) {
            ret.forEach(e -> {
                if (StringUtils.isNotBlank(e.getLink())) {
                    e.setLink(linkPrefix + e.getLink());
                }
            });
        }
        // 拼接图片前缀
        String coverPrefix = config.getCoverPrefix();
        if (StringUtils.isNotBlank(coverPrefix)) {
            ret.forEach(e -> {
                if (StringUtils.isNotBlank(e.getCover())) {
                    e.setCover(coverPrefix + e.getCover());
                }
            });
        }
        return ret;
    }

    private static String parseByNode(Selectable select, String method, String rule) {
        if (StringUtils.isBlank(rule) || null == select) {
            return null;
        }
        if (StringUtils.equals("xpath", method)) {
            return StringUtils.trim(select.xpath(rule).get());
        } else if (StringUtils.equals("css", method)) {
            return StringUtils.trim(select.$(rule, "text").get());
        } else {
            return null;
        }
    }

    private static Map<String, String> parseExt(Selectable select, CrawlerTaskDataConfig config) {
        if (CollectionUtils.isEmpty(config.getExtParseList()) || null == select) {
            return null;
        }
        Map<String, String> extMap = Maps.newHashMap();
        String method = config.getMethod();
        List<CrawlerConfigRow> extParseList = config.getExtParseList();
        extParseList.forEach(e -> {
            String value = parseByNode(select, StringUtils.isNotBlank(e.getParseMethod()) ? e.getParseMethod() : method,
                    e.getParseExpression());
            if (StringUtils.isBlank(value)) {
                return;
            }
            if (StringUtils.isNotBlank(e.getParsePrefix())) {
                value = e.getParsePrefix() + value;
            }
            extMap.put(e.getParseId(), value);
        });
        return extMap;
    }

}
