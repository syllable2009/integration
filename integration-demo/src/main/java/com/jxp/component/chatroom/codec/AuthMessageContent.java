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
public class AuthMessageContent implements MessageContent {
    private final String messageType = "auth";
    private String userId;
    private String password;
    private String token;

    @Override
    @Nonnull
    public String getTextContent() {
        return userId;
    }

}
