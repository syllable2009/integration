//package com.jxp.es.service.impl;
//
//import java.io.StringReader;
//import java.util.List;
//import java.util.function.BiConsumer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.jxp.es.service.BaseCrudService;
//
//import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.IndexRequest;
//import co.elastic.clients.elasticsearch.core.IndexResponse;
//import lombok.extern.slf4j.Slf4j;
//
//import co.elastic.clients.elasticsearch.core.*;
//
//import com.fasterxml.jackson.databind.node.ObjectNode;
//
//
///**
// * @author jiaxiaopeng
// * Created on 2023-06-19 15:31
// */
//
//@Service
//@Slf4j
//public class BaseCrudServiceImpl<T> implements BaseCrudService {
//    @Autowired
//    private ElasticsearchClient elasticsearchClient;
//
//    @Autowired
//    private ElasticsearchAsyncClient elasticsearchAsyncClient;
//
//    @Override
//    public T search(String index, String id) {
//
//        GetResponse<T> response = null;
//        try {
//            response = elasticsearchClient.get(g -> g
//                            .index(index)
//                            .id(id),
//                    T.class);
//        } catch (Exception exception) {
//            log.error("query [" + index + "] by id [" + id + "] error", exception);
//        }
//
//        return response.found() ? response.source() : null;
//    }
//
//    @Override
//    public IndexResponse createByFluentDSL(String index, T product) throws Exception {
//        return elasticsearchClient.index(i -> i
//                .index(index)
//                .id(product.getId())
//                .document(product)
//        );
//    }
//
//    @Override
//    public IndexResponse createByBuilderPattern(String index, T product) throws Exception {
//        IndexRequest.Builder<T> indexReqBuilder = new IndexRequest.Builder<>();
//
//        indexReqBuilder.index(index);
//        indexReqBuilder.id(product.getId());
//        indexReqBuilder.document(product);
//
//        return elasticsearchClient.index(indexReqBuilder.build());
//    }
//
//    @Override
//    public void createAnsync(String index, T product, BiConsumer<IndexResponse, Throwable> action) {
//        elasticsearchAsyncClient.index(i -> i
//                .index(index)
//                .id(product.getId())
//                .document(product)
//        ).whenComplete(action);
//    }
//
//    @Override
//    public IndexResponse createByJSON(String index, String id, String jsonContent) throws Exception {
//        return elasticsearchClient.index(i -> i
//                .index(index)
//                .id(id)
//                .withJson(new StringReader(jsonContent))
//        );
//    }
//
//    @Override
//    public BulkResponse bulkCreate(String index, List<T> products) throws Exception {
//        BulkRequest.Builder br = new BulkRequest.Builder();
//
//        // 将每一个product对象都放入builder中
//        products.stream()
//                .forEach(product -> br
//                        .operations(op -> op
//                                .index(idx -> idx
//                                        .index(index)
//                                        .id(product.getId())
//                                        .document(product))));
//
//        return elasticsearchClient.bulk(br.build());
//    }
//
//    @Override
//    public BulkResponse bulkDelete(String index, List<String> docIds) throws Exception {
//        BulkRequest.Builder br = new BulkRequest.Builder();
//
//        // 将每一个product对象都放入builder中
//        docIds.stream()
//                .forEach(id -> br
//                        .operations(op -> op
//                                .delete(d -> d
//                                        .index(index)
//                                        .id(id))));
//
//        return elasticsearchClient.bulk(br.build());
//    }
//
//    @Override
//    public ObjectNode getObjectNode(String index, String id) throws Exception {
//        GetResponse<ObjectNode> response = elasticsearchClient.get(g -> g
//                        .index(index)
//                        .id(id),
//                ObjectNode.class);
//
//        return response.found() ? response.source() : null;
//    }
//}
