package com.jxp.integration.wechat.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2022-11-24 10:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreteQrCodeRequest {
    @JsonProperty("expire_seconds")
    private int expireSeconds;
    @JsonProperty("action_name")
    private String actionName;
    @JsonProperty("action_info")
    private ActionInfo actionInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionInfo {
        private Map<String, Object> scene;
    }
}