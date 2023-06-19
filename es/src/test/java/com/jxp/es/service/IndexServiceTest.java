package com.jxp.es.service;

import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.IndexSettings.Builder;
import co.elastic.clients.util.ObjectBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-19 15:25
 */
@SpringBootTest
@Slf4j
class IndexServiceTest {

    @Resource
    IndexService indexService;

    @Test
    void addIndex() throws IOException {
        String indexName = "cosmo_v1";
        Assertions.assertFalse(indexService.indexExists(indexName));
        indexService.addIndex(indexName);
        Assertions.assertTrue(indexService.indexExists(indexName));
        indexService.delIndex(indexName);
        Assertions.assertFalse(indexService.indexExists(indexName));
    }

    @Test
    void indexExists() throws IOException {
        boolean exists = indexService.indexExists("cosmo_v1");
        log.info("exists:{}", exists);
    }

    @Test
    void delIndex() {

    }

    @Test
    void create() throws IOException {

        // 索引名
        String indexName = "cosmo_v1";

        // 构建setting时，builder用到的lambda
        Function<Builder, ObjectBuilder<IndexSettings>> settingFn = sBuilder -> sBuilder
                .index(iBuilder -> iBuilder
                        // 三个分片
                        .numberOfShards("3")
                        // 一个副本
                        .numberOfReplicas("1")
                );

        // 新的索引有三个字段，每个字段都有自己的property，这里依次创建
        Property keywordProperty = Property.of(pBuilder -> pBuilder.keyword(kBuilder -> kBuilder.ignoreAbove(256)));
        Property textProperty = Property.of(pBuilder -> pBuilder.text(tBuilder -> tBuilder));
        Property integerProperty = Property.of(pBuilder -> pBuilder.integer(iBuilder -> iBuilder));

        // // 构建mapping时，builder用到的lambda
        Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn = mBuilder -> mBuilder
                .properties("name", keywordProperty)
                .properties("description", textProperty)
                .properties("price", integerProperty);

        // 创建索引，并且指定了setting和mapping
        indexService.create(indexName, settingFn, mappingFn);
    }
}