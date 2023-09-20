package com.jxp.integration.test.config;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.jxp.integration.base.tool.JacksonUtils;
import com.jxp.integration.test.spider.domain.dto.CrawlerMetaDataConfig;
import com.jxp.integration.test.spider.domain.dto.CrawlerTaskDataConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-20 10:24
 */
@Configuration
@Slf4j
public class SpiderParseConfig implements InitializingBean {

    File spiderMetaData;

    File spiderTaskData;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            spiderMetaData = ResourceUtils.getFile("classpath:spider-meta-data.json");
            spiderTaskData = ResourceUtils.getFile("classpath:spider-task-data.json");
        } catch (Exception e) {
            log.error("SpiderParseConfig initialize error,", e);
        }
    }

    @Bean(name = "crawlerMetaDataConfigMap")
    public Map<String, CrawlerMetaDataConfig> crawlerMetaDataConfigMap() {

        Map<String, CrawlerMetaDataConfig> ret = Maps.newHashMap();
        if (null != spiderMetaData) {
            try {
                String content = new String(Files.readAllBytes(spiderMetaData.toPath()));
                ret = JacksonUtils.toBean(content, new TypeReference<Map<String, CrawlerMetaDataConfig>>() {
                });
            } catch (Exception e) {
                log.error("CrawlerMetaDataConfig initialize error,", e);
            }
        }
        return ret;
    }

    @Bean(name = "crawlerTaskDataConfigMap")
    public Map<String, CrawlerTaskDataConfig> crawlerTaskDataConfigMap() {
        Map<String, CrawlerTaskDataConfig> ret = Maps.newHashMap();
        if (null != spiderTaskData) {
            try {
                String content = new String(Files.readAllBytes(spiderTaskData.toPath()));
                ret = JacksonUtils.toBean(content, new TypeReference<Map<String, CrawlerTaskDataConfig>>() {
                });
            } catch (Exception e) {
                log.error("CrawlerTaskDataConfig initialize error,", e);
            }
        }
        return ret;
    }

}
