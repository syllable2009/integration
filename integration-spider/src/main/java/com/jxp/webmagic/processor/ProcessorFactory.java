package com.jxp.webmagic.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jxp.webmagic.DefaultProcessor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-17 10:56
 */
@Component
public class ProcessorFactory {
    private final Map<String, DefaultProcessor> processorMap = new HashMap<>();

    @Autowired
    public void setprocessorMap(Set<DefaultProcessor> handlers) {
        handlers.forEach(handler -> processorMap.put(handler.getName(), handler));
    }

    public DefaultProcessor getDefaultProcessor(String domain) {
        return processorMap.get(domain);
    }

}
