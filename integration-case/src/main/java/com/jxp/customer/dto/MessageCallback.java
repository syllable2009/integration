package com.jxp.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:24
 */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageCallback {
    private String uuid;
    private String token;
    private Long timestamp;
    private String appId;
    private String eventType;
    private Info info;
    private Long botKwaiUserId;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Info {
        private String sessionType;
        private String messageKey;
        private String messageType;
        private String content;
        private String to;
        private String from;
        private String groupId;
    }
}


