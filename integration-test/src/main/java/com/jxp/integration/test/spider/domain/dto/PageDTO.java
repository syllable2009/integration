package com.jxp.integration.test.spider.domain.dto;

import java.util.List;

import com.jxp.integration.test.spider.enums.PageLoadType;
import com.jxp.integration.test.spider.enums.PageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-21 10:32
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageDTO {

    private PageLoadType pageLoadType;

    private PageType pageType;

    private CrawlerTaskDataConfig taskDataConfig;

    private CrawlerMetaDataConfig metaDataConfig;

    List<SingleAddressReq> reqList;

    List<SingleAddressResp> respList;
}
