package com.jxp.component.chatroom.codec;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-28 17:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMessageContent implements MessageContent {
    private final String messageType = "custom";
    private String payload;

    @Override
    @Nonnull
    public String getTextContent() {
        return payload;
    }

}
