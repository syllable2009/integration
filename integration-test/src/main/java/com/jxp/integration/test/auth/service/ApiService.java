package com.jxp.integration.test.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jxp.integration.test.auth.bean.CosmoEntityValue;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-12 20:31
 */
public interface ApiService {

    /**
     * 一级一级获取完整目录结构,无权限也会返回无权限
     *
     * @param spaceType 支持我的云盘 共享云盘 知识库 归我所有(我的文档)
     * @param root parentId，如果为空则为根节点
     */
    List<String> getPath(String spaceType, String root);

    // 在顶层有匿名权限和管理员权限之分
    Map<String, List<CosmoEntityValue>> getAllAuthValues(Collection<String> cosmoIds, Collection<String> entityIds);

    Map<String, CosmoEntityValue> getHighAuth(Collection<String> cosmoIds, String userId);
}
