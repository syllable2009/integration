package com.jxp.integration.base.feign;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Request.HttpMethod;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;

public class RequestQueryInterceptor implements RequestInterceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Set<String> matchMethods = new HashSet<>();

    public RequestQueryInterceptor() {
    }

    public RequestQueryInterceptor(HttpMethod... matchMethods) {
        if (matchMethods != null) {
            Arrays.stream(matchMethods).forEach(httpMethod -> {
                this.matchMethods.add(httpMethod.name());
            });
        }
    }

    @SneakyThrows
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (requestTemplate.body() == null) {
            return;
        }

        if (matchMethods.isEmpty() || matchMethods.contains(requestTemplate.method())) {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(requestTemplate.body());
            requestTemplate.body(null, Charset.defaultCharset());
            Map<String, Collection<String>> queries = buildQuery(jsonNode);
            requestTemplate.queries(queries);
        }
    }

    private Map<String, Collection<String>> buildQuery(JsonNode jsonNode) {
        Map<String, Collection<String>> queries = new HashMap<>();
        buildQuery(jsonNode, "", queries);
        return queries;
    }

    private void buildQuery(JsonNode jsonNode, String path, Map<String, Collection<String>> queries) {
        // 叶子节点
        if (!jsonNode.isContainerNode()) {
            if (jsonNode.isNull()) {
                return;
            }
            Collection<String> values = queries.computeIfAbsent(path, k -> new ArrayList<>());
            values.add(jsonNode.asText());
            return;
        }
        // 数组节点
        if (jsonNode.isArray()) {
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                buildQuery(it.next(), path, queries);
            }
        } else {
            Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (StringUtils.hasText(path)) {
                    buildQuery(entry.getValue(), path + "." + entry.getKey(), queries);
                } else {  // 根节点
                    buildQuery(entry.getValue(), entry.getKey(), queries);
                }
            }
        }
    }
}
