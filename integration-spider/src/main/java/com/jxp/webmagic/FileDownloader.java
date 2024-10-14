package com.jxp.webmagic;

import static com.jxp.config.PlaywrightConfig.PAGE_NAV_OPTIONS;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.jxp.config.PlaywrightConfig;
import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.jxp.dto.bo.FileDTO;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.ScreenshotType;

import cn.hutool.core.util.StrUtil;
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

    @Qualifier("chromiumBrowserContext")
    @Autowired(required = false)
    private BrowserContext chromiumBrowserContext;

    @Resource
    private LoginService loginService;

    public static final String COVER_PREFIX = "spider/";

    public static final String DEFAULT_COVER_PREFIX = "/";

    public static final Map<String, String> FILE_MEDIA_TYPE = Maps.newHashMap();

    static {
        FILE_MEDIA_TYPE.put(IMAGE_JPEG_VALUE, ".jpeg");
        FILE_MEDIA_TYPE.put(MediaType.IMAGE_PNG_VALUE, ".png");
        FILE_MEDIA_TYPE.put(MediaType.IMAGE_GIF_VALUE, ".gif");
        FILE_MEDIA_TYPE.put("image/webp", ".webp");
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public FileDTO download(String url, String id, Boolean screenshot) {
        if (StringUtils.isEmpty(url)) {
            log.info("FileDownloader download file fail,url is empty");
            return null;
        }
        //            String referer = UrlUtils.getHost(url);
        String domain = UrlUtils.getDomain(url);
        CrawlerMetaDataConfig config = CrawlerMetaDataConfig.builder().build();
        Boolean ifNeedLogin = false;
        Boolean ifProxy = false;
        if (config != null) {
            ifNeedLogin = BooleanUtils.isTrue(ifNeedLogin);
            ifProxy = BooleanUtils.isTrue(config.getIfProxy());
        }
        StopWatch watcher = new StopWatch("FileDownloader");
        Page page = null;
        try {
            // 判断是否需要代理服务器的页面
            watcher.start("open new page");
            page = PlaywrightConfig.getChromiumBrowserPage(ifProxy);
            watcher.stop();
            watcher.start("navigate");
            Response response = page.navigate(url, PlaywrightConfig.PAGE_NAV_OPTIONS);
            // 如果需要登录，此处进行登录并且保存Cookie
            watcher.stop();
            // 如果需要登录，此处进行登录并且保存Cookie
            if (ifNeedLogin) {
                watcher.start("login");
                // 目前只支持apsso登录
                Boolean login = loginService.globelLogin(domain, page, url, config);
                if (login == null) {
                    log.info("FileDownloader login status domain:{},present:{}", domain, url);
                    if (!StrUtil.startWith(page.url(), url)) {
                        response = page.navigate(url, PAGE_NAV_OPTIONS);
                    }
                } else if (BooleanUtils.isTrue(login)) {
                    response = page.navigate(url, PAGE_NAV_OPTIONS);
                    // 登录成功后才打开一次是为了像问答这种网站跨域停留列表页无法跳回到详细页面
                    response = page.navigate(url, PAGE_NAV_OPTIONS);
                    log.info("FileDownloader login domain:{} success,targetUrl:{},present:{}", domain, url,
                            page.url());
                } else {
                    log.info("FileDownloader login domain:{} fail,targetUrl:{},present:{}", domain, url,
                            page.url());
                }
                watcher.stop();
            }
            // playwright的自动等待还不太智能，数据未能完全加载出来，这里强制等待3秒
//            Thread.sleep(3000L);
            log.info(watcher.prettyPrint());
            if (!response.ok()) {
                log.info("FileDownloader page fail,url:{}", url);
                return null;
            }
            if (BooleanUtils.isTrue(screenshot)) {
                byte[] screenshotBody = page.screenshot(new ScreenshotOptions().setFullPage(false)
                        .setQuality(100).setType(ScreenshotType.JPEG));
                String fileKey = COVER_PREFIX + id; // + ".jpg" 前端处理不让有后缀+suffix
//                boolean upload = uploadCover(fileKey, screenshotBody);
//                if (upload) {
//                    return FileDTO.builder()
//                            .mediaType(IMAGE_JPEG_VALUE)
//                            .fileKey(fileKey)
//                            .build();
//                } else {
//                    log.info("FileDownloader uploadCover fail,fileKey:{}", fileKey);
//                }
                return null;
            }
            String mediaType = response.allHeaders().get("content-type");
            String suffix = FILE_MEDIA_TYPE.get(mediaType);
            if (StringUtils.isEmpty(suffix)) {
                log.info("FileDownloader cancel, mediaType not match,id:{},mediaType:{}", id, mediaType);
                return null;
            }
//            String fileKey = COVER_PREFIX + id; // 前端处理不让有后缀+suffix
//            boolean upload = uploadCover(fileKey, response.body());
//            if (upload) {
//                return FileDTO.builder()
//                        .mediaType(mediaType)
//                        .fileKey(fileKey)
//                        .build();
//            } else {
//                log.info("FileDownloader uploadCover fail,fileKey:{}", fileKey);
//            }
        } catch (Exception e) {
            log.info("FileDownloader download file exception,url:{},id:{},screenshot:{}", url, id, screenshot, e);
        } finally {
            if (null != page) {
                page.close();
            }
        }
        return null;
    }
}
