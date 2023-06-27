package com.jxp.event.configration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-26 11:19
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "integration-event", name = "enable", havingValue = "true")
public class ConsumerAutoConfiguration {
}
