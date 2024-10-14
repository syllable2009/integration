package com.jxp.customer.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jxp.customer.RedisConstant;
import com.jxp.customer.service.CustomerService;
import com.jxp.customer.util.Jedis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-09-02 17:37
 */
@Slf4j
@Service
@SuppressWarnings("checkstyle:MagicNumber")
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private Jedis jedis;

    @Override
    public boolean manageSession(String appId, String askId, String messageKey, Long timeStamp) {
        if (null == timeStamp) {
            timeStamp = System.currentTimeMillis();
        }
        String sessionKey = RedisConstant.getSessionKey(appId, askId);
        final List<String> hmget = jedis.get().hmget(sessionKey, RedisConstant.SESSION_STATE,
                RedisConstant.SESSION_LAST_TIMESTAMP, RedisConstant.SESSION_SID);
        // 设置会话id
        // 获取当前session的状态
        String lastSendTimestampStr = hmget.get(0);
        Long lastSendTimestamp = StrUtil.isNotBlank(lastSendTimestampStr) ? Long.parseLong(lastSendTimestampStr) : null;
        if (null == lastSendTimestamp) {
            // 没有session，创建新会话
            // redis分布式加锁处理,分布式锁过期时间10s
            String res = jedis.get().set("lock:" + sessionKey, "1", "NX", "EX",
                    10);
            if (StrUtil.equals("OK", res)) {
                // 加锁完再去查询一遍防止并发
//                if (jedis.get().){
//                    createNewSession(sessionKey, appId, askId, messageKey, timeStamp);
//                    return true;
//                }else {
//                    ThreadUtil.sleep(500);
//                    updateCurrentSession(sessionKey, messageKey, timeStamp);
//                }
            } else {
                ThreadUtil.sleep(500);
                updateCurrentSession(sessionKey, messageKey, timeStamp);
            }
        } else {
            final String sessionStateStr = hmget.get(1);
            // 根据session状态从配置获取session的有效期（毫秒）
            Long sessionActiveStamp = 60_000L;
            if (timeStamp - lastSendTimestamp > sessionActiveStamp) {
                // 会话过期了，需要更新到db,并且创建新会话
                String res = jedis.get().set("lock:" + sessionKey, "1", "NX", "EX",
                        10);
                if (StrUtil.equals("OK", res)) {
                    // 再查询一遍防止并发
                    endLastSession(sessionKey);
                    createNewSession(sessionKey, appId, askId, messageKey, timeStamp);
                    return true;
                } else {
                    ThreadUtil.sleep(500);
                    updateCurrentSession(sessionKey, messageKey, timeStamp);
                }
            } else {
                // 没有过期，缓存数据即可
                updateCurrentSession(sessionKey, messageKey, timeStamp);
            }
        }
        return false;
    }

    @Override
    public String distributeCustomer(String distributeCustomerStrategy, List<String> customerList) {
        // 每一个客服维护自己的信息，上线添加，下线删除
        // hashkey：hotline:cs:uid
//        SATURATION(0, "按客服饱和度分配"), 当前饱和情况
        // 工作量，已经量了就不会分配了
//                LEAST_RECENTLY_ALLOCATION(1, "优先分配给最久未分配客服"),
//                FIXED_ORDER_COMPETITION(2, "按固定顺序分配"),
//          LAST_ALLOCATE  上次分配
        String allocateCustomer = null;
        switch (distributeCustomerStrategy) {
            case "LAST_ALLOCATE":
                // 查找上次分配，如果没有怎么办？
                break;
            case "SATURATION":
                // 饱和度计算，到达上线
                allocateCustomer = allocateBySaturation("appId", customerList);
                break;
            case "LEAST_RECENTLY_ALLOCATION":
                break;
            case "FIXED_ORDER_COMPETITION":
                break;
            default:
                break;
        }
        // 执行分配，如果cas执行失败，则尝试重新分配N次，如果分配失败，则后续进入留言等过程
        return null;
    }

    // 按照饱和度排序
    private String allocateBySaturation(String appId, List<String> customerList) {
        // 查询应用的在线客服
        List<String> onlineCustome = Lists.newArrayList();
        if (CollUtil.isEmpty(onlineCustome)) {
            return null;
        }
        // 取交集
        final Collection<String> intersection = CollUtil.intersection(onlineCustome, customerList);
        // 查询在线的工作量，从低到高排序

        // cas处理

        return "";
    }



    private void updateCurrentSession(String sessionKey, String messageKey,
            Long lastSendTimestamp) {
        if (null == lastSendTimestamp) {
            lastSendTimestamp = System.currentTimeMillis();
        }
        Map<String, String> sessionMap = Maps.newHashMap();
        String startTimeStr = String.valueOf(lastSendTimestamp);
        sessionMap.put(RedisConstant.SESSION_LAST_TIMESTAMP, startTimeStr);
        sessionMap.put(RedisConstant.SESSION_END_KEY, messageKey);
        jedis.get().hmset(sessionKey, sessionMap);
    }

    private void createNewSession(String sessionKey, String botId, String askId,
            String messageKey,
            Long lastSendTimestamp) {
        if (null == lastSendTimestamp) {
            lastSendTimestamp = System.currentTimeMillis();
        }
        Map<String, String> sessionMap = Maps.newHashMap();
        String startTimeStr = String.valueOf(lastSendTimestamp);
        final String sid = IdUtil.fastSimpleUUID();
        sessionMap.put(RedisConstant.SESSION_SID, sid);
        sessionMap.put(RedisConstant.SESSION_LAST_TIMESTAMP, startTimeStr);
        sessionMap.put(RedisConstant.SESSION_STATE, "robot");
        sessionMap.put(RedisConstant.SESSION_KWAI_USER, askId);
        sessionMap.put(RedisConstant.SESSION_KWAI_BOT, botId);
        sessionMap.put(RedisConstant.SESSION_START_TIME, startTimeStr);
        sessionMap.put(RedisConstant.SESSION_START_KEY, messageKey);
        sessionMap.put(RedisConstant.SESSION_END_KEY, messageKey);
        jedis.get().hmset(sessionKey, sessionMap);
        // dbsave insert
    }

    private void endLastSession(String sessionKey) {
        final Map<String, String> sessionMap = jedis.get().hgetAll(sessionKey);
        final String sid = sessionMap.get(RedisConstant.SESSION_SID);
        log.info("endLastSession,sid:{},sessionMap:{}", sid, sessionMap);
        // 更新数据库，根据sid
        boolean update = true;
        if (update) {
            jedis.get().del(sessionKey);
        }
    }
}
