package com.jxp.dto.bo;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-10-14 17:23
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingleAddressResp {

    private Long aid;

    private String uid;

    // 地址：请求的链接
    private String url;

    // 链接：文档的原始地址
    private String link;

    // 标题
    private String title;

    // 摘要
    private String description;

    // 封面
    private String cover;

    // 域
    private String domain;

    // 状态：当前文档的状态，默认为0
    private Integer state;

    // 是否重复
    private Boolean ifRepeated;

    // 解析器名称：默认实现为default，可自定义实现
    private String processorName;

    // 使用的配置名称
    private String configName;

    // 文档内容
    private List<String> content;

    // 创建人
    private String userId;

    // 来源：task/single
    private String base;

    // 三方业务id，用来支持获取列表数据时，无法用url来区分的情况，比如微博的最新回复
    private String thirdId;

    // 图片附件列表
    private List<String> picList;

    // 额外解析的数据
    private Map<String, String> ext;
}
