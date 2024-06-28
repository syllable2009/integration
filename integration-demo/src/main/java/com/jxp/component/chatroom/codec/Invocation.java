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
    private String type;
    private String message;
}
