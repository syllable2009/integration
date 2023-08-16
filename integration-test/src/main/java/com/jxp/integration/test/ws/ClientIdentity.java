package com.jxp.integration.test.ws;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-16 18:01
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientIdentity {

    // 身份信息
    private WsConnectIdentityDto wsConnectIdentityDto;

    // 订阅事件
    private Map<String, Boolean> events;

    // 最后一次心跳时间
    private Long lastHeartbeatTimestamp;
}
