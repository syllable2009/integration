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
 * Created on 2023-09-18 15:58
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "SingleAddressResp", description = "单地址响应对象")
public class SingleAddressResp {

    @ApiModelProperty(value = "uuid")
    private String id;

    @ApiModelProperty(value = "地址：请求的链接")
    private String url;

    @ApiModelProperty(value = "摘要")
    private String description;

    @ApiModelProperty(value = "链接：文档的原始地址")
    private String link;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "域")
    private String domain;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "状态：当前文档的状态，默认为0")
    private Integer state;

    @ApiModelProperty(value = "是否重复")
    private Boolean ifRepeated;

    @ApiModelProperty(value = "解析器名称：默认实现为default")
    private String processor;

    @ApiModelProperty(value = "文档内容")
    private List<String> content;

    @ApiModelProperty(value = "创建人")
    private String loginId;

    @ApiModelProperty(value = "创建人")
    private Long userId;

    private String base;
}
