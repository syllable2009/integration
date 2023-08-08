package com.jxp.integration.test.main;

import java.io.IOException;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.selector.Html;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-08 16:25
 */

@Slf4j
public class HtmlUnit {

    public static void main(String[] args) throws IOException {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            // disable javascript
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setActiveXNative(true);
            // disable css support
            webClient.getOptions().setCssEnabled(true);
            final HtmlPage page = webClient.getPage("https:///#/official/social/job-info/17047");
            webClient.waitForBackgroundJavaScript(30000);
            boolean htmlPage = page.getPage().isHtmlPage();
            log.info("htmlPage:{}", htmlPage);
            final String pageAsText = page.asNormalizedText();
            log.info("pageAsText:{}", pageAsText);
            String textContent = page.asXml();
            // asxml getTextContent
            log.info("textContent:{}", textContent);

            Html html = new Html(textContent);
            String s = html.xpath("//*[@id=root]/div/div/div[2]/div[1]/div[2]/div[3]/div[2]/pre/text()").get();
            log.info("hahhaha:{}", s);

        }

    }
}
