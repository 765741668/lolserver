/******************************************************************************
 *
 * Module Name:  netty.websocket - WebSocketServerHandler.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 31, 2016
 * Last Updated By: java
 * Last Updated Date: May 31, 2016
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
package com.lol.demo.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory
            .getLogger(WebSocketServerHandler.class.getName());
    private WebSocketServerHandshaker handshaker;

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (HttpHeaders.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加
        Global.group.add(ctx.channel());
        logger.info("客户端:{}与服务端连接开启", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        Global.group.remove(ctx.channel());
        logger.info("客户端:{}与服务端连接关闭", ctx.channel().id());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame) {
        // 是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
                    .retain());
            return;
        }
        // ping消息
        if (frame instanceof PingWebSocketFrame) {
            System.out.println("pong message : " + new PongWebSocketFrame(frame.content().retain()));
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 文本消息，NOT二进制
        if (!(frame instanceof TextWebSocketFrame)) {
            logger.info("文本消息，NOT二进制");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        String customer = ctx.channel().id().toString();
        String response = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            response = br.readLine();
            if (response == null || response.equalsIgnoreCase("exit")) {
                ctx.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();

        logger.info("服务端收到客户端:{}消息：{}",customer, request);

        TextWebSocketFrame tws = new TextWebSocketFrame("客户端ID["+ customer + "]：" + request+"(server msg)");
        // 群发
        Global.group.writeAndFlush(tws);
        logger.info("服务端群发消息:{} 到客户端:{}", request,ctx.channel().id().toString());
        // 返回【谁发的发给谁】
        // ctx.channel().writeAndFlush(tws);
         */
        TextWebSocketFrame tws = new TextWebSocketFrame("客户端ID[" + customer + "]：" + response);
        // 群发
        Global.group.write(tws);
        logger.info("服务端群发消息:{} 到客户端:{}", response, customer);

    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:3334/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}