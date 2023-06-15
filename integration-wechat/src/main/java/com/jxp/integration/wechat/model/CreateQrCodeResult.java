package com.jxp.integration.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jiaxiaopeng
 * Created on 2022-12-08 12:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateQrCodeResult extends BaseResult {
    private String ticket;
    @JsonProperty("expire_seconds")
    private int expireSeconds;
    private String url;
}
