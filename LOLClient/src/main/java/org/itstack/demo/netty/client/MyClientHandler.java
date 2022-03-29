package org.itstack.demo.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.itstack.demo.netty.future.SyncWriteFuture;
import org.itstack.demo.netty.future.SyncWriteMap;
import org.itstack.demo.netty.msg.Request;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Request> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request msg) throws Exception {
        String requestId = msg.getRequestId();
        SyncWriteFuture future = (SyncWriteFuture) SyncWriteMap.syncKey.get(requestId);
        if (future != null) {
            future.setResponse(msg);
        }
    }
}
