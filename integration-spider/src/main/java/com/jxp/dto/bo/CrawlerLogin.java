package com.jxp.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-15 14:52
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CrawlerLogin {
    private String strOne;
    private String strTwo;
    private String strThree;
    private String strFour;
    private String strFive;
}
