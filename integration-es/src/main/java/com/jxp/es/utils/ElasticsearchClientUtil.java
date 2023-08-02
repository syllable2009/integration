package com.jxp.es.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.jxp.es.model.EsQueryDTO;
import com.jxp.es.model.EsQueryDTO.FilterDto;
import com.jxp.es.model.EsSimpleQueryDTO;

import cn.hutool.core.collection.CollectionUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.util.ObjectBuilder;
import jakarta.json.stream.JsonGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-20 11:18
 * 通常，全文搜索或需要用到相关性评分（score）的场景采用查询（query），其他的全部用过滤（filter）。
 */
@Slf4j
@ConditionalOnBean(ElasticsearchClient.class)
@Component
public class ElasticsearchClientUtil<T> {


    private final static String[] sources = new String[] {"name", "id", "description"};

    public List<T> defaultQueryList(ElasticsearchClient elasticsearchClient, String index, Class<T> targetClass,
            Function<Builder, ObjectBuilder<Query>> fn)
            throws IOException {

        SearchResponse<T> response = elasticsearchClient
                .search(s -> s.index(index)
                                .query(fn)
                                .from(null)
                                .size(30)
                                .sort(sort -> sort.field(f -> f.field("name").order(SortOrder.Asc)))
                                .source(sc -> sc.filter(f -> f.includes(Lists.newArrayList(sources)))),
                        targetClass);

        return getResult(response);
    }

    public String printEsBySearchRequest(SearchRequest searchRequest) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonpMapper mapper = new JacksonJsonpMapper();
        JsonGenerator generator = mapper.jsonProvider().createGenerator(byteArrayOutputStream);
        mapper.serialize(searchRequest, generator);
        generator.close();
        return byteArrayOutputStream.toString();
    }


    // “term 查询可以用于精确匹配一个字段中的某个值,不会对输入做任何处理
    // match查询会先对搜索词进行分词,分词完毕后再逐个对分词结果进行匹配,match_phrase也可以叫做紧邻搜索
    // bool查询
    //参数1：must 必须匹配
    //参数2：must_not 必须不匹配
    //参数3：should 默认情况下，should语句一个都不要求匹配，只有一个特例：如果查询中没有must语句，那么至少要匹配一个should语句
    public List<T> termPageQueryList(ElasticsearchClient elasticsearchClient, EsSimpleQueryDTO dto,
            Class<T> targetClass)
            throws IOException {
        //        SortOptions sortOptions =
        //                SortOptions.of(s -> s.field(f -> f.field("").order(SortOrder.valueOf(""))));

        SearchResponse<T> response = elasticsearchClient.search(s -> s
                        .index(dto.getIndexName())
                        .query(q ->
                                q.term(t -> t
                                        .field(dto.getField())
                                        .value(dto.getValue())
                                ))

                        //                        .sort(Lists.newArrayList(sortOptions))
                        .from(dto.getFrom()).size(dto.getSize()),
                targetClass);

        return getResult(response);
    }

    public long termCount(ElasticsearchClient client, EsSimpleQueryDTO dto) throws Exception {
        CountResponse count = client.count(c -> c.index(dto.getIndexName()).query(q -> q.term(t -> t
                .field(dto.getField())
                .value(dto.getValue())
        )));
        return count.count();
    }


    public List<T> matchQueryList(ElasticsearchClient elasticsearchClient, EsQueryDTO dto, Class<T> targetClass)
            throws IOException {

        // 构建请求
        List<FilterDto> filters = dto.getFilters();
        if (CollectionUtil.isNotEmpty(filters)) {

            Query byName = MatchQuery.of(m -> m
                    .field("name")
                    .query("searchText")
            )._toQuery();

            // Search by max price
            Query byMaxPrice = RangeQuery.of(r -> r
                    .field("price")
                    .gte(JsonData.of(1))
            )._toQuery();
        }
        // 分组

        // 处理排序
        List<SortOptions> sortOptions = null;
        if (CollectionUtil.isNotEmpty(dto.getSort())) {
            sortOptions = dto.getSort()
                    .stream()
                    .map(e -> SortOptions
                            .of(s -> s.field(f -> f.field(e.getProperty()).order(SortOrder.valueOf(e.getSortOrder())))))
                    .collect(Collectors.toList());
        }

        List<SortOptions> finalSortOptions = sortOptions;
        SearchResponse<T> response = elasticsearchClient.search(s -> s
                .index("cosmo_v1")
                .query(q -> q.bool(a -> a.must(MatchQuery.of(m -> m
                        .field("name")
                        .query("searchText")
                )._toQuery())
                        .should(Lists.newArrayList())))
                .sort(finalSortOptions), targetClass
        );
        return getResult(response);
    }

    /**
     * 构件复合查询条件
     */
    private static List<Query> getFieldValues(EsQueryDTO dto, List<Query> queries) {
        List<Query> ret = Lists.newArrayList();
        List<FieldValue> fieldValues = new ArrayList<>();
        //        //根据关键字列表构件复合查询的值
        //        dto.getWords().stream().forEach(word -> fieldValues.add(FieldValue.of(word)));
        //        //查询条件列表
        //        ret.add(Query.of(q -> q.terms(t -> t.field(dto.getField()).terms(v -> v.value(fieldValues)))));

        return ret;
    }


    public List<T> getResult(SearchResponse<T> response) {
        long total = response.hits().total().value();
        log.info("covert result, total:{}", total);
        List<Hit<T>> hits = response.hits().hits();
        return hits.stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
