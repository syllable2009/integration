package com.jxp.webmagic.processor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jxp.dto.bo.SingleAddressResp;
import com.jxp.webmagic.DefaultProcessor;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-17 10:46
 */
@Slf4j
@Service
public class OneProcessor extends DefaultProcessor {

    private static String NAME = "XXX";

    @Override
    public void process(Page page) {
        boolean downloadSuccess = page.isDownloadSuccess();
        if (!downloadSuccess) {
            log.error("spider download {} page fail,url:{}", NAME, page.getUrl());
            return;
        }
        log.info("spider process {} start,url:{}", NAME, page.getUrl());
        // 单页面都处理html页面
        Html html = page.getHtml();
        if (null == html) {
            log.error("spider process {} fail, html is null,{}", NAME, page.getUrl());
            return;
        }

        final List<String> all = html.xpath("").all();
        final String get = html.xpath("").get();
        this.setProcessorData(SingleAddressResp.builder()
                .build());
    }
}
