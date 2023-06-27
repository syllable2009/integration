package com.jxp.event.configration;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.jxp.event.publisher.EventPublisher;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-26 11:19
 */

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "integration-event", name = "enable", havingValue = "true")
public class ProducerAutoConfiguration {

    @Resource
    private Environment environment;
    @Resource
    private EventProperty eventProperty;

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        final String eventConf = environment.getProperty("event.conf", "true");
        log.info("eventConf:{},eventProperty:{}", eventConf, JSONUtil.toJsonStr(eventProperty));
        if (eventProperty == null) {
            log.warn("no event publish property config found, only local event can be published");
            eventProperty = EventProperty.builder().publishProperties(Collections.emptyList())
                    .consumerProperties(Collections.emptyList()).build();
            return new EventPublisher(applicationEventPublisher);
        }
        //        final Map<String, String> producerMap = eventProperty.getPublishProperties().stream()
        //                .collect(Collectors.toMap(
        //                        p -> StrUtil.isBlank(p.getClassName()) ? "java.lang.Object" : p
        //                                .getClassName(), p -> p.getTopic()));
        //                        p -> MqFactoryProvider.getInstance(BizDef.IS_PSP).producerFactory()
        //                                .createProducerV2(p.getTopic(), p.getAppKey())));

        //        final EventPublishType defaultPublishType = EventPublishType.LOCAL;
        //        final Map<String, EventPublishType> publishTypeMap = eventProperty.getPublishProperties().stream()
        //                .collect(Collectors.toMap(
        //                        p -> StrUtil.isBlank(p.getClassName()) ? "java.lang.Object" : p
        //                                .getClassName(),
        //                        p -> Optional.of(p).map(EventPublishProperty::getPublishType)
        //                                .orElse(defaultPublishType)));

        final Map<String, EventPublishProperty> publishMap = eventProperty.getPublishProperties().stream()
                .collect(Collectors.toMap(
                        p -> StrUtil.isBlank(p.getClassName()) ? "java.lang.Object" : p
                                .getClassName(),
                        p -> p, (f, s) -> f));
        return new EventPublisher(applicationEventPublisher, publishMap);
    }
}
