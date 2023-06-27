package com.jxp.event.publisher;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.messaging.support.GenericMessage;

import com.jxp.event.configration.EventPublishProperty;
import com.jxp.event.enums.EventPublishType;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-26 11:23
 */
@Slf4j
public class EventPublisher {

    //    @Resource
    //    private RocketMQTemplate rocketMQTemplate;

    //    <eventClassName, producer>
    // 这里以rocketMq为例，可以根据class或者topic配置不同，发送到不同的producer对象，这里就只有一个发送对象只存放topic
    //    private final Map<String, String> producerMap;

    private final ApplicationEventPublisher applicationEventPublisher;

    //<eventClassName, defaultPublishType>
    //    private final Map<String, EventPublishType> defaultPublishType;

    private final Map<String, EventPublishProperty> publishMap;

    private static EventPublishProperty DEAULT_PUBLISH_PROPERTY = EventPublishProperty.builder()
            .publishType(EventPublishType.LOCAL).build();

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        //        this.defaultPublishType = new HashMap<>();
        //        this.producerMap = new HashMap<>();
        this.publishMap = new HashMap<>();
    }

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher,
            Map<String, EventPublishProperty> publishMap) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.publishMap = publishMap;
    }

    public <T> void publish(T event) {
        Class key = event.getClass();
        // 尽量找到event类的配置，
        while (!publishMap.containsKey(event.getClass().getName())
                && key.getSuperclass() != null) {
            key = key.getSuperclass();
        }
        publish(event, publishMap.getOrDefault(key.getClass().getName(), DEAULT_PUBLISH_PROPERTY));
    }

    private <T> void publish(T event, EventPublishProperty publishProperty) {
        EventPublishType publishType = publishProperty.getPublishType();
        if (null == publishType) {
            publishType = EventPublishType.LOCAL;
        }
        switch (publishType) {
            case LOCAL:
                publishLocal(event);
                break;
            case DISTRIBUTE:
                publishDistribute(event, publishProperty);
                break;
            default:
                break;
        }
    }

    private <T> void publishLocal(T event) {
        log.info("event publishLocal,event:{}", event);
        if (event instanceof ApplicationEvent) {
            applicationEventPublisher.publishEvent(event);
        } else {
            applicationEventPublisher.publishEvent(new PayloadApplicationEvent<>(this, event));
        }
    }

    @SneakyThrows
    private <T> void publishDistribute(T event, EventPublishProperty publishProperty) {
        final EventDTO eventDTO = EventDTO.builder()
                .className(event.getClass().getName())
                .dataStr(event).build();
        String topic = publishMap.getOrDefault(publishProperty.getClassName(), DEAULT_PUBLISH_PROPERTY).getTopic();
        log.info("event publishDistribute,topic:{},dto:{}", topic, eventDTO);
        // 发到不同的topic
        //        rocketMQTemplate.send(topic, new GenericMessage(eventDTO));
        log.info("mock rocketMQTemplate send,topic:{},message:{}", topic, new GenericMessage(eventDTO));
    }
}
