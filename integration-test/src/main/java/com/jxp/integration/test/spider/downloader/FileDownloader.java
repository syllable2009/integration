package com.jxp.integration.test.spider.downloader;

import static com.jxp.integration.test.config.PlaywrightConfig.PAGE_NAV_OPTIONS;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.google.common.collect.Maps;
import com.jxp.integration.test.config.PlaywrightConfig;
import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.ScreenshotType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-26 14:46
 */
@Slf4j
@Service
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDownloader {

    @Resource
    private LoginService loginService;
    @Resource
    private Map<String, CrawlerMetaDataConfig> crawlerMetaDataConfigMap;

    private static final String COVER_PREFIX = "spider/";

    // 需要下载的文件类型
    public static final Map<String, String> FILE_MEDIA_TYPE = Maps.newHashMap();

    static {
        FILE_MEDIA_TYPE.put(IMAGE_JPEG_VALUE, ".jpeg");
        FILE_MEDIA_TYPE.put(MediaType.IMAGE_PNG_VALUE, ".png");
        FILE_MEDIA_TYPE.put(MediaType.IMAGE_GIF_VALUE, ".gif");
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public String download(String url, String id, Boolean screenshot) {
        if (StringUtils.isEmpty(url)) {
            log.error("FileDownloader download file fail,url is empty");
            return null;
        }
        //            String referer = UrlUtils.getHost(url);
        String domain = UrlUtils.getDomain(url);
        CrawlerMetaDataConfig config = crawlerMetaDataConfigMap.get(domain);
        Boolean ifNeedLogin = false;
        if (config != null && BooleanUtils.isTrue(config.getIfNeedLogin())) {
            ifNeedLogin = true;
        }
        Page page = null;
        StopWatch watcher = new StopWatch("FileDownloader");
        try {
            // 判断是否需要代理服务器的页面
            watcher.start("open new page");
            page = PlaywrightConfig.getChromiumBrowserPage(config.getIfProxy());
            watcher.stop();
            watcher.start("navigate");
            log.info("Playwright navigate::{}", url);
            Response response = page.navigate(url, PAGE_NAV_OPTIONS);
            watcher.stop();
            // 如果需要登录，此处进行登录并且保存Cookie
            if (ifNeedLogin) {
                watcher.start("login");
                // 目前只支持apsso登录
                Boolean login = loginService.globelLogin(domain, page, url, config, response);
                if (BooleanUtils.isTrue(login)) {
                    log.info("login domain:{} success,targetUrl:{},url:{}", domain, url, page.url());
                } else {
                    log.info("login domain:{} fail,targetUrl:{},url:{}", domain, url, page.url());
                }
                watcher.stop();
            }
            log.info(watcher.prettyPrint());
            if (!response.ok()) {
                log.error("FileDownloader page fail,url:{}", url);
                return null;
            }
            if (BooleanUtils.isTrue(screenshot)) {
                byte[] screenshotBody = page.screenshot(new ScreenshotOptions().setFullPage(false)
                        .setQuality(100).setType(ScreenshotType.JPEG));
                String fileKey = COVER_PREFIX + id + ".jpg";
                boolean upload = uploadCover(fileKey, screenshotBody);
                if (upload) {
                    return fileKey;
                } else {
                    log.error("FileDownloader screenshot fail,fileKey:{}", fileKey);
                }
                return null;
            }
            String mediaType = response.allHeaders().get("content-type");
            String suffix = FILE_MEDIA_TYPE.get(mediaType);
            if (StringUtils.isEmpty(suffix)) {
                log.info("FileDownloader cancel, mediaType not match,id:{},mediaType:{}", id, mediaType);
                return null;
            }
            String fileKey = COVER_PREFIX + id + suffix;
            boolean upload = uploadCover(fileKey, response.body());
            if (upload) {
                return fileKey;
            } else {
                log.error("FileDownloader uploadCover fail,fileKey:{}", fileKey);
            }
        } catch (Exception e) {
            log.error("FileDownloader download file exception,url:{},id:{},screenshot:{}", url, id, screenshot, e);
        } finally {
            if (null != page) {
                page.close();
            }
        }
        // 处理文件下载
        //        if (page.getRequest().isBinaryContent()) {
        //            DownloadFileUtil.saveFileBytes(page.getBytes(), "/Users/jiaxiaopeng/", IdUtil.fastSimpleUUID() +
        //                    PlaywrightDownloader.BINARY_MAP.get(page.getResultItems().get("content-type")));
        //            log.info("recommend spider end process,binaryContent downloaded,url:{}", page.getUrl());
        //            processorData = SingleAddressResp.builder()
        //                    .state(0)
        //                    .build();
        //            return;
        //        }
        return null;
    }

    // fileKey转换成cdn上能裁剪的文件地址
    private Boolean uploadCover(String fileKey, byte[] body) {
        return true;
    }

}
