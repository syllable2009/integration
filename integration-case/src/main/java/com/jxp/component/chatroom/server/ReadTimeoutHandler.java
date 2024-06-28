package com.jxp.component.chatroom.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * @author jiaxiaopeng
 * Created on 2024-06-28 10:19
 */
public class ReadTimeoutHandler extends IdleStateHandler {

    private boolean closed;

    public ReadTimeoutHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        super.channelIdle(ctx, evt);
        assert evt.state() == IdleState.READER_IDLE;
        readTimeOut(ctx);
    }

    private void readTimeOut(ChannelHandlerContext ctx) {
        if (!closed) {
            ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
            ctx.close();
            closed = true;
        }
    }
}
