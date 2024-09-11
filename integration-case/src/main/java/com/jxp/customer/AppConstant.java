package com.jxp.customer;

import cn.hutool.core.util.StrUtil;

/**
 * 应用级缓存数据
 * @author jiaxiaopeng
 * Created on 2024-09-11 15:36
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AppConstant {

    public static String APP_ID = "appId"; // 注意区分sessionKey
    public static String ALLOCATE_QUEUE = "allocateQueue"; // 分配队列，全量客服，按照添加顺序，如果有变更需要刷新
    public static String ALLOCATE_INDEX = "0"; // 分配索引

    public static String getAppKey(String appId) {
        return StrUtil.format("kefuhao:app:{}", appId);
    }
    // 实时监控的其他数据
}
