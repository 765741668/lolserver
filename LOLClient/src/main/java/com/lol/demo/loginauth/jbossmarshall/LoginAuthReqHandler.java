/******************************************************************************
 *
 * Module Name:  netty - LoginAuthReqHandler.java
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
package com.lol.demo.loginauth.jbossmarshall;

import com.lol.demo.common.NettyMessage;
import com.lol.demo.encode.jbossmarshall.NettyClient;
import com.lol.demo.enums.MessageType;
import com.lol.demo.game.Header;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class LoginAuthReqHandler extends SimpleChannelInboundHandler {
    private final Logger logger = LoggerFactory.getLogger(LoginAuthReqHandler.class);
    NettyClient client;

    public LoginAuthReqHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage message = buildMessage();
        logger.info("Client send Login auth message to server : ---> {} ({})" + message,ctx.channel().remoteAddress());
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress localAddress = ctx.channel().localAddress();
        logger.info("{} is inactive .....", localAddress);
        super.channelInactive(ctx);
//        ctx.channel().eventLoop().schedule(()->client.connect(localAddress),10,TimeUnit.SECONDS);
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()) {
            logger.info("Client reveived Login auth message from server : ---> {} ({})" + message,ctx.channel().remoteAddress());
            String loginResult = (String) message.getBody();
            if (!loginResult.equals(MessageType.LOGIN_OK.getDesc())) {
                ctx.close();
            } else {
                logger.info("Login is ok : {}",message);
                ctx.fireChannelRead(message);
            }
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
        logger.info(cause.getMessage());
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildMessage() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getValue());
        message.setBody("It is a login auth request...");
        message.setHeader(header);
        return message;

    }
}