package com.lol.demo.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class TestServerChannelHandler extends ChannelHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(TestServerChannelHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        logger.info("server get request from client.");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        logger.info("the server receive : {}", body);
        String currenTime = "Query_Time".equalsIgnoreCase(body) ? new Date().toString() : "null";
        ByteBuf resp = Unpooled.copiedBuffer(currenTime.getBytes());
        ctx.write(resp);
        logger.info("server respond done.");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        logger.info("server read Complete done.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        ctx.close();
        logger.info("server exit on exception . " + cause.getMessage());
    }

}