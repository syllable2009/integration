package com.jxp.component.chatroom.codec;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-28 17:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMessageContent implements MessageContent {
    private String content;
    private final String messageType = "text";

    @Override
    @Nonnull
    public String getTextContent() {
        return content;
    }

}
