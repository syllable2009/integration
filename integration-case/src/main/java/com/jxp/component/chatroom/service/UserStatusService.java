package com.jxp.component.chatroom.service;

/**
 * 管理用户的连接信息
 * 当用户量越来越大，必然需要增加服务器的数量，用户的连接被分散在不同的机器上。
 * 此时，就需要存储用户连接在哪台机器上。
 * @author jiaxiaopeng
 * Created on 2024-06-27 16:07
 */
public interface UserStatusService {
    /**
     * 用户上线，存储userId与机器id的关系
     *
     * @param userId
     * @param connectorId
     * @return 如果当前用户在线，则返回他连接的机器id，否则返回null
     */
    String online(String userId, String connectorId);

    /**
     * 用户下线
     *
     * @param userId
     */
    void offline(String userId);

    /**
     * 通过用户id查找他当前连接的机器id
     *
     * @param userId
     * @return
     */
    String getConnectorId(String userId);
}
