package com.jxp.integration.test.spider.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-09-18 17:28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CrawlerTaskDataConfig {

    // 默认的解析方式：支持css，xpath1.0，css-list只适合content，不必做成配置
    private String method;
    // 有时为了将标题，摘要，封面和链接对应上，可以先解析出一组节点，然后从节点中解析数据，nodeList strList(默认)
    // 如果一个节点配置成nodeList，必须groupValue解析出来是个列表，其他的规则基于这个列表
    private String group;
    // 对应group的解析方式
    private String groupMethod;
    private String groupValue;

    // 解析链接
    private String link; //$.data[*].item_info.article_info.article_id
    private String linkMethod; //jsonpath
    private String linkPrefix; // https://juejin.cn/post/

    // 解析封面
    private String coverType;
    private String coverMethod;
    private String cover;
    private String coverPrefix;

    // 解析标题-as文件夹名称
    private String titleMethod;
    private String title;

    // 第三方业务id 类型uuid，parse=null
    private String thirdIdType;
    private String thirdIdMethod;
    private String thirdId;

    // 摘要生成类型
    private String descriptionType;
    // 从列表解析出来摘要，摘要类型透传到单条记录
    private String descriptionMethod;
    private String description;

    // 额外的解析规则，此值会透传给每一个链接揭露并存放到记录的map-json中
    private List<CrawlerConfigRow> extParseList;
}
