package com.jxp.integration.test.spider.domain.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-11-02 17:25
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskProcessData {

    private String link;
    private String title;
    private String desciption;
    private String cover;
    private Map<String, String> ext;
}
