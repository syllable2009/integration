package com.jxp.integration.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.jxp.integration.base.tool.IntegrationTemplate;
import com.jxp.integration.wechat.client.WeChatClient;
import com.jxp.integration.wechat.model.AccessTokenResult;
import com.jxp.integration.wechat.model.CreateQrCodeResult;
import com.jxp.integration.wechat.model.CreateViewMenuRequest;
import com.jxp.integration.wechat.model.CreateViewMenuResult;
import com.jxp.integration.wechat.model.CreteQrCodeRequest;
import com.jxp.integration.wechat.model.DelMenuResult;
import com.jxp.integration.wechat.model.TemplateMessageRequest;
import com.jxp.integration.wechat.model.TemplateMessageResult;
import com.jxp.integration.wechat.model.UserInfoResult;
import com.jxp.integration.wechat.service.WeChatService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WeChatServiceImpl extends IntegrationTemplate implements WeChatService {

    private final WeChatClient.WeChatApi weChatApi;

    public WeChatServiceImpl(WeChatClient weChatClient) {
        this.weChatApi = weChatClient.getWeChatApi();
    }

    @Override
    public AccessTokenResult getAccessToken(String grantType, String appid, String appSecret) {
        return invoke(new Call<AccessTokenResult>(grantType, appid, appSecret) {
            @Override
            public AccessTokenResult invoke() {
                return weChatApi.getAccessToken(grantType, appid, appSecret);
            }
        });
    }

    public CreateQrCodeResult createQrCode(String accessToken, CreteQrCodeRequest req) {
        return invoke(new Call<CreateQrCodeResult>(accessToken, req) {
            @Override
            public CreateQrCodeResult invoke() {
                return weChatApi.createQrCode(accessToken, req);
            }
        });
    }

    public UserInfoResult getUserInfo(String accessToken, String openid, String lang) {
        return invoke(new Call<UserInfoResult>(accessToken, openid, lang) {
            @Override
            public UserInfoResult invoke() {
                return weChatApi.getUserInfo(accessToken, openid, lang);
            }
        });
    }

    public TemplateMessageResult sendTemplateMessage(String accessToken, TemplateMessageRequest req) {
        return invoke(new Call<TemplateMessageResult>(accessToken, req) {
            @Override
            public TemplateMessageResult invoke() {
                return weChatApi.sendTemplateMessage(accessToken, req);
            }
        });
    }

    public CreateViewMenuResult createViewMenu(String accessToken, CreateViewMenuRequest req) {
        return invoke(new Call<CreateViewMenuResult>(accessToken, req) {
            @Override
            public CreateViewMenuResult invoke() {
                return weChatApi.createViewMenu(accessToken, req);
            }
        });
    }

    public JsonNode getMenuInfo(String accessToken) {
        return invoke(new Call<JsonNode>(accessToken) {
            @Override
            public JsonNode invoke() {
                return weChatApi.getMenuInfo(accessToken);
            }
        });
    }

    public DelMenuResult delMenu(String accessToken) {
        return invoke(new Call<DelMenuResult>(accessToken) {
            @Override
            public DelMenuResult invoke() {
                return weChatApi.delMenu(accessToken);
            }
        });
    }
}
