package com.jxp.event.configration;

import com.jxp.event.enums.EventPublishType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventPublishProperty {

    private String className;

    private EventPublishType publishType;

    private String topic;

    private String appKey;

}
