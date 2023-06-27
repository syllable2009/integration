package com.jxp.event.configration;

import java.util.Set;

import lombok.Data;

@Data
public class EventConsumerProperty {

    private String topic;

    private Set<String> tag;

    private String consumerGroup;

    private String appKey;

}
