package com.jxp.config;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.google.common.collect.Maps;
import com.jxp.dto.bo.CrawlerDomainConfig;
import com.jxp.dto.bo.CrawlerMetaDataConfig;
import com.jxp.dto.bo.CrawlerTaskDataConfig;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-15 10:51
 */
@Configuration
@Slf4j
public class SpiderParseConfig implements InitializingBean {

    private File spiderMetaData;

    private File spiderTaskData;

    private File spiderDomainData;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            spiderMetaData = ResourceUtils.getFile("classpath:spider-meta-data.json");
            spiderTaskData = ResourceUtils.getFile("classpath:spider-task-data.json");
            spiderDomainData = ResourceUtils.getFile("classpath:spider-domain-data.json");
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
                ret = JSONUtil.toBean(content, new TypeReference<Map<String, CrawlerMetaDataConfig>>() {
                }, true);
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
                ret = JSONUtil.toBean(content, new TypeReference<Map<String, CrawlerTaskDataConfig>>() {
                }, true);
            } catch (Exception e) {
                log.error("CrawlerTaskDataConfig initialize error,", e);
            }
        }
        return ret;
    }

    @Bean(name = "crawlerDomainDataConfigMap")
    public Map<String, CrawlerDomainConfig> crawlerDomainDataConfigMap() {
        Map<String, CrawlerDomainConfig> ret = Maps.newHashMap();
        if (null != spiderTaskData) {
            try {
                String content = new String(Files.readAllBytes(spiderDomainData.toPath()));
                ret = JSONUtil.toBean(content, new TypeReference<Map<String, CrawlerDomainConfig>>() {
                }, true);
            } catch (Exception e) {
                log.error("CrawlerTaskDataConfig initialize error,", e);
            }
        }
        return ret;
    }

}
