package com.jxp.component.chatroom.handle;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 21:55
 */

@Component
@Slf4j
public class MsgHandleContainer implements InitializingBean {

    private final Map<String, MsgHandle> handleMap = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(MsgHandle.class)
                .values()
                .forEach(e -> handleMap.put(e.getType(), e));

    }

    public MsgHandle getMessageHandler(String type) {
        return handleMap.getOrDefault(type, null);
    }
}
