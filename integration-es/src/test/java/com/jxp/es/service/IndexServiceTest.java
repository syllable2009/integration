package com.jxp.es.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

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
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch.core.ScrollResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
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

    void testQuery(Function<Query.Builder, ObjectBuilder<Query>> fn) {
        try {
            List<Product> list =
                    elasticsearchClientUtil.defaultQueryList(elasticsearchClient, index, Product.class, fn);
            log.info("query result, data:{}", JSONUtil.toJsonStr(list));
        } catch (Exception ex) {
            log.error("exception e", ex);
        }
    }

    @Test
    void test1() {
        // term
        Function<Query.Builder, ObjectBuilder<Query>> fn1 = q ->
                q.term(t -> t
                        .field("name")
                        .value("bug2")
                );
        testQuery(fn1);
        BoolQuery bq = BoolQuery
                .of(e -> e.must(fn1).must(s -> s.term(TermQuery.of(t -> t.field("description").value("6lfdv")))));
        Function<Query.Builder, ObjectBuilder<Query>> fn2 = q ->
                q.bool(bq);
        testQuery(fn2);

        BoolQuery bq3 = BoolQuery
                .of(e -> e.should(fn1).should(s -> s.term(TermQuery.of(t -> t.field("description").value("ksh3e")))));
        testQuery(t -> t.bool(bq3));
        // match,英文的分词以空格为主
        MatchQuery m1 = MatchQuery.of(e -> e.field("description").query("ksh"));
        testQuery(t -> t.match(m1));
    }

    @Test
    void test2() {
        // match,ik分词
        MatchQuery m1 = MatchQuery.of(e -> e.field("description").query("中国"));
        testQuery(t -> t.match(m1));

        MultiMatchQuery mq = QueryBuilders.multiMatch().fields("description", "name").query("ksh")
                .build();
        testQuery(t -> t.multiMatch(mq));
    }

    @Test
    void test3() {
        // 条件 or and
    }

    @Test
    void test4() {
        // 过滤filter
    }

    @Test
    void test5() {
        // 聚合
    }

    @Test
    void test6() {
        // 分页1
    }

    @Test
    void test7() {
        // 分页2
    }

    @Test
    void test8() throws IOException {
        // 构建查询条件
        List<Query> queryList = new ArrayList<>();

        // MatchPhraseQuery
        String name = "张三 李四";
        Query byName = MatchPhraseQuery.of(m -> m
                .field("name")
                .query(name)
        )._toQuery();

        // RangeQuery
        Query byAge1 = RangeQuery.of(r -> r
                .field("age")
                .gte(JsonData.of(10))
        )._toQuery();

        Query byAge2 = RangeQuery.of(r -> r
                .field("age")
                .lte(JsonData.of(13))
        )._toQuery();

        // TermsQuery
        List<FieldValue> ls = new ArrayList<>();
        ls.add(FieldValue.of("男"));
        ls.add(FieldValue.of("女"));
        Query termsQuery = TermsQuery.of(t -> t.field("sex").terms(terms -> terms.value(ls)))._toQuery();

        queryList.add(byName);
        queryList.add(byAge1);
        queryList.add(byAge2);
        queryList.add(termsQuery);

        // 普通查询
        SearchResponse<Product> search2 = elasticsearchClient.search(s -> s.index(index)
                .query(q -> q.bool(b -> b.must(queryList))), Product.class
        );

        // scroll第一次查询
        SearchResponse<Product> response = elasticsearchClient.search(s -> s
                        .index("user")
                        .query(q -> q.bool(b -> b.must(queryList)))
                        .size(5000)
                        .scroll(t -> t.time("5m")),                                               //开启scroll
                Product.class
        );

        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit : hits) {
            //            userList.add(hit.source());
        }
        String scrollId = response.scrollId();
        List<String> strings = new ArrayList<>();
        strings.add(scrollId);
        ScrollResponse<Product> search = null;
        do {
            String scrollIdTemp = scrollId;
            log.info(scrollIdTemp);
            search = elasticsearchClient.scroll(s -> s.scrollId(scrollIdTemp).scroll(t -> t.time("5m")), Product.class);
            for (Hit<Product> hit : search.hits().hits()) {
                // userList.add(hit.source());
            }
            scrollId = search.scrollId();
            strings.add(scrollId);
        } while (!search.hits().hits().isEmpty());
        // 手动清除scrollId
        elasticsearchClient.clearScroll(c -> c.scrollId(strings));

    }

    @Test
    void test9() throws IOException {
        // 如果你觉得这种调用各种方法拼接参数的方式不习惯，那么也可以直接上 JSON，如下：
        String key = "bug2";
        StringReader sr = new StringReader("{\n" +
                "  \"query\": {\n" +
                "    \"term\": {\n" +
                "      \"name\": {\n" +
                "        \"value\": \"" + key + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
        SearchRequest request = new SearchRequest.Builder()
                .index(index)
                .withJson(sr)
                .build();
        SearchResponse<Product> search = elasticsearchClient.search(request, Product.class);
        List<Product> result = elasticsearchClientUtil.getResult(search);
        log.info("result:{}", JSONUtil.toJsonStr(result));
    }

    @Test
    void testTerm() throws Exception {

        EsSimpleQueryDTO dto = EsSimpleQueryDTO.builder()
                .indexName(index)
                .field("name")
                .value("Bug2")
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
        String searchText = "bike";

        MatchQuery nameQuery = MatchQuery.of(m -> m
                .field("name")
                .query(searchText));

        MatchQuery descriptionQuery = MatchQuery.of(m -> m
                .field("description")
                .query("4yw47"));

        SearchResponse<Product> response = elasticsearchClient.search(s -> s
                        .index(index)
                        .query(q -> q.bool(b -> b.must(Query.of(w -> w.match(nameQuery)))
                                .must(Query.of(w -> w.match(descriptionQuery))))
                        ).size(100),
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
        Stream.of("我是中国人", "中国的地方很大", "中国的gdp在下降", "中国人口数量在下降", "ksh tank", "siri ksh", "halo ksh")
                .forEach(e -> productList.add(Product.builder()
                        .id(IdUtil.fastSimpleUUID())
                        .name(e)
                        .price(RandomUtil.randomInt(1, 10000))
                        .description(e)
                        .build()));
        //        for (int i = 0; i < 10000; i++) {
        //            productList.add(Product.builder()
        //                    .id(IdUtil.fastSimpleUUID())
        //                    .name(RandomUtil.randomString(4))
        //                    .price(RandomUtil.randomInt())
        //                    .description(RandomUtil.randomString(5))
        //                    .build());
        //        }
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