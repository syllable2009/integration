package com.jxp.dto.bo;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:18
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingleAddressReq {

    @NotEmpty(message = "链接地址不能为空")
    private String url;

    // 摘要类型:none,aigc,content,parse,custom
    private String descriptionType;

    private String cunstomDesc;

    // 自定义封面类型:目前支持none,parse,custom,sourceIdentity,screenshot
    private String customCoverType;

    private String customCoverUrl;

    // 来源
    private String base;

    // 三方业务id，用于去重
    private String thirdId;

    @JsonIgnore
    private String userId;

    // 本次请求使用的解析器，用于扩展配置无法处理需要自定义解析器的情况
    private String processorName;
    // 本次使用的配置名称
    private String configName;
    // 额外解析的数据
    private Map<String, String> ext;


    private String title;
    private String link;
    private String description;
    private String domain;
}
