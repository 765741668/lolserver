/******************************************************************************
 *
 * Module Name:  netty - HeartBeatRespHandle.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 13, 2016
 * Last Updated By: java
 * Last Updated Date: May 13, 2016
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
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(HeartBeatRespHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        logger.info("Server receive heart beat message from client : ---> {}({})", message, ctx.channel().localAddress());
//        if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.getValue()){
//            NettyMessage sendMsg = buildMessage();
//            logger.info("Server send heart beat message to client : ---> {}({})",sendMsg,ctx.channel().localAddress());
//        }else{
//            ctx.fireChannelRead(message);
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
