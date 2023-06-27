package com.jxp.es.service.impl;

import java.io.IOException;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.jxp.es.service.IndexService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.util.ObjectBuilder;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-19 10:44
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void addIndex(String name) throws IOException {
        ApplicationContext applicationContext;
        elasticsearchClient.indices().create(c -> c.index(name));
    }

    @Override
    public boolean indexExists(String name) throws IOException {
        ApplicationContext a;
        return elasticsearchClient.indices().exists(b -> b.index(name)).value();
    }

    @Override
    public void delIndex(String name) throws IOException {
        elasticsearchClient.indices().delete(c -> c.index(name));
    }

    @Override
    public void create(String name,
            Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> settingFn,
            Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn) throws IOException {
        elasticsearchClient
                .indices()
                .create(c -> c
                        .index(name)
                        .aliases("main-index", a -> a   //创别名
                                .isWriteIndex(true)
                        )
                        .settings(settingFn)
                        .mappings(mappingFn)
                );
    }
}
