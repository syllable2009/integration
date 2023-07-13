package com.jxp.integration.test.auth.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 11:46
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cosmo implements Serializable {

    private Long aid;
    private String id;
    // 文档类型： 0全部；1文件夹；2文档；3表单；4云盘；5表格；6演示文稿；7群组；8.知识库
    private String cosmoType;
    private Integer state; // 0正常 1=回收站 2=正常
    private String firstShortcut;
}
