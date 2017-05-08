/******************************************************************************
 *
 * Module Name:  netty - HeartBeatReqHandle.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 12, 2016
 * Last Updated By: java
 * Last Updated Date: May 12, 2016
 * Description:
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
package com.lol.demo.heartbeat.googleprotobuf;

import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoHeader;
import com.lol.demo.encode.protobuf.SubscribeReqProto.SubscribeReq;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp.MsgType;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(HeartBeatReqHandler.class);
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeResp message = (SubscribeResp) msg;
        if (message != null && message.getMsgType() == MsgType.LOGIN_RESP) {
            logger.info("Login Auth request is over, start to send Heart Beat request ... ");
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
        } else if (message != null && message.getMsgType() == MsgType.HEARTBEAT_RESP) {
            logger.info("Client receive heart beat message from server : ---> {}({})", message, ctx.channel().localAddress());

        } else {
            ctx.fireChannelRead(message);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartBeatTask implements Runnable {

        private ChannelHandlerContext ctx;

        private HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            SubscribeReq message = buildMessage();
            logger.info("Client send heartBeat message to server : ---> {} ({}) ", message,ctx.channel().remoteAddress());
            ctx.writeAndFlush(message);
        }

        private SubscribeReq buildMessage() {
            ProtoHeader.Builder header = ProtoHeader.newBuilder();
            header.setMsgType(ProtoHeader.MsgType.HEARTBEAT_REQ);

            SubscribeReq.Builder builder = SubscribeReq.newBuilder();
            builder.setHeader(header);

            return builder.build();
        }

    }

}
