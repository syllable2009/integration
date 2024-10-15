package com.jxp.service;

import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.jxp.dto.bo.CrawlerTaskDataConfig;
import com.jxp.dto.bo.RecommendCrawlerTaskData;
import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.dto.bo.SingleAddressResp;
import com.jxp.dto.bo.SpiderTaskResp;
import com.jxp.dto.bo.TaskAddressReq;

import us.codecraft.webmagic.Site;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:29
 */
public interface SpiderApiService {

    SingleAddressResp parse(SingleAddressReq req, String userId);

    SingleAddressResp parseRun(SingleAddressReq req, CrawlerMetaDataConfig config, Site site);

    SpiderTaskResp taskSpiderRun(RecommendCrawlerTaskData taskData, CrawlerTaskDataConfig config);

    SpiderTaskResp taskParseRun(TaskAddressReq req, String userId);
}
