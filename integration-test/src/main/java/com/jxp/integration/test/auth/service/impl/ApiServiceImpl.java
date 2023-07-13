package com.jxp.integration.test.auth.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import com.jxp.integration.test.auth.bean.Cosmo;
import com.jxp.integration.test.auth.bean.ShortCut;
import com.jxp.integration.test.auth.service.ApiService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-12 20:31
 */

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    @Override
    public List<String> getPath(String spaceType, String root) {
        String domainId = "default";
        String userId = "userId";
        List<ShortCut> shortCutList = Lists.newArrayList();
        // 可以缓存成zset？
        if (StrUtil.isBlank(root)) {
            // 根节点:select * from ShortCut where domainId = #{} and spaceType = #{} and parentId is null;
        } else {
            // 非根节点:select * from ShortCut where domainId = #{} and spaceType = #{} and parentId = root;
        }
        Map<Boolean, List<ShortCut>> shortcutNodeMap = shortCutList.stream()
                .collect(Collectors.groupingBy(e -> StrUtil.equals("shortcut", e.getNodeType())));

        List<ShortCut> shortCutsNode = shortcutNodeMap.get(Boolean.TRUE);
        // 转换成原始节点
        Map<String, String> relateMap = shortCutsNode.stream()
                .collect(Collectors.toMap(ShortCut::getId, ShortCut::getCosmoId));

        relateMap.values();
        List<Cosmo> cosmoList = Lists.newArrayList();
        Map<String, String> relate2 = cosmoList.stream()
                .collect(Collectors.toMap(Cosmo::getId, Cosmo::getFirstShortcut));

        // 节点过滤后得到查询权限的cosmoIds
        List<ShortCut> origionNode = shortcutNodeMap.get(Boolean.FALSE);
        // 解析出权限的继承关系
        Map<String, List<String>> authMap = origionNode.stream()
                .collect(Collectors.toMap(ShortCut::getId, e -> StrUtil.split(e.getPathValue(), ";")));

        // 调整，过滤掉路径上的shortcutNode，并添加自身（有阻断）

        // 查权

        // 排序

        // 转换返回
        return null;
    }
}
