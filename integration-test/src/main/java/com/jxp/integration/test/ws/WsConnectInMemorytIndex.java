package com.jxp.integration.test.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yeauty.pojo.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-16 17:57
 */

@Slf4j
public class WsConnectInMemorytIndex {

    // 内存索引失效时间（连接心跳超时的时间，客户端默认是10秒一次心跳 ，）
    private static final int CLIENT_HEARTBEAT_TIMEOUT_MS = 60 * 1000;

    // 本机clientID对应的所有事项（包括关注的event列表，对应的用户，最后活跃时间等）
    private static final Map<String, ClientIdentity> CLIENT_ID_MAP = new ConcurrentHashMap<>();

    //每个event对应的所有client列表
    private static final Map<String, Map<String, Boolean>> EVENT_REVERSE_MAP =
            new ConcurrentHashMap<>();

    public static boolean checkIfClientIdInInstance(String clientId) {
        if (CLIENT_ID_MAP.containsKey(clientId)) {
            //当前节点不包含该客户端
            return true;
        }
        return false;
    }

    public static void onOpenSuccess(WsConnectIdentityDto connectIdentityDto) {

        //清除本机上之前的clientId
        removeClient(connectIdentityDto.getClientId());

        //广播其他节点删除指定clientId的session
        //        wsInputEventMessageService.broadcastKickOutEvent(connectIdentityDto.getClientId());

        //本机初始化
        WsConnectInMemorytIndex.buildIndexWhenOpenConnect(connectIdentityDto);

        // 发送链接事件
        //        wsEventListenerList.forEach(wsEventListener -> wsEventListener.onOpenSuccess(connectIdentityDto));

    }

    public static WsConnectIdentityDto getClientInfo(String clientId) {
        if (null == CLIENT_ID_MAP.get(clientId)) {
            return null;
        }
        return CLIENT_ID_MAP.get(clientId).getWsConnectIdentityDto();
    }

    private static void removeClient(String clientId) {
        WsConnectIdentityDto clientInfo = WsConnectInMemorytIndex.getClientInfo(clientId);
        if (clientInfo == null) {
            return;
        }
        Session session = clientInfo.getSession();
        if (session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                //ignore
                log.warn("close old session error", e);
            }
        }
        WsConnectInMemorytIndex.removeClientId(clientId);
    }

    /**
     * 内存索引中删除 clientId 下所有事件
     */
    public static void removeClientId(String clientId) {
        if (!CLIENT_ID_MAP.containsKey(clientId)) {
            return;
        }
        // 先从正向索引中找到所有的事件，并删除对应的反向索引
        Map<String, Boolean> events = CLIENT_ID_MAP.get(clientId).getEvents();
        for (String event : events.keySet()) {
            if (EVENT_REVERSE_MAP.containsKey(event)) {
                EVENT_REVERSE_MAP.get(event).remove(clientId);
                if (EVENT_REVERSE_MAP.get(event).size() == 0) {
                    EVENT_REVERSE_MAP.remove(event);
                }
            }
        }
        // 删除正向索引
        CLIENT_ID_MAP.remove(clientId);
    }

    public static void buildIndexWhenOpenConnect(WsConnectIdentityDto connectIdentityDto) {
        //初始化索引信息（并没有事件注册）
        tryReBuildMemoryIndex(connectIdentityDto);
        CLIENT_ID_MAP.putIfAbsent(connectIdentityDto.getClientId(), ClientIdentity.builder()
                .wsConnectIdentityDto(connectIdentityDto)
                .lastHeartbeatTimestamp(System.currentTimeMillis())
                .build());
        // 更新内存中的心跳时间
//        flushHeartbeatTime(connectIdentityDto.getClientId());
    }

    private static void flushHeartbeatTime(String currentClientId) {
        if (CLIENT_ID_MAP.containsKey(currentClientId)) {
            CLIENT_ID_MAP.get(currentClientId).setLastHeartbeatTimestamp(System.currentTimeMillis());
        }
    }


    /**
     * 检查 clientId 是否创建了内存索引，如果尚未创建则从 redis 中同步数据做创建（以防万一，在某个case下内存索引没有建立成功的时候，后续进行补救）
     */
    private static void tryReBuildMemoryIndex(WsConnectIdentityDto connectIdentityDto) {
        // 如果内存中已经存在，则不从 redis 中同步
        if (CLIENT_ID_MAP.containsKey(connectIdentityDto.getClientId())) {
            return;
        }
        // 获得 clientId 所有的事件
        //        RedisConnection redisConn = SpringUtils.getBean(RedisConnection.class);
        //        String key = PspWsConstants.getSubscriptionKey(connectIdentityDto.getClientId());
        //        //获取该client所有订阅的事件
        //        Map<String, String> allEvents = redisConn.hgetall(key);
        //        if (MapUtils.isEmpty(allEvents)) {
        //            return;
        //        }
        Map<String, Boolean> events = new ConcurrentHashMap<>();
        //        for (String event : allEvents.keySet()) {
        //            events.put(event, true);
        //            // 添加反向索引
        //            Map<String, Boolean> clientIds = EVENT_REVERSE_MAP.getOrDefault(event, new ConcurrentHashMap<>());
        //            clientIds.put(connectIdentityDto.getClientId(), true);
        //            EVENT_REVERSE_MAP.put(event, clientIds);
        //
        //        }
        // 添加正向索引
        CLIENT_ID_MAP.put(connectIdentityDto.getClientId(), ClientIdentity.builder()
                .wsConnectIdentityDto(connectIdentityDto)
                .events(events)
                .build());
    }
}
