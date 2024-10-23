package com.jxp.util;

import static com.fasterxml.jackson.core.JsonFactory.Feature.INTERN_FIELD_NAMES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Streams;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


/**
 * @author
 * Created on 2023-02-10
 */
@Slf4j
@UtilityClass

public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper(
            new JsonFactory().disable(INTERN_FIELD_NAMES)).registerModule(new GuavaModule());


    /*
     * 有写可能会在 spring IOC 容器没有设置完成前调用 ObjectMapper
     */
    static {
        MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.enable(ALLOW_UNQUOTED_CONTROL_CHARS);
        MAPPER.enable(ALLOW_COMMENTS);
        MAPPER.enable(ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
        MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL); //不返回 Null 值
        MAPPER.registerModule(new ParameterNamesModule());
        MAPPER.disable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        MAPPER.registerModule(javaTimeModule);
    }

    public static <T> T jsonToObj(String content, Class<T> valueType) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
        try {
            return MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            log.error("jsonToObj error,  target type: {}, content: {}", valueType, content);
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObj(byte[] content, Class<T> valueType) {
        try {
            return MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String objToJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] objToJsonByte(Object value) {
        try {
            return MAPPER.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> jsonToArray(String content, Class<T> valueType) {
        try {
            return MAPPER.readValue(content,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode get(ObjectNode target, String path) {
        if (target == null || target.isMissingNode() || target.isNull()) {
            return MissingNode.getInstance();
        }
        if (StrUtil.isBlank(path)) {
            return target;
        }
        String pathSplitter = ".";
        String fieldName = path.contains(pathSplitter) ? StrUtil.subBefore(path,
                pathSplitter, false) : path;
        JsonNode currentNode;
        String arrayElementPathRegex = "(.*)\\[(\\d)\\]";
        Matcher matcher = Pattern.compile(arrayElementPathRegex).matcher(fieldName);

        if (!matcher.find()) {
            currentNode = target.get(fieldName);
        } else {
            String realFieldName = matcher.group(1);
            int index = Integer.parseInt(matcher.group(2));
            currentNode = target.get(realFieldName).get(index);
        }

        if (path.contains(pathSplitter)) {
            return get((ObjectNode) currentNode, StrUtil.subAfter(path, pathSplitter, false));
        }
        return currentNode;

    }

    @SneakyThrows
    public static <T> T fromJSON(JsonNode value, Class<T> valueType) {
        if (value == null) {
            return null;
        }
        return MAPPER.treeToValue(value, valueType);
    }

    @SneakyThrows
    public static <T> List<T> toArray(JsonNode json, Class<T> clz) {
        if (json == null || json.isMissingNode() || json.isNull()) {
            return Collections.emptyList();
        }

        if (!(json instanceof ArrayNode)) {
            throw new RuntimeException("输入节点不是ArrayNode，无法转为List: json = " + json);
        }
        return Streams.stream(json.iterator()).map(node -> fromJSON(node, clz))
                .collect(Collectors.toList());
    }

    public static Map<String, Object> objToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }

        return MAPPER.convertValue(obj, Map.class);
    }

    public static <T> T mapToObj(Map<String, Object> map, Class<T> valueType) {
        return MAPPER.convertValue(map, valueType);
    }

    @SneakyThrows
    public JsonNode readTree(String json) {
        return MAPPER.readTree(json);
    }

    /**
     * 处理转义符
     * @param originStr
     * @return
     */
    public String prepareStrInJson(String originStr) {
        return originStr.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }


}
