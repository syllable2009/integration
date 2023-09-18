package com.jxp.integration.test.spider.domain.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:01
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "SpiderTaskResp", description = "爬虫任务响应对象")
public class SpiderTaskResp {

    @ApiModelProperty(value = "文档内容")
    private List<SingleAddressReq> singleAddressReqList;
}
