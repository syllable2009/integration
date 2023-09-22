package com.jxp.integration.test.spider.domain.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 16:00
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "SingleAddressReq", description = "单地址请求对象")
public class SingleAddressReq {

    @NotEmpty(message = "链接地址不能为空")
    @ApiModelProperty(value = "链接地址", required = true)
    private String url;

    // 优先级：req > kconf > process > default
    @ApiModelProperty(value = "自定义标题")
    private String title;

    @ApiModelProperty(value = "自定义链接地址")
    private String link;

    @ApiModelProperty(value = "自定义摘要")
    private String description;

    @ApiModelProperty(value = "自定义封面")
    private String customCoverUrl;

    @JsonIgnore
    private String loginId;
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    @ApiModelProperty(value = "来源")
    private String base;

    @ApiModelProperty(value = "三方业务id")
    private String thirdId;
}
