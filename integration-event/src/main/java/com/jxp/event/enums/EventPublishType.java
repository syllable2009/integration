package com.jxp.event.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventPublishType {

    LOCAL,
    DISTRIBUTE;

    @JsonCreator
    public static EventPublishType of(String typeStr) {
        try {
            return valueOf(typeStr);
        } catch (Exception e) {
            return LOCAL;
        }
    }
}
