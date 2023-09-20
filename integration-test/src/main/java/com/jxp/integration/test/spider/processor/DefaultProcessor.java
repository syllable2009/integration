package com.jxp.integration.test.spider.processor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SingleAddressResp;
import com.jxp.integration.test.spider.selector.CustomSelector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:13
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class DefaultProcessor implements PageProcessor {

    private Site site;

    // 返回对象
    private SingleAddressResp processorData;

    // 配置的规则对象
    private CrawlerMetaDataConfig config;

    // 请求对象
    private SingleAddressReq req;

    @Override
    public void process(Page page) {
        log.info("spider start parse,url:{}", req.getUrl());
        boolean downloadSuccess = page.isDownloadSuccess();
        if (!downloadSuccess) {
            log.error("spider download page fail,url:{}", page.getUrl());
            return;
        }
        // 单页面都处理html页面
        Html html = page.getHtml();
        if (null == html) {
            log.error("spider parse html is null,{}", page.getUrl());
            return;
        }
        if (this.config == null) {
            // 默认解析实现
            processorData = handleByDefault(page);
        } else {
            initConfig(config);
            // 解析标题
            String title = parseTitle(req.getTitle(), config, html);
            // 解析链接
            String link = parseLink(req.getLink(), config, html);
            if (StringUtils.isBlank(link)) {
                link = req.getUrl();
            }
            // 解析正文
            List<String> content = parseContent(config, html);
            // 获取封面
            String cover = getCoverUrl(req.getCustomCoverUrl(), config, html);

            processorData = SingleAddressResp.builder()
                    .content(content)
                    .title(title)
                    .link(link)
                    .url(page.getUrl().get())
                    .cover(cover)
                    .build();
        }
        processorData.setId(UUID.randomUUID().toString().replace("-", ""));
        processorData.setState(0);
        processorData.setLoginId(req.getLoginId());
        processorData.setUserId(req.getUserId());
        page.putField("processorData", processorData);
        log.info("spider success end parse,url:{}", req.getUrl());
    }

    private static String parseTitle(String customTitle, CrawlerMetaDataConfig config, Html html) {
        if (StringUtils.isNotBlank(customTitle)) {
            return customTitle;
        }
        if (StringUtils.isBlank(config.getTitle())) {
            return html.getDocument().title();
        }
        return analysisElement(html,
                StringUtils.isBlank(config.getTitleMethod()) ? config.getMethod() : config.getTitleMethod(),
                config.getTitle());
    }

    private static String parseLink(String customLink, CrawlerMetaDataConfig config, Html html) {
        if (StringUtils.isNotBlank(customLink)) {
            return customLink;
        }
        if (StringUtils.isBlank(config.getLink())) {
            return null;
        }
        return analysisElement(html,
                StringUtils.isBlank(config.getLinkMethod()) ? config.getMethod() : config.getLinkMethod(),
                config.getLink());
    }

    private static List<String> parseContent(CrawlerMetaDataConfig config, Html html) {
        if (StringUtils.isBlank(config.getContent())) {
            return Lists.newArrayList();
        }
        return analysisElementList(html,
                StringUtils.isBlank(config.getContentMethod()) ? config.getMethod() : config.getContentMethod(),
                config.getContent());
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

    private String getCoverUrl(String customCoverUrl, CrawlerMetaDataConfig config, Html html) {
        if (StringUtils.isNotBlank(customCoverUrl)) {
            return customCoverUrl;
        }
        String cover = null;
        if (StringUtils.isNotBlank(config.getCover())) {
            switch (config.getCoverType()) {
                case "custom":
                    cover = analysisElement(html,
                            StringUtils.isBlank(config.getCoverMethod()) ? config.getMethod()
                                                                         : config.getCoverMethod(),
                            config.getCover());
                    if (StringUtils.isNotBlank(config.getCoverPrefix()) && StringUtils.isNotBlank(cover)) {
                        cover = config.getCoverPrefix() + cover;
                    }
                    break;
                case "sourceIdentity":
                    cover = null;
                    break;
                case "aigc":
                    cover = null;
                    break;
                case "customUrl":
                    cover = null;
                    break;
                case "none":
                    cover = null;
                    break;
                default:
                    cover = null;
            }
        }
        return cover;
    }

    private static String analysisElement(Html html, String method, String c) {
        if (StringUtils.equals("xpath", method)) {
            return StringUtils.trim(html.xpath(c).get());
        } else if (StringUtils.equals("css", method)) {
            return StringUtils.trim(html.$(c, "text").get());
        } else {
            return null;
        }
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

    private SingleAddressResp handleByDefault(Page page) {
        Html html = page.getHtml();
        List<String> content = html.selectDocumentForList(new CustomSelector());
        return SingleAddressResp.builder()
                .content(content)
                .title(StringUtils.isNotBlank(req.getTitle()) ? req.getTitle() : html.getDocument().title())
                .link(StringUtils.isNotBlank(req.getLink()) ? req.getLink() : page.getUrl().get())
                .url(page.getUrl().get())
                .cover(StringUtils.isNotBlank(req.getCustomCoverUrl()) ? req.getCustomCoverUrl() : null)
                .processor("default")
                .build();
    }

    private static void initConfig(CrawlerMetaDataConfig config) {
        if (config == null) {
            return;
        }
        if (StringUtils.isBlank(config.getMethod())) {
            config.setMethod("xpath");
        }
    }
}
