package com.lol.demo.game;

import com.lol.demo.common.NettyMessage;
import com.lol.demo.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class GameServerChannelHandler extends SimpleChannelInboundHandler {
    private static Monster monster;
    private static Integer monster_damage_fx;
    private static Double percentage;
    private final Logger logger = LoggerFactory.getLogger(GameServerChannelHandler.class);

    public GameServerChannelHandler() {
        monster = new Monster();
        monster.setAttack(500);
        monster.setDefense(200);
        monster.setLive(10000);
        monster.setDeath(false);

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
//        percentage = Integer.valueOf(Utils.get0_1PercentageArea(2,0.8,1));
//        percentage = Utils.get0_1PercentageArea(2,0.8,1);
//        hero_damage_fx = (hero.getAttack()-monster.getAttack()-monster.getDefense()) * percentage;

        NettyMessage message = (NettyMessage) msg;
        message = buildMessage(message.getBody());
        logger.info("Server receive Game message from Clien : ---> {}({})", message, ctx.channel().localAddress());
        ctx.writeAndFlush(message);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.error("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        logger.info("Server ({}) read Game message Completed...", ctx.channel().localAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("GameServer Handle({}) exit on exception,close server : {}", ctx.channel().localAddress(), cause.getMessage(), cause);
        ctx.close();
    }

    private NettyMessage buildMessage(Object body) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();

        header.setType(MessageType.GAME_RESP.getValue());
        message.setHeader(header);
        message.setBody(body);

        return message;
    }

}
