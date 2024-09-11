package com.jxp.customer;

import cn.hutool.core.util.StrUtil;

/**
 * 客服信息redis缓存key，按照人维度，支持多个应用号，所有消息号共计
 * @author jiaxiaopeng
 * Created on 2024-09-11 15:36
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
public class CustomerConstant {

    public static String USER_ID = "userId"; // 注意区分sessionKey
    public static String LAST_ALLOCATE_TIME = "lastAllocateTime"; // 上次分配时间
    public static String SATURATION = "saturation"; // 饱和度

    public static String getUserKey(String userId) {
        return StrUtil.format("kefuhao:user:{}", userId);
    }

    // 实时监控的其他数据
}
