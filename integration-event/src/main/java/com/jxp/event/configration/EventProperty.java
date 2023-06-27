package com.jxp.event.configration;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConditionalOnProperty(prefix = "integration-event", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "event")
@Component
@PropertySource(value = {"classpath:/application.yml"},encoding = "utf-8")
public class EventProperty {

    private List<EventPublishProperty> publishProperties;

    private List<EventConsumerProperty> consumerProperties;
}
