package com.jxp.integration.wechat.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jiaxiaopeng
 * Created on 2022-11-24 10:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateMessageResult extends BaseResult {
    private Long msgid;
}