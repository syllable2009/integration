package com.jxp.integration.test.spider.service;

import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.jxp.integration.test.spider.domain.dto.SingleAddressReq;
import com.jxp.integration.test.spider.domain.dto.SingleAddressResp;
import com.jxp.integration.test.spider.domain.dto.SpiderTaskResp;
import com.jxp.integration.test.spider.domain.entity.RecommendCrawlerTaskData;

import us.codecraft.webmagic.Site;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 15:59
 */
public interface SpiderService {

    SingleAddressResp parse(SingleAddressReq req);

    SingleAddressResp parseRun(SingleAddressReq req, CrawlerMetaDataConfig config, Site site);

    SpiderTaskResp taskSpiderRun(RecommendCrawlerTaskData req);
}
