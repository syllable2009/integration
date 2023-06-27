package com.jxp.event.configration;

import java.util.List;

import javax.annotation.Resource;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-26 11:19
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "integration-event", name = "enable", havingValue = "true")
public class ConsumerAutoConfiguration implements ApplicationContextAware {

    @Resource
    private Environment environment;
    @Resource
    private EventProperty eventProperty;

    // 注册消费者
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        List<EventConsumerProperty> consumerProperties = eventProperty.getConsumerProperties();
        if (CollUtil.isEmpty(consumerProperties)){
            return;
        }
//        consumerProperties.forEach();
        new RocketMQListener() {
            @Override
            public void onMessage(Object o) {

            }
        };

    }
}
