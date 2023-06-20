package com.jxp.es.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-20 11:21
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EsQueryDTO {

    private String collectionId;
    // 外层默认or的关系：name=张三,age=14 or name=李四,age=15
    private List<FilterDto> filters;
    private List<SortDto> sort;
    private Integer limit;
    private GroupDto group;
    private List<String> extraBlockIds;


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class SortDto {
        private String property;
        private String sortOrder;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class FilterDto {
        private String operator; // and代表条件都必须满足，or代表任何一个条件都可以
        private List<SingleFilter> filters; // 规则组

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SingleFilter {
            private String property; // 单规则
            // not_contain is_before
            private String operator;
            private String type; // 字段类型，如date、number,list等
            // value值
            private Object value;
        }
    }

    @Data
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupDto {
        private String property; // 分组属性值
        private String direction; // 排列顺序
        private String type; // 字段类型，如date、number等
        private String start; // 起始值
        private String end; // 结束值
        private String interval; // 间隔
        private List<GroupInfo> groups; // 指定的分组顺序

        @Data
        @NoArgsConstructor
        public static class GroupInfo {
            private String value;
            private int limit;
            private String type; // 分组值类型：default/custom
            private String start; // 起始值
            private String end; // 结束值
        }
    }
}
