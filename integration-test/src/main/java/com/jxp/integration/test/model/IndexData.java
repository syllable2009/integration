package com.jxp.integration.test.model;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-14 20:07
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IndexData {
    @ExcelProperty(value = "字符串标题", index = 0, converter = CustomStringStringConverter.class)
    private String string;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "日期标题", index = 1)
    private Date date;
    /**
     * 这里设置3 会导致第二列空的
     */
    @NumberFormat("#.##%")
    @ExcelProperty(value = "数字标题", index = 3)
    private Double doubleData;
}
