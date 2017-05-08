/******************************************************************************
 *
 * Module Name:  netty.http - HttpXmlServerHandle.java
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

import com.lol.jibx.shiporderv1.CTshiporder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpXmlServerHandle extends ChannelHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(HttpXmlServerHandle.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("chennel is inactive.");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpXmlRequest request = (HttpXmlRequest) msg;
        CTshiporder order = (CTshiporder) request.getBody();
        logger.info("Server reveived request from Client: ---> {}({})", order, ctx.channel().remoteAddress());
        ChannelFuture cf = ctx.writeAndFlush(new HttpXmlResponse(null, order));
        //send response msg,over than close http connection
        if (HttpHeaderUtil.isKeepAlive(request.getRequest())) {
            cf.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> arg0) throws Exception {
                    ctx.close();
                }

            });
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        boolean isActive = ctx.channel().isActive();
        logger.info("chnnel is active : {}", isActive);
        if (isActive) {
            sentError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        } else {
            ctx.close();
        }
    }

    private void sentError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, Unpooled.copiedBuffer("server fail...." + status.toString() + "\t\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}