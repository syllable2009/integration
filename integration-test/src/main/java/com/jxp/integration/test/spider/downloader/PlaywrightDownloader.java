package com.jxp.integration.test.spider.downloader;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.integration.test.config.PlaywrightConfig;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.ScreenshotType;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-03 16:33
 */
@Slf4j
@Service
public class PlaywrightDownloader extends AbstractDownloader implements Closeable {

    @Resource
    BrowserContext browserContext;

    @Override
    public void close() throws IOException {

    }

    @Override
    public Page download(Request request, Task task) {
        if (task == null || task.getSite() == null) {
            throw new NullPointerException("task or site can not be null");
        }
        // BrowserContext用于内存中隔离的浏览器配置文件，以确保它们不会相互干扰。
        //        BrowserContext browserContext = browser.newContext();
        com.microsoft.playwright.Page page = browserContext.newPage();
        Response navigate = page.navigate(request.getUrl());
        if (!navigate.ok()) {
            log.error("spider download page fail,url:{}", request.getUrl());
            return Page.fail();
        }
        log.info("spider download page success,url:{}", request.getUrl());
        Page ret = new Page();
        ret.setStatusCode(navigate.status());
        ret.setDownloadSuccess(true);
        ret.setRequest(request);
        ret.setUrl(new PlainText(request.getUrl()));
        if (!request.isBinaryContent()) {
            ret.setCharset(getHtmlCharset(navigate.allHeaders(), navigate.body()));
            ret.setRawText(navigate.text());
        }
        ret.setBytes(navigate.body());
        Map<String, List<String>> headers = Maps.newHashMap();
        navigate.allHeaders().forEach((k, v) -> headers.put(k, Lists.newArrayList(v)));
        ret.setHeaders(headers);
        page.close();
        return ret;
    }

    @Override
    public void setThread(int threadNum) {

    }

    public static void main(String[] args) throws InterruptedException {
        // 自己关闭自己的context

        BrowserContext browserContext = PlaywrightConfig.BROWSER_CONTEXT;
        com.microsoft.playwright.Page page = browserContext.newPage();
        Response navigate = page.navigate("https://www.36kr.com/newsflashes/2368027471505793");
//        log.info("content:{}", navigate.text());
//        log.info("charset:{}", getHtmlCharset(navigate.allHeaders(), navigate.body()));
//        log.info("allHeaders:{}", navigate.allHeaders());
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                .setFullPage(true)
                .setType(ScreenshotType.PNG)
                .setPath(Paths.get("/Users/jiaxiaopeng/screenshot2.png")));
        Locator l1 = page.getByText("原文链接");
        log.info("l1:{}",l1.isVisible());
        Locator l2 = page.getByText(Pattern.compile("原文链接$", Pattern.CASE_INSENSITIVE));
        log.info("l2:{}",l2.isVisible());
//        Locator l3 = page.locator("//a[@class=article-link-icon]");
//        log.info("l3:{}",l3.isVisible());
        ElementHandle elementHandle = page.querySelector(".article-link-icon");
        log.info("l4:{}",elementHandle.isVisible());
        l1.hover();
        l1.click();
        page.waitForTimeout(60_000);
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                .setPath(Paths.get("/Users/jiaxiaopeng/screenshot3.png")));
        PlaywrightConfig.close();
    }

    private static String getHtmlCharset(Map<String, String> allHeaders, byte[] contentBytes) {
        String charset = allHeaders.get("charset");
        if (StringUtils.isNotBlank(charset)) {
            return charset;
        }
        String contentType = allHeaders.get("content-type");
        try {
            charset = CharsetUtils.detectCharset(contentType, contentBytes);
            if (null == charset) {
                log.error("getHtmlCharset null,contentType:{}", contentType);
            }
            return charset;
        } catch (Exception e) {
            log.error("getHtmlCharset exception,contentType:{}", contentType);
        }
        return Charset.defaultCharset().name();
    }
}
