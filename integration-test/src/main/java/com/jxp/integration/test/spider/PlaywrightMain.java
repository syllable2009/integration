package com.jxp.integration.test.spider;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.collect.Lists;
import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.jxp.integration.test.spider.domain.dto.CrawlerTaskDataConfig;
import com.jxp.integration.test.spider.domain.dto.PageDTO;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SingleAddressResp;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;
import com.jxp.integration.test.spider.downloader.PlaywrightDownloader;
import com.jxp.integration.test.spider.enums.PageType;
import com.jxp.integration.test.spider.helper.SpiderHelper;
import com.jxp.integration.test.spider.helper.SpiderTaskHelper;
import com.jxp.integration.test.spider.pipeline.DefaultPipeline;
import com.jxp.integration.test.spider.processor.DefaultProcessor;
import com.jxp.integration.test.spider.processor.DefaultTaskProcessor;
import com.jxp.integration.test.util.DownloadFileUtil;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ScreenshotType;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 11:02
 */
@Slf4j
public class PlaywrightMain {

    private static final String BASE_URL = "https://www.google.com";

    private static final String PATH = "/Users/jiaxiaopeng/";

    public static void main(String[] args) {
        //        test5();
        //        String s = URLUtil.completeUrl("http://www.netbian.com", "https://pic.netbian.com/tupian/27978.html");
        //        log.info("s:{}", s);
        test1();
    }

