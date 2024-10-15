package com.jxp.webmagic;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.dto.bo.SingleAddressResp;

import cn.hutool.core.util.IdUtil;
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
import us.codecraft.webmagic.selector.Selector;
import us.codecraft.webmagic.utils.UrlUtils;


/**
 * @author jiaxiaopeng
 * Created on 2023-08-11 16:09
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

    private FileDownloader fileDownloader;

    private Selector selector;

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
            site.addHeader("referer", UrlUtils.getHost(req.getUrl()));
        }
        return this.site;
    }

    @SneakyThrows
    @Override
    public void process(Page page) {
        boolean downloadSuccess = page.isDownloadSuccess();
        if (!downloadSuccess) {
            log.error("spider download page fail,url:{}", page.getUrl());
            return;
        }
        log.info("spider process start,url:{}", page.getUrl());
        // 单页面都处理html页面
        Html html = page.getHtml();
        if (null == html) {
            log.error("spider process fail, html is null,{}", page.getUrl());
            return;
        }
        if (this.config == null) {
            // 默认解析实现
            processorData = handleByDefault(page);
        } else {
            initConfig(config);
            processorData = SingleAddressResp.builder()
                    .processorName(config.getName())
                    .content(parseContent(config, html, selector))
                    .title(parseTitle(null, config, html))
                    .description(parseDescription(req, config, html))
                    .link(parseLink(req, config, html))
                    .url(page.getUrl().get())
                    .cover(parseCover(req, config, html))
                    .thirdId(parseThirdId(req.getThirdId(), config, html))
                    .picList(parsePicList(config, html))
                    .ext(parseExt(req, config, html))
                    .build();
        }
        // 结尾校验，如果标题或者内容为空，则进行忽略
//        if (StringUtils.isBlank(processorData.getTitle())) {
//            log.error("spider process fail, parse title is empty,{}", page.getUrl());
//            processorData = null;
//            return;
//        }
        if (CollectionUtils.isEmpty(processorData.getContent())) {
            log.error("spider process fail, parse content is empty,{}", page.getUrl());
            processorData = null;
            return;
        }
        processorData.setUid(IdUtil.fastSimpleUUID());
        processorData.setState(0);
        processorData.setBase(req.getBase());
        page.putField("processorData", processorData);
        log.info("spider process end, process,url:{}", page.getUrl());
    }

    /**
     * 标题解析,可以是传入或者解析
     */
    private static String parseTitle(String customTitle, CrawlerMetaDataConfig config, Html html) {
        if (StringUtils.isNotBlank(customTitle)) {
            return customTitle;
        }
        String title = analysisElement(html,
                StringUtils.isNotBlank(config.getTitleMethod()) ? config.getTitleMethod() : config.getMethod(),
                config.getTitle());
        // 保底取值
        if (StringUtils.isBlank(title)) {
            title = html.getDocument().title();
        }
        return title;
    }

    /**
     * 解析链接，可以是传入或者解析，保底为url
     */
    private static String parseLink(SingleAddressReq req, CrawlerMetaDataConfig config, Html html) {
        String link = analysisElement(html,
                StringUtils.isBlank(config.getLinkMethod()) ? config.getMethod() : config.getLinkMethod(),
                config.getLink());

        if (StringUtils.isBlank(link)) {
            link = req.getUrl();
        } else {
            if (StringUtils.isNotBlank(config.getLinkPrefix())) {
                link = config.getLinkPrefix() + link;
            }
        }
        return link;
    }

    /**
     * 解析摘要，如果请求中存在，直接取值
     * 否则按照请求的摘要生成类型，配置的请求类型取值，当为parse时解析
     */
    private static String parseDescription(SingleAddressReq req, CrawlerMetaDataConfig config, Html html) {
        String descriptionType = req.getDescriptionType();
        if (StringUtils.isBlank(descriptionType)) {
            descriptionType = config.getDescriptionType();
        }
        if (!StringUtils.equals("parse", descriptionType)) {
            return null;
        }
        List<String> descList = analysisElementList(html,
                StringUtils.isNotBlank(config.getDescriptionMethod()) ? config.getDescriptionMethod()
                        : config.getMethod(),
                config.getDescription());
        if (CollectionUtils.isEmpty(descList)) {
            return null;
        }
        return Joiner.on("").skipNulls().join(descList);
    }

    /**
     * 解析封面，优先以请求request为主，否则取配置config的值
     * 如果不是封面的自定义类型不为解析，则直接返回空
     */
    private static String parseCover(SingleAddressReq req, CrawlerMetaDataConfig config,
            Html html) {
        String customCoverType = req.getCustomCoverType();
        if (StringUtils.isBlank(customCoverType) && null != config) {
            customCoverType = config.getCoverType();
        }
        if (null != config && StringUtils.isBlank(config.getCover()) || !StringUtils.equals("parse", customCoverType)) {
            return null;
        }
        String cover = analysisElement(html,
                StringUtils.isBlank(config.getCoverMethod()) ? config.getMethod() : config.getCoverMethod(),
                config.getCover());

        if (StringUtils.isNotBlank(config.getCoverPrefix()) && StringUtils.isNotBlank(cover)) {
            cover = config.getCoverPrefix() + cover;
        }
        return cover;
    }

    private static String parseThirdId(String thirdId, CrawlerMetaDataConfig config, Html html) {
        if (StringUtils.isNotBlank(thirdId)) {
            return thirdId;
        }
        if (StringUtils.isBlank(config.getThirdId())) {
            return null;
        }
        return analysisElement(html,
                StringUtils.isBlank(config.getThirdIdMethod()) ? config.getMethod() : config.getThirdIdMethod(),
                config.getThirdId());
    }

    /**
     * 解析正文，必须是解析出来的结果
     */
    private static List<String> parseContent(CrawlerMetaDataConfig config, Html html, Selector selector) {

        List<String> contentList = null;
        if (StringUtils.isNotBlank(config.getContent())) {
            contentList = analysisElementList(html,
                    StringUtils.isNotBlank(config.getContentMethod()) ? config.getContentMethod() : config.getMethod(),
                    config.getContent());
        }
        // 默认采用default保底
        if (CollectionUtils.isEmpty(contentList) && null != selector) {
            contentList = html.selectDocumentForList(selector);
        }
        return contentList;
    }

    private static List<String> parsePicList(CrawlerMetaDataConfig config, Html html) {

        if (BooleanUtils.isNotTrue(config.getIfDownloadPic())) {
            return Lists.newArrayList();
        }
        List<String> picList = analysisElementList(html,
                StringUtils.isBlank(config.getPicMethod()) ? config.getMethod() : config.getPicMethod(),
                config.getPic());
        if (CollectionUtils.isEmpty(picList)) {
            return picList;
        }
        if (StringUtils.isNotBlank(config.getPicPrefix())) {
            return picList.stream()
                    .map(e -> config.getCoverPrefix() + e)
                    .collect(Collectors.toList());
        }
        return picList;
    }

    private static Map<String, String> parseExt(SingleAddressReq req, CrawlerMetaDataConfig config,
            Html html) {
        Map<String, String> extMap = req.getExt();
        if (CollectionUtils.isEmpty(config.getExtParseList())) {
            return extMap;
        }
        if (MapUtils.isEmpty(extMap)) {
            extMap = Maps.newHashMap();
        }
        String method = config.getMethod();
        Map<String, String> finalExtMap = extMap;
        config.getExtParseList().forEach(e -> {
            String value =
                    analysisElement(html, StringUtils.isNotBlank(e.getParseMethod()) ? e.getParseMethod() : method,
                            e.getParseExpression());
            if (StringUtils.isBlank(value)) {
                return;
            }
            if (StringUtils.isNotBlank(e.getParsePrefix())) {
                value = e.getParsePrefix() + value;
            }
            finalExtMap.put(e.getParseId(), value);
        });
        return finalExtMap;
    }

    private static String analysisElement(Html html, String method, String c) {
        if (StringUtils.isBlank(c)) {
            return null;
        }
        if (StringUtils.equals("xpath", method)) {
            return StringUtils.trim(html.xpath(c).get());
        } else if (StringUtils.equals("css", method)) {
            return StringUtils.trim(html.$(c, "text").get());
        } else {
            return null;
        }
    }

    private static List<String> analysisElementList(Html html, String method, String c) {
        if (StringUtils.isBlank(c)) {
            return Lists.newArrayList();
        }
        if (StringUtils.equals("xpath", method)) {
            return html.xpath(c).all()
                    .stream()
                    .map(e -> StringUtils.trim(e))
                    .filter(e -> StringUtils.isNotBlank(e))
                    .collect(Collectors.toList());
        } else if (StringUtils.equals("css", method)) {
            return html.$(c, "text").all()
                    .stream()
                    .map(e -> StringUtils.trim(e))
                    .filter(e -> StringUtils.isNotBlank(e))
                    .collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }
    }

    private SingleAddressResp handleByDefault(Page page) {
        Html html = page.getHtml();
        //        List<String> content = html.xpath("//body//*/text()").all()
        //                .stream()
        //                .filter(e -> StringUtils.isNotBlank(e))
        //                .collect(Collectors.toList());
        List<String> content = null;
        if (null != selector) {
            content = html.selectDocumentForList(selector);
        }
        //        List<String> content = Lists.newArrayList(html.smartContent().get());
        return SingleAddressResp.builder()
                .processorName(config.getName())
                .content(content)
                .title(StringUtils.isNotBlank(req.getTitle()) ? req.getTitle() : html.getDocument().title())
                .link(StringUtils.isNotBlank(req.getLink()) ? req.getLink() : page.getUrl().get())
                .url(page.getUrl().get())
                .cover(req.getCustomCoverUrl())
                .description(req.getDescription())
                .thirdId(req.getThirdId())
                .picList(Lists.newArrayList())
                .base(req.getBase())
                .ext(req.getExt())
                .build();
    }

    private static void initConfig(CrawlerMetaDataConfig config) {
        if (config == null) {
            log.error("spider process fail, config is null");
            return;
        }
        if (StringUtils.isBlank(config.getMethod())) {
            config.setMethod("xpath");
        }
    }
}
