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

import com.lol.demo.encode.protobuf.SubscribeReqProto.SubscribeReq;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp;
import com.lol.demo.encode.protobuf.SubscribeRespProto.SubscribeResp.MsgType;
import com.lol.demo.util.PropertiesExUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelHandlerAdapter {
    private static Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    private final Logger logger = LoggerFactory.getLogger(LoginAuthRespHandler.class);
    private String whiteList = PropertiesExUtil.getInstance()
            .getPropertiesValue("/spring/config/handle.properties", "handle.login.response.whiteList");

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq message = (SubscribeReq) msg;

        com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoHeader.MsgType requestMsgType =
                com.lol.demo.encode.protobuf.SubscribeReqProto.ProtoHeader.MsgType.LOGIN_REQ;

        if (message.getHeader() != null && message.getHeader().getMsgType() == requestMsgType) {
            logger.info("Server reveived Login auth message from Client : ---> {}({})", message, ctx.channel().localAddress());
            String nodeIndex = ctx.channel().localAddress().toString();
            SubscribeResp loginResp = null;

            if (nodeCheck.containsKey(nodeIndex)) {
                logger.warn("Already logined,ignore...");
                loginResp = buildResp(MsgType.LOGIN_DENY);
            } else {
                InetSocketAddress addr = (InetSocketAddress) ctx.channel().localAddress();
                String ip = addr.getAddress().getHostAddress();
                logger.info("ip: {}", ip);
                boolean isOK = false;
                if (whiteList.contains(ip)) {
                    isOK = true;
                }

                if (isOK) {
                    nodeCheck.put(nodeIndex, true);
                    loginResp = buildResp(MsgType.LOGIN_OK);
                } else {
                    loginResp = buildResp(MsgType.LOGIN_OUT_OF_WHITE_IP);
                }

                logger.info("The login response is : {}", loginResp);

            }

            ctx.writeAndFlush(loginResp);
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

    private SubscribeResp buildResp(MsgType subReq) {
        SubscribeResp.Builder builder = SubscribeResp.newBuilder();
        builder.setSubReqID(subReq.getNumber());
        builder.setRespCode("0");
        builder.setMsgType(MsgType.LOGIN_RESP);
        builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return builder.build();
    }


}
