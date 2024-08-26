package com.jxp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-08-22 16:20
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocMarkDownConfig {
    // 大标题
    private String title;
    // 小标题
    private String header;
    // 文档内容
    private List<String> content;
    // 分隔符
    private String segment;
}
