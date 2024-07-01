package com.jxp.component.chatroom.handle;

import org.springframework.stereotype.Component;

import com.jxp.component.chatroom.codec.AuthMessageContent;
import com.jxp.component.chatroom.codec.Invocation;
import com.jxp.component.chatroom.enums.MsgTypeEnum;
import com.jxp.component.chatroom.server.NettyChannelManager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-27 21:53
 */

@Slf4j
@Component
public class AuthHandle implements MsgHandle {

    @Override
    public String getType() {
        return MsgTypeEnum.Auth.getCode();
    }

    @Override
    public void execute(Channel channel, Invocation invocation) {
        log.info("[Server][AuthHandle][execute],invocation:{}", invocation);
        final AuthMessageContent content = JSONUtil.toBean(invocation.getContent(),
                AuthMessageContent.class);
        // 此处调用用户模块rpc接口来验证用户
        if (StrUtil.equals("jiaxiaopeng", content.getUserId())) {
            log.info("[Server][AuthHandle][认证成功],invocation:{}", invocation);
            final String userId = content.getUserId();
            NettyChannelManager.addUser(channel, userId);
        } else {
            log.info("[Server][AuthHandle][认证失败],invocation:{}", invocation);
            channel.close();
        }
    }
}
