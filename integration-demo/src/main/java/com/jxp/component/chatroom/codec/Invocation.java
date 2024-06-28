package com.jxp.component.chatroom.codec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 20:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Invocation {
    private String type; // event,chat,chatroom,ack
    private String state; // send deliver read
    private String active; // recall delete
    private String message;
    private String fromId;
    private String toId;
    private String uuid;
    private Long timeStamp;
    private MessageContent content;
}
