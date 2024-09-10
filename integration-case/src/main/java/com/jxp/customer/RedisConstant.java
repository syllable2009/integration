package com.jxp.customer;

import cn.hutool.core.util.StrUtil;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:40
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
public class RedisConstant {
    // session相关信息

    public static String SESSION_SID = "sid"; // 注意区分sessionKey
    public static String SESSION_APP_KEY = "appKey"; // 消息号标识
    public static String SESSION_STATE = "state"; // 注意区分sessionKey
    public static String SESSION_SERVICE_GROUP_ID = "serviceGroupId";
    public static String SESSION_CHAT_GROUP_ID = "chatGroupId";
    public static String SESSION_LAST_TIMESTAMP = "lastSendTimestamp"; // 根据配置实时计算session的有效期
    public static String SESSION_END_KEY = "endMsgKey";
    public static String SESSION_START_KEY = "startMsgKey";
    public static String SESSION_KWAI_USER = "kwaiUserId";
    public static String SESSION_KWAI_BOT = "kwaiBotId";
    public static String SESSION_USER_NAME = "username";
    public static String SESSION_START_TIME = "startTime";
    public static String SESSION_END_TIME = "endTime";
    public static String SESSION_HELLO_TIME = "helloTime"; // 打招呼时间，看是否和session独立
    // 队列相关信息

    // 其他
    public static String APP_HELLO_TIME = "helloTime"; // 打招呼时间，看是否和session独立


    public static String getSessionKey(String kwaiBotId, String userId) {
        return StrUtil.format("kefuhao:session:{}:{}", kwaiBotId, userId);
    }
}
