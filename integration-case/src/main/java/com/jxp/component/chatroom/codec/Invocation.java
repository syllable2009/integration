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
    private String type; // auth,enter,leave,chat,chatroom,ack
    private String state; // send,deliver,read
    private String active; // recall,delete
    private String fromId;
    private String toId;
    private String uuid;
    private Long timeStamp;
    private String content;
}
