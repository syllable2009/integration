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
public class TemplateMessageRequest {
    private String touser;
    @JsonProperty("template_id")
    private String templateId;
    private String url;
    private Map<String, String> miniprogram;
    @JsonProperty("client_msg_id")
    private String clientMsgId;
    private Map<String, ValuePair> data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValuePair {
        private String value;
        private String color;
    }
}