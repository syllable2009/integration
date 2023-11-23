package com.jxp.integration.test.spider.downloader;

import static com.jxp.integration.test.config.PlaywrightConfig.PAGE_NAV_OPTIONS;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.integration.test.config.PlaywrightConfig;
import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
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
@Service
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlaywrightDownloader extends AbstractDownloader implements Closeable {

    @Resource
    private LoginService loginService;
    @Resource
    private Map<String, CrawlerMetaDataConfig> crawlerMetaDataConfigMap;

    @Override
    public void close() throws IOException {
    }

    @Override
    public Page download(Request request, Task task) {
        if (task == null || task.getSite() == null) {
            throw new NullPointerException("PlaywrightDownloader download page fail,task or site can not be null");
        }
        String url = request.getUrl();
        String domain = UrlUtils.getDomain(url);
        // 获取配置
        CrawlerMetaDataConfig config = crawlerMetaDataConfigMap.get(domain);
        // 判断是否需要代理
        Boolean ifProxy = false;
        if (config != null && BooleanUtils.isTrue(config.getIfProxy())) {
            ifProxy = true;
        }
        // 判断是否需要登录
        Boolean ifNeedLogin = false;
        if (config != null && BooleanUtils.isTrue(config.getIfNeedLogin())) {
            ifNeedLogin = true;
        }
        com.microsoft.playwright.Page page = null;
        StopWatch watcher = new StopWatch("PlaywrightDownloader");
        try {
            // 判断是否需要代理服务器的页面
            watcher.start("open new page");
            page = PlaywrightConfig.getChromiumBrowserPage(ifProxy);
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
                if (null == login) {
                    log.info("PlaywrightDownloader login done domain:{},present:{}", domain, url);
                    if (!StringUtils.equals(page.url(), url)) {
                        response = page.navigate(url, PAGE_NAV_OPTIONS);
                    }
                } else if (BooleanUtils.isTrue(login)) {
                    // 登录成功后才打开一次是为了像内网sso网站跨域停留列表页无法跳回到详细页面，偶尔可能需要二次跳转
                    response = page.navigate(url, PAGE_NAV_OPTIONS);
                    if (!StringUtils.equals(page.url(), url)) {
                        response = page.navigate(url, PAGE_NAV_OPTIONS);
                    }
                    log.info("login domain:{} success,targetUrl:{},url:{}", domain, url, page.url());
                } else {
                    log.info("login domain:{} fail,targetUrl:{},url:{}", domain, url, page.url());
                    return Page.fail();
                }
                watcher.stop();
            }
            log.info(watcher.prettyPrint());
            // playwright的自动等待还不太智能，数据未能完全加载出来，这里强制等待3秒
            Thread.sleep(3000L);
            //            page.mouse().move(0, 0);  // 将鼠标移动到页面顶部
            //            while (true) {
            //                page.mouse().wheel(0, 1);  // 向下滚动鼠标滚轮
            //                // 检查是否有新的增量数据加载
            //                if (checkForNewData(page)) {
            //                    break;
            //                }
            //            }
            //            Locator locator = page.locator("xpath=//button");
            //            boolean enabled = locator.isEnabled();
            //            if (BooleanUtils.isNotTrue(enabled)) {
            //                log.error("PlaywrightDownloader can not find define locator");
            //            }else {
            //                locator.click();
            //            }
            if (!response.ok()) {
                log.error("PlaywrightDownloader download page fail,url:{}", request.getUrl());
                return Page.fail();
            }
            log.info("PlaywrightDownloader download page success,url:{},page.url:{}", request.getUrl(), page.url());
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
            log.error("PlaywrightDownloader download page exception,url:{}", request.getUrl(), e);
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

    public boolean checkForNewData(com.microsoft.playwright.Page page) {
        // 在这里编写检查增量数据的逻辑
        // 如果有新的增量数据加载，返回true；否则返回false
        // 例如，可以检查页面上特定元素的数量或属性来判断是否有新数据加载
        return true;
    }
}
