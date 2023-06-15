package com.jxp.integration.wechat.service;

import com.jxp.integration.wechat.model.AccessTokenResult;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-15 10:22
 */
public interface WeChatService {
    AccessTokenResult getAccessToken(String grantType, String appid, String appSecret);
}
