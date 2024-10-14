package com.jxp.service;

import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.jxp.dto.bo.SingleAddressReq;
import com.jxp.dto.bo.SingleAddressResp;

import us.codecraft.webmagic.Site;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:29
 */
public interface SpiderApiService {

    SingleAddressResp parse(SingleAddressReq req, String userId);

    SingleAddressResp parseRun(SingleAddressReq req, CrawlerMetaDataConfig config, Site site);
}