    public static void test1() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false).setSlowMo(1000).setDevtools(false));
            BrowserContext browserContext = browser.newContext(new NewContextOptions().setLocale("zh-CN"));
            Page page = browserContext.newPage();
            page.navigate(BASE_URL);

            ElementHandle input = page.waitForSelector("xpath=//textarea[@id=\"APjFqb\"]");
            if (BooleanUtils.isTrue(input.isVisible())) {
                input.fill("小米");
            } else {
                log.info("input not found");
            }

            ElementHandle button = page.waitForSelector("xpath=//input[@type=\"submit\"]");
            if (BooleanUtils.isTrue(button.isVisible())) {
                button.click();
            } else {
                log.info("button not found");
            }
            Thread.sleep(3000L);
            int start = 0;
            int step = 1000;
            for (int i = 0; i < 20; i++) {
                page.mouse().wheel(start, step);
                start = start + step;
                Thread.sleep(300L);
            }
            page.screenshot(new ScreenshotOptions().setType(ScreenshotType.JPEG)
                    .setFullPage(false).setQuality(90)
                    .setPath(Paths.get(PATH + IdUtil.simpleUUID() + ".jpg")));
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public static void test2() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(1000));
            BrowserContext browserContext = browser.newContext(new NewContextOptions()
                    .setViewportSize(1920, 1080));
            Page page = browserContext.newPage();
            page.onRequest(request -> System.out.println(">> " + request.method() + " " + request.url()));
            page.onResponse(response -> System.out.println("<<" + response.status() + " " + response.url()));
            page.navigate("https://www.baidu.com/");
            page.locator("#kw").fill("公众号");
            page.locator("#kw").press("Enter");
            String value = page.getAttribute("#kw", "value");
            Assert.equals(value, "公众号");
            System.out.println(page.url());

        }
    }

    public static void test3() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(1000));
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true));

            Page page1 = browserContext.newPage();
            //            page.onDialog(dialog -> dialog.dismiss());

            //            page.waitForTimeout(60_000); = thread.sleep
            page1.navigate("http://www.netbian.com/2560x1440/", new Page.NavigateOptions().setTimeout(120 * 1000));
            log.info("pageInfo1:{},url:{}", page1.toString(), page1.url());

            Page page2 = browserContext.waitForPage(() -> {
                page1.getByAltText("可爱女生帽子高清美女壁纸背景图片").click();
                log.info("open a new tab2");
            });

            // 注册事件
            page2.waitForLoadState();
            log.info("pageInfo2:{},url:{}", page2.toString(), page2.url());


            Page page3 = browserContext.waitForPage(() -> {
                log.info("open a new tab3");
                page2.getByAltText("可爱女生帽子高清美女壁纸背景图片").click();
            });

            page3.waitForLoadState();
            log.info("pageInfo3:{},url:{}", page3.toString(), page3.url());


            // 这里为什么会超时？难道一定要下载的流文件？
            Page page4 = browserContext.waitForPage(() -> {
                log.info("open a new tab4");
                page3.getByAltText("可爱女生帽子高清美女壁纸背景图片").click();
            });

            String content = page4.content();
            log.info("content:{}", content);
            DownloadFileUtil.downloadByHutool(page4.url(), "/Users/jiaxiaopeng/", "222.jpg");
            // page4.waitForLoadState();  图片下载文件流有问题
            page4.waitForTimeout(5_000);
            log.info("pageInfo4:{},url:{}", page4.toString(),
                    page4.url()); // http://img.netbian.com/file/2023/0918/2022362nAbF.jpg


            //这里是运行时打断点使用，方便调试（适用于喜欢用录制回放生成脚本的同学）
            //page.pause();
            //后退操作
            //            page.goBack();
            //前进操作
            //            page.goForward();
            //刷新操作
            //            page.reload();
        }
    }

    public static void test4() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setArgs(Collections.singletonList("--start-maximized"))
                            .setHeadless(false).setSlowMo(1000));
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true));

            Page page1 = browserContext.newPage();
            //            page.onDialog(dialog -> dialog.dismiss());

            //            page.waitForTimeout(60_000); = thread.sleep
            page1.navigate("http://www.netbian.com/2560x1440/", new Page.NavigateOptions().setTimeout(120 * 1000));
            log.info("pageInfo1:{},url:{}", page1.toString(), page1.url());

            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in"));
            //            page1.getAttribute(AriaRole.LINK,new Page.GetAttributeOptions().);
        }
    }

    private final static PlaywrightDownloader playwrightDownloader = new PlaywrightDownloader();
    private final static DefaultPipeline defaultPipeline = new DefaultPipeline();

    public static void test5() {
        String baseUrl = "http://www.netbian.com/2560x1440/";
        List<PageDTO> flowSequence = Lists.newArrayList(PageDTO.builder()
                        .pageType(PageType.PageList)
                        .taskDataConfig(CrawlerTaskDataConfig.builder()
                                .method("xpath")
                                .link("//div[@id=main]/div[@class=list]//a/@href")
                                .linkPrefix("http://www.netbian.com")
                                .build())
                        .reqList(Lists.newArrayList(SingleAddressReq.builder()
                                .link(baseUrl)
                                .build()))
                        .build(),
                PageDTO.builder()
                        .pageType(PageType.SingleElement)
                        .metaDataConfig(CrawlerMetaDataConfig.builder()
                                .method("xpath")
                                .link("")
                                .linkPrefix("")
                                .build())
                        .build()
        );

        flowSequence.forEach(e -> {
            PageDTO dto = handFlowSequence(e);
            log.info("pageDTO:{}", JSONUtil.toJsonStr(dto));
        });
    }

    public static PageDTO handFlowSequence(PageDTO dto) {
        PageType pageType = dto.getPageType();
        if (PageType.IndexList == pageType) {
            List<SingleAddressReq> reqList = Lists.newArrayList();
            dto.getReqList().forEach(i -> reqList.addAll(handlePageList(i, dto.getTaskDataConfig())));
            dto.setReqList(reqList);
            return dto;
        } else if (PageType.PageList == pageType) {
            List<SingleAddressReq> reqList = Lists.newArrayList();
            dto.getReqList().forEach(i -> reqList.addAll(handlePageList(i, dto.getTaskDataConfig())));
            dto.setReqList(reqList);
            return dto;
        } else if (PageType.SingleElement == pageType) {
            List<SingleAddressResp> respList = Lists.newArrayList();
            dto.getReqList().forEach(i -> respList.add(handleSinglePage(i, dto.getMetaDataConfig())));
            dto.setRespList(respList);
            return dto;
        } else if (PageType.Trigger == pageType) {
            return dto;
        } else if (PageType.Download == pageType) {
            return dto;
        } else {
        }
        return null;
    }

    public static SingleAddressResp handleSinglePage(SingleAddressReq singleAddressReq, CrawlerMetaDataConfig config) {
        SpiderHelper spiderHelper = SpiderHelper.builder()
                .req(singleAddressReq)
                .downloader(playwrightDownloader)
                .processor(DefaultProcessor.builder()
                        .config(config)
                        .site(null)
                        .req(singleAddressReq)
                        .build())
                .pipeline(defaultPipeline)
                .build();
        spiderHelper.run();
        DefaultProcessor processor = (DefaultProcessor) spiderHelper.getProcessor();
        if (null == processor) {
            log.error("handleSinglePage processorData is null,url:{}", singleAddressReq.getLink());
            return null;
        }
        return processor.getProcessorData();
    }

    public static List<SingleAddressReq> handlePageList(SingleAddressReq singleAddressReq,
            CrawlerTaskDataConfig config) {
        RecommendCrawlerTaskData taskData = RecommendCrawlerTaskData.builder()
                .link(singleAddressReq.getLink())
                .build();
        SpiderTaskHelper spiderHelper = SpiderTaskHelper.builder()
                .taskData(taskData)
                .downloader(playwrightDownloader)
                .processor(DefaultTaskProcessor.builder()
                        .config(config)
                        .site(null)
                        .taskData(taskData)
                        .build())
                .pipeline(defaultPipeline)
                .build();
        spiderHelper.run();
        DefaultTaskProcessor processorData = (DefaultTaskProcessor) spiderHelper.getProcessor();
        if (null == processorData.getProcessorData()) {
            log.error("handlePageList processorData is null,url:{}", taskData.getLink());
            return Lists.newArrayList();
        }
        return processorData.getProcessorData().getSingleAddressReqList();
    }
}
