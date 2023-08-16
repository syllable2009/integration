package com.jxp.integration.test.ws;

import org.springframework.stereotype.Service;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-16 19:53
 */

@Slf4j
@Service
public class WsService {


    public static void sendMessage(WsConnectIdentityDto connectIdentityDto, String message) {
        try {
            //            synchronized (connectIdentityDto.getSession()) {   }
            if (connectIdentityDto.getSession().isOpen()) {
                Thread.sleep(500);
//                ChannelFuture cf = connectIdentityDto.getSession().channel().writeAndFlush(new TextWebSocketFrame(message));
                ChannelFuture cf = connectIdentityDto.getSession().sendText(message);

                //添加ChannelFutureListener以便在写操作完成后接收通知
                cf.addListener((ChannelFutureListener) future -> {
                    //写操作完成，并没有错误发生
                    if (future.isSuccess()) {
                        System.out.println("successful");
                    } else {
                        //记录错误
                        System.out.println("error");
                        future.cause().printStackTrace();
                    }
                });
            } else {
                //                ClearUnAliveConnectService connectService = SpringUtils.getBean
                //                (ClearUnAliveConnectService.class);
                //                connectService.addCheckConnect(connectIdentityDto);
            }

        } catch (Exception e) {
            log.error("sendMessage error", e);
        }
    }
}
