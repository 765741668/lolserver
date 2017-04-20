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
package com.lol.demo.loginauth.googleprotobuf;

import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoBody;
import com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoHeader;
import com.lol.demo.encode.protobuf.SubscribeReqProto.SubscribeReq;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(LoginAuthReqHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SubscribeReq message = createSubscribeReq();
        logger.info("Client send Login auth message to server : ---> " + message + "(" + ctx.channel().remoteAddress() + ")");
        ctx.writeAndFlush(message);
    }

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
        if (message != null && message.getRespCode().equals("0")) {
            logger.info("Client reveived Login auth message from server : ---> " + message + "(" + ctx.channel().remoteAddress() + ")");
            String loginResult = message.getRespCode();
            if (!loginResult.equals("0")) {
                ctx.close();
            } else {
                logger.info("Login is ok : " + message);
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

    private SubscribeReq createSubscribeReq() {
        ProtoHeader.Builder header = ProtoHeader.newBuilder();
        header.setSubReqID(1);
        header.setMsgType(ProtoHeader.MsgType.LOGIN_REQ);

        List<String> address = new ArrayList<String>();
        address.add("Nanjing");
        address.add("Beijing");
        address.add("Hangzhou");

        ProtoBody.Builder body = ProtoBody.newBuilder();
        body.setProductName("Netty Book");
        body.setUserName("leeka");
        body.addAllAddress(address);
        body.getMutableHeaderMap().put("head1", header.build());

        SubscribeReq.Builder builder = SubscribeReq.newBuilder();
        builder.setHeader(header);
        builder.setBody(body);

        return builder.build();
    }
}