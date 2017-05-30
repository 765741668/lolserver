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
package com.lol.demo.heartbeat.jbossmarshall;

import com.lol.demo.common.NettyMessage;
import com.lol.demo.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class HeartBeatReqHandler extends SimpleChannelInboundHandler {
    private final Logger logger = LoggerFactory.getLogger(HeartBeatReqHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().localAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()) {
            logger.info("Login Auth request is over, start to send Heart Beat request ... ");
        } else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.getValue()) {
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
        ctx.fireExceptionCaught(cause);
    }

}
