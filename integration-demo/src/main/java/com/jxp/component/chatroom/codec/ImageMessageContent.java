package com.jxp.component.chatroom.codec;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-28 17:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageMessageContent implements MessageContent {
    private String messageType = "image";

    private Integer height;
    private String mediaId;
    private Integer width;
    private Long contentLength;
    private String url;


    @Override
    @Nonnull
    public String getTextContent() {
        return url;
    }
}
