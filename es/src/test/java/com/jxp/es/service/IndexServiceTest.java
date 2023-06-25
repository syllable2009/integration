package com.jxp.es.service;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Lists;
import com.jxp.es.model.EsSimpleQueryDTO;
import com.jxp.es.model.Product;
import com.jxp.es.utils.ElasticsearchClientUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.ScrollResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.IndexSettings.Builder;
import co.elastic.clients.json.JsonData;
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
    @Resource
    BaseCrudService baseCrudService;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    private static final String index = "cosmo_v1";

    @Resource
    ElasticsearchClientUtil elasticsearchClientUtil;

    @Test
    void testTerm() throws Exception {

        EsSimpleQueryDTO dto = EsSimpleQueryDTO.builder()
                .indexName(index)
                .field("name")
                .value("halo")
                .from(null) // 分页从0开始，null和0等效
                .size(50)
                .build();

        long count = elasticsearchClientUtil.termCount(elasticsearchClient, dto);
        log.info("count,{}", count);
        List<Product> term = elasticsearchClientUtil.termPageQueryList(elasticsearchClient, dto, Product.class);
        log.info("terms,{}", term);
    }


    @Test
    void simpleSearchQuery() throws IOException {
        String searchText = "duck";
        SearchResponse<Product> response = elasticsearchClient.search(s -> s
                        .index(index)
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        ),
                Product.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            log.info("There are {} results", total.value());
        } else {
            log.info("There are more than {} results", total.value());
        }

        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit : hits) {
            Product product = hit.source();
            log.info("Found product {}" + product);
        }
    }

    @Test
    void mustSearchQuery() throws IOException {
        String searchText = "bike";
        double maxPrice = -1990188287;

        // Search by product name
        Query byName = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        // Search by max price
        Query byMaxPrice = RangeQuery.of(r -> r
                .field("price")
                .gte(JsonData.of(maxPrice))
        )._toQuery();

        // Combine name and price queries to search the product index
        SearchResponse<Product> response = elasticsearchClient.search(s -> s
                        .index(index)
                        .query(q -> q
                                .bool(b -> b
                                        .must(byName)
                                        .must(byMaxPrice)
                                )
                        ),
                Product.class
        );

        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit : hits) {
            Product product = hit.source();
            log.info("Found product {} " + product);
        }
    }

    @Test
    void score() {
    }

    @Test
    void scroll() throws IOException {
        List<SortOptions> sorts = Lists.newArrayList();
        sorts.add(SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Desc))));

        SearchResponse<Product> response = elasticsearchClient.search(s -> s
                        .index(index)
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query("bike")
                                )
                        ).size(5000).scroll(t -> t.time("5s"))
                        .sort(sorts),
                Product.class
        );
        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit : hits) {
            Product product = hit.source();
            log.info("Found product {} " + product);
        }

        String scrollId = response.scrollId();
        ScrollResponse<Product> scroll =
                elasticsearchClient.scroll(s -> s.scrollId(scrollId).scroll(t -> t.time("5s")), Product.class);
        List<Hit<Product>> hits1 = scroll.hits().hits();
        while (CollectionUtil.isNotEmpty(hits1)) {
            for (Hit<Product> hit : hits1) {
                Product product = hit.source();
                log.info("scroll Found product {} " + product);
            }
            hits1 = elasticsearchClient.scroll(s -> s.scrollId(scrollId).scroll(t -> t.time("5s")), Product.class)
                    .hits().hits();
        }
        //        elasticsearchClient.clearScroll(c -> c.scrollId(scrollId));
        return;
    }

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
        //        List<String> randomFive = RandomUtil.randomEles(Lists.newArrayList(""), 5);
        boolean exists = indexService.indexExists("cosmo_v1");
        log.info("exists:{}", exists);
    }

    @Test
    void add() throws Exception {
        List<Product> productList = Lists.newArrayList();
        //        Stream.of("bike", "mike", "hike", "bike", "tank", "siri", "halo")
        //                .forEach(e -> productList.add(Product.builder()
        //                        .id(IdUtil.fastSimpleUUID())
        //                        .name(e)
        //                        .price(RandomUtil.randomInt())
        //                        .description(RandomUtil.randomString(5))
        //                        .build()));
        for (int i = 0; i < 10000; i++) {
            productList.add(Product.builder()
                    .id(IdUtil.fastSimpleUUID())
                    .name("bike")
                    .price(RandomUtil.randomInt())
                    .description(RandomUtil.randomString(5))
                    .build());
        }
        baseCrudService.bulkCreate(index, productList);
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
        Property textProperty = Property.of(
                pBuilder -> pBuilder.text(tBuilder -> tBuilder.analyzer("ik_max_word").searchAnalyzer("ik_smart")));
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