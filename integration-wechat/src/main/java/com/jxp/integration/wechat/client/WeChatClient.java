package com.jxp.integration.wechat.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.jxp.integration.base.tool.HttpProxy;
import com.jxp.integration.wechat.model.AccessTokenResult;
import com.jxp.integration.wechat.model.CreateQrCodeResult;
import com.jxp.integration.wechat.model.CreateViewMenuRequest;
import com.jxp.integration.wechat.model.CreateViewMenuResult;
import com.jxp.integration.wechat.model.CreteQrCodeRequest;
import com.jxp.integration.wechat.model.DelMenuResult;
import com.jxp.integration.wechat.model.TemplateMessageRequest;
import com.jxp.integration.wechat.model.TemplateMessageResult;
import com.jxp.integration.wechat.model.UserInfoResult;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.Getter;

public class WeChatClient {

    private static final String BASE_URL = "https://api.weixin.qq.com";

    @Getter
    private WeChatApi weChatApi;

    public WeChatClient() {
        this.weChatApi = buildWeChatApi();
    }

    private WeChatApi buildWeChatApi() {
        return Feign.builder()
                .client(new OkHttpClient(HttpProxy.getDefaultProxiedOkHttpClient()))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(WeChatApi.class, BASE_URL);
    }

    public interface WeChatApi {
        /**
         * 获取token
         */
        @RequestLine("GET /cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}")
        AccessTokenResult getAccessToken(@Param("grant_type") String grantType, @Param("appid") String appId,
                @Param("secret") String appSecret);

        /**
         * 生成扫描二维码
         */
        @RequestLine("POST /cgi-bin/qrcode/create?access_token={access_token}")
        CreateQrCodeResult createQrCode(@Param("access_token") String accessToken, CreteQrCodeRequest req);

        /**
         * 通过openid查询用户信息
         */
        @RequestLine("GET /cgi-bin/user/info?access_token={access_token}&openid={openid}&lang={lang}")
        UserInfoResult getUserInfo(@Param("access_token") String accessToken, @Param("openid") String openid,
                @Param("lang") String lang);

        /**
         * 发送模板消息
         */
        @RequestLine("POST /cgi-bin/message/template/send?access_token={access_token}")
        TemplateMessageResult sendTemplateMessage(@Param("access_token") String accessToken,
                TemplateMessageRequest req);

        /**
         * 创建view和click菜单
         */
        @RequestLine("POST /cgi-bin/menu/create?access_token={access_token}")
        CreateViewMenuResult createViewMenu(@Param("access_token") String accessToken,
                CreateViewMenuRequest req);

        /**
         * 查询公众号当前使用的自定义菜单
         */
        @RequestLine("GET /cgi-bin/get_current_selfmenu_info?access_token={access_token}")
        JsonNode getMenuInfo(@Param("access_token") String accessToken);

        /**
         * 删除自定义菜单
         */
        @RequestLine("GET /cgi-bin/menu/delete?access_token={access_token}")
        DelMenuResult delMenu(@Param("access_token") String accessToken);
    }
}
