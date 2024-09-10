package com.jxp.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:24
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventCallback {
    private String uuid;
    private long timestamp;
    private String appId;
    private String type;
    private Info info;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Info {
        private String messageKey;
        private Integer sessionType;
    }
}
