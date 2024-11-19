package com.jxp.integration.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-11-19 11:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class I18nVo {
    private String zhCN;
    private String enUS;
}
