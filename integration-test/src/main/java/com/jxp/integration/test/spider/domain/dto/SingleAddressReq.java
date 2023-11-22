package com.jxp.integration.test.spider.domain.dto;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "SingleAddressReq", description = "单地址请求对象")
@ApiModel(value = "SingleAddressReq", description = "单地址请求对象")
public class SingleAddressReq {

    @NotEmpty(message = "链接地址不能为空")
    @Schema(title = "链接地址", description = "http://")
    private String url;

    // 优先级：req > kconf > process > default
    @ApiModelProperty(value = "自定义标题")
    private String title;

    @ApiModelProperty(value = "自定义链接地址")
    private String link;

    @ApiModelProperty(value = "自定义摘要")
    private String description;

    @ApiModelProperty(value = "摘要类型:none,aigc,content")
    private String descriptionType;

    @ApiModelProperty(value = "自定义封面或者要截图的url地址,类型为screenshot和customUrl时生效")
    private String customCoverUrl;

    @ApiModelProperty(value = "自定义封面类型:目前支持none,parse,customUrl,sourceIdentity,screenshot")
    private String customCoverType;

    @JsonIgnore
    private String loginId;
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    @ApiModelProperty(value = "来源")
    private String base;

    @ApiModelProperty(value = "三方业务id")
    private String thirdId;

    @ApiModelProperty(value = "额外解析的数据")
    private Map<String, String> ext;

}
