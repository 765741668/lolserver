package com.lol.demo.game;

import com.lol.demo.common.NettyMessage;
import com.lol.demo.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class GameClientChannelHandler extends SimpleChannelInboundHandler<NettyMessage> {
    private final Logger logger = LoggerFactory.getLogger(GameClientChannelHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage message = buildMessage(null);
        logger.info("Client send Game message to server : ---> {}({})", message, ctx.channel().remoteAddress());
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, NettyMessage msg)
            throws Exception {
        logger.info("Client received Game message from server : ---> {}({})", msg, ctx.channel().remoteAddress());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        logger.info("Client({} read Game message completed...", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.info("GameClientHandle({}) exit on exception,close client.",ctx.channel().remoteAddress());
        ctx.close();
    }

    private NettyMessage buildMessage(Object body) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();

        header.setType(MessageType.GAME_REQ.getValue());
        message.setHeader(header);
        message.setBody(body);

        return message;
    }

}
