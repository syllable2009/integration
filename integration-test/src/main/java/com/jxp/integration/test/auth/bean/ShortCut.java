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
public class ShortCut implements Serializable {
    private Long aid;
    private String id;
    // 空间类型：0全部；1我的空间；2共享空间；3收藏；4快速访问；5最近
    private String spaceType;// 我的云盘 共享云盘 知识库 归我所有(我的文档) 其他的单独处理：最近使用 分享给我 收藏夹 回收站,只有我的空间和知识库有继承模型
    private String domain;
    private Integer state; // 0 正常 1=删除
    // weight顺序,从小到大
    private float weight;
    //    private String pre;
    //    private String next;
    // shortcut，origin
    private String nodeType;
    // 如果是shortcut，此值才有意义
    private String originShortCutId;
    // 子父关系，唯一确定组织关系字段，为root/base表示为根节点
    private String parentId;
    // 一颗树一个treeId，方便批量查询内存计算
    private String treeId;
    // 文档id
    private String cosmoId;

    // 路径,可移动时路径变化太大，所以采用parentId
    private String pathValue;

}
