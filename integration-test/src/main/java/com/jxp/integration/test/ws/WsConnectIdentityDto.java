package com.jxp.integration.test.ws;

import org.yeauty.pojo.Session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-16 17:55
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WsConnectIdentityDto {
    // 当前连接信息
    private Session session;
    //连接的客户端唯一标识
    private String clientId;
    //当前连接登陆的用户信息
    private String userId;
}
