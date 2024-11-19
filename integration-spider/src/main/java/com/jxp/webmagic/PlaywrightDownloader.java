package com.jxp.webmagic;

import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.config.PlaywrightConfig;
import com.jxp.dto.bo.CrawlerDomainConfig;
import com.microsoft.playwright.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-03 16:33
 */
@Slf4j
//@Service
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlaywrightDownloader extends AbstractDownloader implements Closeable {

    private LoginService loginService;

    private CrawlerDomainConfig config;

    public static final Map<String, String> BINARY_MAP =
            ImmutableMap.of(IMAGE_JPEG_VALUE, ".jpg", IMAGE_PNG_VALUE, ".png",
                    IMAGE_GIF_VALUE, ".gif"
            );

    @Override
    public void close() throws IOException {
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public Page download(Request request, Task task) {
        if (task == null || task.getSite() == null) {
            log.error("PlaywrightDownloader download page fail,task or site can not be null,url:{}",
                    request.getUrl());
            return Page.fail();
        }
        // 配置校验
        if (config == null) {
            log.error("PlaywrightDownloader download page fail,config can not be null,url:{}", request.getUrl());
            return Page.fail();
        }
        String url = request.getUrl();
        String domain = UrlUtils.getDomain(url);
        // 判断是否需要登录，登录拦截了才会去登录
        Boolean ifNeedLogin = false;
        if (config != null && BooleanUtils.isTrue(config.getIfNeedLogin())) {
            ifNeedLogin = true;
        }
        StopWatch watcher = new StopWatch("PlaywrightDownloader");
        com.microsoft.playwright.Page page = null;
        try {
            // 判断是否需要代理服务器的页面
            watcher.start("open new blank page");
            if (config != null && BooleanUtils.isTrue(config.getIfProxy())) {
                page = PlaywrightConfig.getChromiumBrowserPage(true);
            } else {
                page = PlaywrightConfig.getChromiumBrowserPage(false);
            }
            watcher.stop();
            watcher.start("navigate");
            Response response = page.navigate(request.getUrl(), PlaywrightConfig.PAGE_NAV_OPTIONS);
            // 如果需要登录，此处进行登录并且保存Cookie
            watcher.stop();
            if (ifNeedLogin) {
                watcher.start("login");
                // 判断是否登录
                Boolean login = loginService.globelLogin(domain, page, url, config);
                if (login == null) {
                    log.info("PlaywrightDownloader login status domain:{},present:{}", domain, url);
                    if (!StringUtils.equals(page.url(), url)) {
                        response = page.navigate(url, PlaywrightConfig.PAGE_NAV_OPTIONS);
                    }
                } else if (BooleanUtils.isTrue(login)) {
                    response = page.navigate(url, PlaywrightConfig.PAGE_NAV_OPTIONS);
                    // 登录成功后才打开一次是为了像问答这种网站跨域停留列表页无法跳回到详细页面
                    response = page.navigate(url, PlaywrightConfig.PAGE_NAV_OPTIONS);
                    log.info("PlaywrightDownloader login domain:{} success,targetUrl:{},present:{}", domain, url,
                            page.url());
                } else {
                    log.info("PlaywrightDownloader login domain:{} fail,targetUrl:{},present:{}", domain, url,
                            page.url());
                }
                watcher.stop();
            }
            log.info(watcher.prettyPrint());
            // playwright的自动等待还不太智能，数据未能完全加载出来，这里强制等待3秒
//             Thread.sleep(3000L);
            if (null == response || !response.ok()) {
                log.info("PlaywrightDownloader download page fail,response not ok,url:{}", request.getUrl());
                return Page.fail();
            }
            log.info("PlaywrightDownloader download page success,url:{},currentUrl:{}",
                    request.getUrl(), page.url());
            Page ret = new Page();
            ret.setStatusCode(response.status());
            ret.setDownloadSuccess(true);
            ret.setRequest(request);
            ret.setUrl(new PlainText(page.url()));
            String contentType = response.allHeaders().get("content-type");
            ret.putField("content-type", contentType);
            if (!request.isBinaryContent()) {
                ret.setRawText(page.content());
            } else {
                ret.setBytes(response.body());
            }
            // 赋值请求头信息
            Map<String, List<String>> headers = Maps.newHashMap();
            response.allHeaders().forEach((k, v) -> headers.put(k, Lists.newArrayList(v)));
            ret.setHeaders(headers);
            return ret;
        } catch (Exception e) {
            log.info("PlaywrightDownloader download page exception,url:{}", request.getUrl(), e);
        } finally {
            if (null != page) {
                page.close();
            }
        }
        return Page.fail();
    }

    @Override
    public void setThread(int threadNum) {
    }
}
