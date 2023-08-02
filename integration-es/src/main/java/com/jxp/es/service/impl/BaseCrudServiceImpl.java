package com.jxp.es.service.impl;

import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jxp.es.model.Product;
import com.jxp.es.service.BaseCrudService;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * @author jiaxiaopeng
 * Created on 2023-06-19 15:31
 */

@ConditionalOnBean(ElasticsearchClient.class)
@Service
@Slf4j
public class BaseCrudServiceImpl implements BaseCrudService {
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private ElasticsearchAsyncClient elasticsearchAsyncClient;


    @Override
    public Product search(String index, String id) {
        return null;
    }

    @Override
    public IndexResponse createByFluentDSL(String index, Product product) throws Exception {
        return null;
    }

    @Override
    public IndexResponse createByBuilderPattern(String index, Product product) throws Exception {
        return null;
    }

    @Override
    public IndexResponse createByJSON(String index, String id, String jsonContent) throws Exception {
        return null;
    }

    @Override
    public BulkResponse bulkCreate(String index, List<Product> products) throws Exception {
        BulkRequest.Builder br = new BulkRequest.Builder();

        // 将每一个product对象都放入builder中
        products.stream()
                .forEach(product -> br.routing(product.getId())
                        .operations(op -> op
                                .index(idx -> idx
                                        .index(index)
                                        .id(product.getId())
                                        .document(product))));

        return elasticsearchClient.bulk(br.build());
    }

    @Override
    public ObjectNode getObjectNode(String index, String id) throws Exception {
        return null;
    }

    @Override
    public BulkResponse bulkDelete(String index, List docIds) throws Exception {
        return null;
    }


    @Override
    public void createAnsync(String index, Product product, BiConsumer action) {

    }
}
