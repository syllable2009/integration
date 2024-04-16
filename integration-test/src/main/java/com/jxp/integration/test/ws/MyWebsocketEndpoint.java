package com.jxp.integration.test.ws;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.BeforeHandshake;
import org.yeauty.annotation.OnBinary;
import org.yeauty.annotation.OnClose;
import org.yeauty.annotation.OnError;
import org.yeauty.annotation.OnEvent;
import org.yeauty.annotation.OnMessage;
import org.yeauty.annotation.OnOpen;
import org.yeauty.annotation.PathVariable;
import org.yeauty.annotation.RequestParam;
import org.yeauty.annotation.ServerEndpoint;
import org.yeauty.pojo.Session;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-16 17:23
 */
@Slf4j
@ServerEndpoint(path = "/ws/{arg}", host = "${ws.host}", port = "${ws.port}")
public class MyWebsocketEndpoint {

    //    @Resource
    //    WsService wsService;

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
            @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {

        session.setSubprotocols("stomp");
        if (!"ok".equals(req)) {
            System.out.println("Authentication failed!");
            session.close();
        }
    }


    private static void setCookie(HttpServletResponse response, String name, @Nullable String value,
            long maxAgeInSeconds) {

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(Math.toIntExact(maxAgeInSeconds));
        cookie.setPath(StrUtil.SLASH);
        cookie.setHttpOnly(Boolean.TRUE);
        cookie.setSecure(Boolean.TRUE);
        log.info("setCookie.cookie.name===>:{},cookie.value===>{},cookie.MaxAge===>{}",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge());
        response.addCookie(cookie);

    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam
            MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {

        // 判断重连
        System.out.println("new connection");
        System.out.println(req);

        String cookies = headers.get("Cookie");
        // 身份识别
        log.info("cookie:{}", cookies);
        String[] cookieList = cookies.split(";");

        String token = "";
        for (int i = 0; i < cookieList.length; i++) {
            String cookie = cookieList[i];
            String[] fields = cookie.trim().split("=");
            if (fields.length != 2) {
                continue;
            }
            if (fields[0].trim().equals("Constants.XX_TOKEN_COOKIE_KEY")) {
                token = fields[1].trim();
            }
        }

        // 登录的时候设置了cookie
        //        Map<String, String> session = new HashMap<>();
        //        session.put(UID_ATTR_NAME, uid);
        //        redis.getKsRedisSyncCommands().hmset("前缀:ddd:"+ sessionKey, session);
        // 现在取出来
        //        Map<String, String> sessionMap = pspRedis.getKsRedisSyncCommands().hgetall("前缀:ddd:" +sessionKey);
        //        userid = sessionMap.get("UID_ATTR_NAME");
        ChannelId id = session.channel().id();
        // 鉴权，绑定user和clientid


        // 解析 clientId
        String clientId = headers.get("clientId");
        if (StringUtils.isBlank(clientId)) {
            clientId = (String) reqMap.getFirst("clientId");
        }
        if (StringUtils.isBlank(clientId)) {
            log.error("find clientId is null");
            session.close();
            return;
        }
        session.setAttribute("clientId", clientId);
        //相同clientId 可以重连
        if (WsConnectInMemorytIndex.checkIfClientIdInInstance(clientId)) {
            log.warn("clientId={}出现重复", clientId);
        }

        // 验证登录
        String userId = "";
        WsConnectIdentityDto connectIdentityDto = new WsConnectIdentityDto();
        connectIdentityDto.setSession(session);
        connectIdentityDto.setUserId(userId);
        connectIdentityDto.setClientId(clientId);
        //维护连接信息
        //        WsMsgMaintainerService maintainer = SpringUtils.getBean(WsMsgMaintainerService.class);
        //        maintainer.onOpenSuccess(connectIdentityDto);
        WsConnectInMemorytIndex.onOpenSuccess(connectIdentityDto);
        log.info("clientId={},user={} open ws success", clientId, userId);

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        //        session.sendText("Hello Netty!");

        MessagePrefix prefix = MessagePrefix.of(message.charAt(0));
        if (null == prefix) {
            // 判断是否为可处理前缀，
        }
        switch (prefix) {
            case REQUEST_HEARTBEAT:
                handleHeartbeatEvent();
                break;
            case REQUEST_BUSINESS:
                break;
            default:
                break;

        }

        Object clientId = session.getAttribute("clientId");
        WsService.sendMessage(WsConnectInMemorytIndex.getClientInfo((String) clientId), "Hello Netty!");
        WsService.sendMessage(WsConnectInMemorytIndex.getClientInfo((String) clientId), "Hello Netty!2");

    }

    private void handleHeartbeatEvent() {
        // 刷新心跳时间
        // 响应心跳信息
    }



    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }
}
