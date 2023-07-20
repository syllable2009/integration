package com.jxp.integration.test.auth.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.integration.test.auth.bean.Cosmo;
import com.jxp.integration.test.auth.bean.CosmoEntityValue;
import com.jxp.integration.test.auth.bean.ShortCut;
import com.jxp.integration.test.auth.bean.ShortCutTreeNode;
import com.jxp.integration.test.auth.service.ApiService;
import com.jxp.integration.test.auth.service.CosmoEntityValueService;
import com.jxp.integration.test.auth.service.CosmoService;
import com.jxp.integration.test.auth.service.ShortCutService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-12 20:31
 */

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    private static CosmoEntityValueService cosmoEntityValueService = new CosmoEntityValueServiceImpl();
    private static ShortCutService shortCutService = new ShortCutServiceImpl();
    private static CosmoService cosmoService = new CosmoServiceImpl();
    private static ApiService apiService = new ApiServiceImpl();

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
        // 节点类型直接nodeType
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

    @Override
    public Map<String, List<CosmoEntityValue>> getAllAuthValues(Collection<String> cosmoIds,
            Collection<String> entityIds) {
        return null;
    }

    @Override
    public Map<String, CosmoEntityValue> getHighAuth(Collection<String> cosmoIds, String userId) {
        // 管理员和匿名用户的判断
        // 文档的继承性判断
        // 数据量很多的话，分批查询，过滤掉失效记录汇总
        List<CosmoEntityValue> valueList = cosmoEntityValueService.getValueList(cosmoIds, null);
        Map<String, Map<Long, List<CosmoEntityValue>>> cosmoEntityMap = valueList.stream()
                .collect(Collectors.groupingBy(CosmoEntityValue::getCosmoId,
                        Collectors.groupingBy(CosmoEntityValue::getEntityId)));

        //
        cosmoEntityMap.forEach((k, v) -> {
            v.forEach((f, s) -> {

            });
        });

        // 继承权限的接收存储
        Map<String, List<String>> docInheritMap = Maps.newHashMap();
        // init,把自己加上是需要查询挂在自身的阻断节点和权限节点
        cosmoIds.forEach((String e) -> {
            docInheritMap.put(e, Lists.newArrayList(e));
        });

        return null;
    }

    // 以当前节点为始获取所有子节点，包含删除的和回收站的
    private Map<String, ShortCutTreeNode> getSubPaths(Collection<String> shortcutIds,
            Function<ShortCut, Boolean> filter) {
        Map<String, ShortCutTreeNode> retMap = Maps.newHashMap();
        // 辅助赋值map
        Map<String, ShortCutTreeNode> inheritMap = Maps.newHashMap();
        List<ShortCut> rootList = shortCutService.getByIds(shortcutIds);
        rootList.stream()
                .filter(e -> filter.apply(e))
                .forEach(e -> {
                    ShortCutTreeNode node = ShortCutTreeNode.builder()
                            .id(e.getId())
                            .weight(e.getWeight())
                            .children(Lists.newArrayList())
                            .build();
                    inheritMap.put(e.getId(), node);
                    retMap.put(e.getId(), node);
                });
        Collection<String> iterator = retMap.keySet();
        // 如何防止递归死循环呢？
        iterator.remove(inheritMap.keySet());
        while (CollUtil.isNotEmpty(iterator)) {
            List<ShortCut> queryShortCutList = shortCutService.getByParentIds(shortcutIds);
            queryShortCutList
                    .stream()
                    .filter(e -> filter.apply(e))
                    .forEach(e -> {
                        ShortCutTreeNode node = ShortCutTreeNode.builder()
                                .id(e.getId())
                                .weight(e.getWeight())
                                .children(Lists.newArrayList())
                                .build();
                        inheritMap.put(e.getId(), node);
                        ShortCutTreeNode pNode = inheritMap.get(e.getParentId());
                        if (pNode != null) {
                            pNode.getChildren().add(node);
                        }
                    });
            iterator = queryShortCutList.stream()
                    .filter(e -> filter.apply(e))
                    .map(ShortCut::getId)
                    .collect(Collectors.toList());
            iterator.remove(inheritMap.keySet());
        }
        return retMap;
    }

    // 以当前节点为始获取祖辈节点
    // 如果中间节点删除了，是过滤掉节点还是直接删除？删除
    private Map<String, List<String>> getParentPaths(Collection<String> shortcutIds,
            Function<ShortCut, Boolean> filter) {

        Map<String, List<String>> retMap = Maps.newHashMap();
        // 辅助赋值map
        Map<String, List<String>> inheritMap = Maps.newHashMap();

        List<ShortCut> rootList = shortCutService.getByIds(shortcutIds);
        rootList.stream()
                .filter(e -> filter.apply(e))
                .forEach(e -> {
                    retMap.put(e.getId(), Lists.newArrayList(e.getId()));
                    inheritMap.put(e.getId(), retMap.get(e.getId()));
                });

        List<String> iterator = rootList.stream()
                .filter(e -> filter.apply(e))
                .map(ShortCut::getParentId)
                .collect(Collectors.toList());

        // 如何防止递归死循环呢？
        iterator.removeAll(inheritMap.keySet());
        while (CollUtil.isNotEmpty(iterator)) {
            List<ShortCut> parentList = shortCutService.getByIds(iterator);
            parentList.stream()
                    .filter(e -> filter.apply(e))
                    .forEach(e -> {
                        List<String> pList = inheritMap.get(e.getParentId());
                        if (pList != null) {
                            pList.add(e.getId());
                            inheritMap.put(e.getId(), pList);
                        }
                    });
            iterator = parentList.stream()
                    .filter(e -> filter.apply(e))
                    .map(ShortCut::getParentId)
                    .collect(Collectors.toList());
        }
        return retMap;
    }
}
