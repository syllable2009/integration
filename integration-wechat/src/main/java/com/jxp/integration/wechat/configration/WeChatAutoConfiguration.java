package com.jxp.integration.wechat.configration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jxp.integration.wechat.client.WeChatClient;
import com.jxp.integration.wechat.service.WeChatService;
import com.jxp.integration.wechat.service.impl.WeChatServiceImpl;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-15 10:19
 */
@Configuration
@ConditionalOnProperty(prefix = "integration-wechat", name = "enable", havingValue = "true")
public class WeChatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WeChatClient weChatClient() {
        return new WeChatClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatService weChatService(WeChatClient weChatClient) {
        return new WeChatServiceImpl(weChatClient);
    }
}
