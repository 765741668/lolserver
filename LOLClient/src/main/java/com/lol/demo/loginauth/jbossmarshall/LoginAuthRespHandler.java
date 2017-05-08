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
import com.lol.demo.enums.MessageType;
import com.lol.demo.game.Header;
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
            .getPropertiesValue("/env/spring/config/handle.properties", "handle.login.response.whiteList");

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
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.getValue()) {
            logger.info("Server reveived Login auth message from Client : ---> {}({})", message, ctx.channel().localAddress());
            String nodeIndex = ctx.channel().localAddress().toString();
            NettyMessage loginResp = null;

            if (nodeCheck.containsKey(nodeIndex)) {
                logger.warn("Already logined,ignore...");
                loginResp = buildMessage(MessageType.LOGIN_DENY.getDesc());
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
                    loginResp = buildMessage(MessageType.LOGIN_OK.getDesc());
                } else {
                    loginResp = buildMessage(MessageType.LOGIN_OUT_OF_WHITE_IP.getDesc());
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

    private NettyMessage buildMessage(String body) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.getValue());

        message.setHeader(header);
        message.setBody(body);
        return message;
    }


}
