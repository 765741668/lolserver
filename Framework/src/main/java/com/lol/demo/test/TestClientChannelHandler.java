package com.lol.demo.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClientChannelHandler extends ChannelHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(TestClientChannelHandler.class);
    private ByteBuf msg;

    public TestClientChannelHandler() {
        byte[] req = "Query_Time".getBytes();
        msg = Unpooled.buffer(req.length);
        msg.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client sended msg to server.");
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        logger.info("Client get respond from server.");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        logger.info("get server time : {}", body);
        logger.info("client read done.");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        logger.info("client reveive server respond and process done.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        logger.info("Client exit on exception.");
    }

}