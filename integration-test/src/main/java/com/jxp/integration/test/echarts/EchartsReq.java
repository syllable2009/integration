package com.jxp.integration.test.echarts;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-07 14:54
 */

@Data
public class EchartsReq {

    @NotNull
    private EchartType echartType;

    @NotNull
    private String bizType;
}
