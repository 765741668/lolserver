/******************************************************************************
 *
 * Module Name:  netty.http - HttpXmlClientHandle.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 26, 2016
 * Last Updated By: java
 * Last Updated Date: May 26, 2016
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
package com.lol.demo.http.xml;

import com.lol.demo.jibx.ObjectFactory;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpXmlClientHandle extends ChannelHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(HttpXmlClientHandle.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpXmlRequest request = new HttpXmlRequest(null, ObjectFactory.createCTshiporder("orderid01"));
        logger.info("Client send request to server : ---> {}({})", request, ctx.channel().remoteAddress());
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("chennel is inactive.");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpXmlResponse response = (HttpXmlResponse) msg;
        logger.info("Client reveived response of http header from server : ---> {}({})",
                response.getHttpResponse().headers().names(), ctx.channel().remoteAddress());
        logger.info("Client reveived response of http header from server : ---> {}({})",
                response, ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }
}