package com.jxp.integration.wechat.model;

import java.util.List;

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
public class CreateViewMenuRequest {

    private List<Button> button;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Button {
        private String type;
        private String name;
        private String key;
        private String url;
        private String appid;
        private String pagepath;
        @JsonProperty("sub_button")
        private List<Button> subButton;
    }
}