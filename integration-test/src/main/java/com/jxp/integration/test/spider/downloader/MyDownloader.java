package com.jxp.integration.test.spider.downloader;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-03 16:11
 */

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

@Slf4j
public class MyDownloader implements Downloader {
    //声明驱动
    private WebClient webClient;

    public MyDownloader() {

        // 新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);   // 当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // 当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false); // 不启用ActiveX
        webClient.getOptions().setCssEnabled(false);    // 是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true);  // 很重要，启用JS
        webClient.getOptions().setDownloadImages(false);    // 不下载图片
        webClient.setAjaxController(new NicelyResynchronizingAjaxController()); // 很重要，设置支持AJAX
        webClient.waitForBackgroundJavaScript(5 * 1000);   // 异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
        //webClient.getCookieManager()
        //       .addCookie(Cookie).addRequestHeader("Cookie","_did=web_35512701325345A6; ksCorpDeviceid=d33d31d0-d6e4-41dd-9840-32b1cb60a43e; hdige2wqwoino=GPr7AfktnrCh3NZrN7te6wSctAe6zX7R084e139b; did=web_b0b09f485cdbba02cc31ba7871dcf1359720; Hm_lvt_86a27b7db2c5c0ae37fee4a8a35033ee=1677650476; _ga_VKXBFL78SD=GS1.1.1678333927.1.1.1678333945.42.0.0; intercom-device-id-pjrf3upr=81a2f76b-6b30-4c72-9dc2-b7023170abc6; logged_out_marketing_header_id=eyJfcmFpbHMiOnsibWVzc2FnZSI6IkltTXhZemd5TnpBd0xUZGtPR1l0TkRBd05pMDRabUppTFRWbU1USXlNRGN4TkRKallpST0iLCJleHAiOm51bGwsInB1ciI6ImNvb2tpZS5sb2dnZWRfb3V0X21hcmtldGluZ19oZWFkZXJfaWQifX0%3D--dcc568a3031a2656a358922ad27c8e29f93fcc80; apdid=686ab5e0-f6cb-4c7d-bd2d-eb8bb27baf317567b145a767badb03931f8b6013184c:1687682245:1; accessproxy_session=80d98724-29e6-4e50-8268-7a4624d638f4; didv=1689240364810; userSource=others; _gid=GA1.2.874043532.1691048681; k-token=a07f37b10c38491e79672c29361be4cf; _ga=GA1.2.2027780773.1664422375; _ga_F6CM1VE30P=GS1.1.1691048681.4.1.1691048760.0.0.0");
    }

    /**
     * 由于selenium的默认域名为data;因此第一次必须跳转到登录页，才能加入对应域名
     *
     * @param request Request
     */
    @Override
    public Page download(Request request, Task task) {
        try {
            HtmlPage page = null;
            try {
                page = webClient.getPage(request.getUrl());  // 尝试加载给出的网页
            } catch (Exception e) {
                log.info("======>【严重】爬取失败：{}",request.getUrl());
                e.printStackTrace();
            } finally {
                webClient.close();
            }
            log.info("result:{}",page.asNormalizedText());
            Page page1 = new Page();
//            page1.setRawText(page.asXml());
            return page1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setThread(int threadNum) {

    }

    //构建page返回对象
    private Page createPage(String url, String content) {
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(url));
        page.setRequest(new Request(url));
        page.setDownloadSuccess(true);
        return page;
    }
}