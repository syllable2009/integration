package com.jxp.integration.wechat.model;

import lombok.Data;

@Data
public abstract class BaseResult {
    private Integer errcode;
    private String errmsg;
}
