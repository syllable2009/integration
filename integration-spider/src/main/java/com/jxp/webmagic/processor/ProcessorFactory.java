package com.jxp.webmagic.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jxp.webmagic.DefaultProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-17 10:56
 */
@Slf4j
@Component
public class ProcessorFactory {
    private final Map<String, DefaultProcessor> processorMap = new HashMap<>();

    @Autowired
    public void setProcessorMap(List<DefaultProcessor> handlers) {
        log.info("handlers:{}", handlers.size());
        handlers.forEach(handler -> processorMap.put(handler.getName(), handler));
    }

    public DefaultProcessor getDefaultProcessor(String domain) {
        return processorMap.get(domain);
    }

}
