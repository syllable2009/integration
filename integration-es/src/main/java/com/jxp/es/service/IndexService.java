package com.jxp.es.service;

import java.io.IOException;
import java.util.function.Function;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.util.ObjectBuilder;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-19 10:43
 */
public interface IndexService {

    /**
     * 新建指定名称的索引
     */
    void addIndex(String name) throws IOException;

    /**
     * 检查制定名称的索引是否存在
     */
    boolean indexExists(String name) throws IOException;

    /**
     * 删除指定索引
     */
    void delIndex(String name) throws IOException;

    /**
     * 创建索引，指定setting和mapping
     *
     * @param name 索引名称
     * @param settingFn 索引参数
     * @param mappingFn 索引结构
     */
    void create(String name,
            Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> settingFn,
            Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn) throws IOException;
}
