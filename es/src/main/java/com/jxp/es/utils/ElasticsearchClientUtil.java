package com.jxp.es.utils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.jxp.es.model.EsQueryDTO;
import com.jxp.es.model.EsQueryDTO.FilterDto;

import cn.hutool.core.collection.CollectionUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-20 11:18
 */
public class ElasticsearchClientUtil<T> {

    public List<T> query(ElasticsearchClient elasticsearchClient, EsQueryDTO dto, Class<T> targetClass)
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
                .index("index")
                .query(q -> q
                        .match(t -> t
                                .field("name")
                                .query("searchText")
                        )
                ).sort(finalSortOptions), targetClass
        );
        return getResult(response);
    }


    private List<T> getResult(SearchResponse<T> response) {
        long value = response.hits().total().value();
        List<Hit<T>> hits = response.hits().hits();
        return hits.stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
