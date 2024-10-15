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
public class TaskAddressReq {

    @NotEmpty(message = "链接地址不能为空")
    private String url;

    private String domain;

    // 三方业务id，用于去重
    private String thirdId;

    @JsonIgnore
    private String userId;

    // 本次请求使用的解析器
    private String processor;

    // 额外解析的数据
    private Map<String, String> ext;
}
