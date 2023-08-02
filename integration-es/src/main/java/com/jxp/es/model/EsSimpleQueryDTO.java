package com.jxp.es.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-20 11:21
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EsSimpleQueryDTO {

    //    @ApiModelProperty("索引名称")
    private String indexName;
    //    @ApiModelProperty("关键字属性")
    private String field;
    //    @ApiModelProperty("关键字")
    private String value;
    //    @ApiModelProperty("起始行")
    private Integer from;
    //    @ApiModelProperty("页数")
    private Integer size;

    private List<String> words;//关键字值数组

    private String order;//排序字段
    private String orderType;//排序方式 asc/desc

    public Integer getSize() {
        return size == 0 ? 30 : size;
    }

}
