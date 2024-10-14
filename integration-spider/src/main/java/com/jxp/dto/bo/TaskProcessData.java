package com.jxp.dto.bo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 20:26
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
