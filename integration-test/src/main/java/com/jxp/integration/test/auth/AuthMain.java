package com.jxp.integration.test.auth;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import com.jxp.integration.test.auth.bean.Cosmo;
import com.jxp.integration.test.auth.bean.CosmoEntityValue;
import com.jxp.integration.test.auth.bean.ShortCut;
import com.jxp.integration.test.auth.service.ApiService;
import com.jxp.integration.test.auth.service.CosmoEntityValueService;
import com.jxp.integration.test.auth.service.CosmoService;
import com.jxp.integration.test.auth.service.ShortCutService;
import com.jxp.integration.test.auth.service.impl.ApiServiceImpl;
import com.jxp.integration.test.auth.service.impl.CosmoEntityValueServiceImpl;
import com.jxp.integration.test.auth.service.impl.CosmoServiceImpl;
import com.jxp.integration.test.auth.service.impl.ShortCutServiceImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-07-11 11:44
 * 如何设计权限缓存：Key：docId，Value Map<groupId,Integer>
 */

@Slf4j
public class AuthMain {

    private static CosmoEntityValueService cosmoEntityValueService = new CosmoEntityValueServiceImpl();
    private static ShortCutService shortCutService = new ShortCutServiceImpl();
    private static CosmoService cosmoService = new CosmoServiceImpl();
    private static ApiService apiService = new ApiServiceImpl();

    public static void main(String[] args) {
        long viewId = stringToViewId("VPYEDr9b8Jk0");
        log.info("viewId:{}", viewId);
    }


    public static void create() {
        // 创建一个节点
        // 是否创建一个cosmo
    }

    public static long stringToViewId(String v) {
        if (StrUtil.isBlank(v)) {
            return 0;
        }
        return v.charAt(0) == 'V' ? Longs.fromByteArray(java.util.Base64.getUrlDecoder().decode(v.substring(1)))
                                  : NumberUtils.toLong(v);
    }

    private ShortCut getOriginShortcutNode(String shortcutId) {
        ShortCut sc = shortCutService.getById(shortcutId);
        if (StrUtil.equals("shortcut", sc.getNodeType())) {
            sc = shortCutService.getById(sc.getOriginShortCutId());
        }
        return sc;
    }

    public List<ShortCut> getTreeList(String treeId) {
        List<ShortCut> treeList = shortCutService.getByTreeId(treeId);
        // todo 大节点判断，如果超过1w大节点
        return treeList;
    }

    public Map<String, String> calAuthByDocId(String userId, String docId) {
        Cosmo cosmo = cosmoService.getById(docId);
        String firstShortcut = cosmo.getFirstShortcut();
        calAuthByShortcutId(userId, firstShortcut);
        return null;
    }

    // 计算权限-内存计算查询权限向上查询，赋值权限向下查询
    public Map<String, Map<String, Integer>> calAuthByShortcutId(String userId, String shortcutId) {
        // 获取原始节点
        ShortCut originShortcutNode = getOriginShortcutNode(shortcutId);
        // 批量获取nodes
        List<ShortCut> treeList = getTreeList(originShortcutNode.getTreeId());
        // 向上遍历,防止死循环出现
        List<String> cosmoIds = upLoopAncestors(originShortcutNode, treeList);
        // 获取权限
        List<CosmoEntityValue> valueList = cosmoEntityValueService.getValueList(cosmoIds, null);
        // db中应该保证 一个entityId 对cosmoId 的记录最多一个invalidType一个

        // 分组统计，阻断权限表示在当前层，按照更新时间排序精确到6位
        // 阻断记录
        Map<String, Map<Long, CosmoEntityValue>> blockMap = valueList.stream()
                .filter(e -> 0 == e.getInvalidType())
                .collect(Collectors.groupingBy(CosmoEntityValue::getCosmoId,
                        Collectors.toMap(CosmoEntityValue::getEntityId, Function.identity(), (f, s) -> s)));
        // 继承记录
        Map<String, Map<Long, CosmoEntityValue>> inheritMap = valueList.stream()
                .filter(e -> 0 == e.getInvalidType())
                .collect(Collectors.groupingBy(CosmoEntityValue::getCosmoId,
                        Collectors.toMap(CosmoEntityValue::getEntityId, Function.identity(), (f, s) -> s)));


        //        Map<Long, CosmoEntityValue> blockMap = Maps.newHashMap();
        cosmoIds.forEach(e -> {
            Map<Long, CosmoEntityValue> nodeMap = inheritMap.get(e);
            Map<Long, CosmoEntityValue> block = blockMap.get(e);

            //            if (null != nodeValueMap) {
            //                // Group1 list
            //                nodeValueMap.forEach((o, v) -> {
            //                    CosmoEntityValue max = v.stream()
            //                            .max(Comparator.comparing(CosmoEntityValue::getUpdateTime))
            //                            .get();
            //
            //                });
            //            }
        });


        // 特殊规则处理：比如创建人，知识库的owner
        // 权限返回
//        valueMap.forEach((k, v) -> {
//            v.forEach((e, c) -> {
//
//            });
//        });
        return null;
    }

    // 向上遍历到祖宗,过滤掉shortcut节点
    public List<String> upLoopAncestors(ShortCut originShortcutNode, List<ShortCut> treeList) {
        Map<String, ShortCut> treeMap = treeList.stream()
                .collect(Collectors.toMap(ShortCut::getId, Function.identity()));
        // 父节点有且只有一个
        ShortCut sc = originShortcutNode;
        // ids用来防止死循环
        Set<String> ids = Sets.newHashSet();
        List<String> ancestors = Lists.newArrayList();
        while (sc != null) {
            if (ids.contains(sc.getId())) {
                log.error("tree has circle loop,originShortcutNode:{}", originShortcutNode);
                break;
            } else {
                ids.add(sc.getId());
            }

            if (!StrUtil.equals("origin", sc.getNodeType())) {
                ancestors.add(sc.getCosmoId());
            }
            sc = treeMap.get(originShortcutNode.getParentId());
        }
        return ancestors;
    }

    // 向下遍历到子孙，返回有序Map
    public void downLoopdescendants(ShortCut originShortcutNode, List<ShortCut> treeList) {
        TreeMap<Comparable, Object> comparableObjectTreeMap = Maps.newTreeMap();

        Map<String, List<ShortCut>> treeMap =
                treeList.stream()
                        .collect(Collectors.groupingBy(ShortCut::getParentId));

        List<ShortCut> shortCuts = treeMap.get(originShortcutNode.getId());

    }

    // 带缓存的权限获取
    public Map<String, Map<String, Integer>> cachedAuthByDocId() {
        return null;
    }
}
