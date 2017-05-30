package com.lol.demo.encode.protobuf.lol;

import com.lol.LoginProtocol;
import com.lol.MatchProtocol;
import com.lol.PlayerProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import com.lol.demo.encode.protobuf.MessageUpProto;
import com.lol.demo.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class LOLClientMatchHandler extends SimpleChannelInboundHandler {

    private Logger logger = LoggerFactory.getLogger(LOLClientLoginHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("链接到服务器：[{}]",ctx.channel().remoteAddress().toString());
        MessageUpProto.LoginUpBody.Builder loginUpBody = MessageUpProto.LoginUpBody.newBuilder();
        loginUpBody.setAcount("yzh2");
        loginUpBody.setPassword("123456");

        logger.info("发送登陆信息： {}",loginUpBody.build().toString());
        MessageUpProto.MessageUp messageUpLogin = Utils.packgeUpData(Protocol.TYPE_LOGIN, 1, LoginProtocol.LOGIN_CREQ, loginUpBody.build());
        ctx.writeAndFlush(messageUpLogin);

        Thread.sleep(3000);

        logger.info("请求玩家角色上线...");
        MessageUpProto.MessageUp online = Utils.packgeUpData(Protocol.TYPE_PLYAER, 1,
                PlayerProtocol.ONLINE_CREQ, null);
        ctx.writeAndFlush(online);

        //TODO:1V1 3V3 5V5
        logger.info("请求寻找1V1匹配1...");
        MessageUpProto.MatchUpBody.Builder matchUpBody = MessageUpProto.MatchUpBody.newBuilder();
        matchUpBody.setTeamMax(3);
        MessageUpProto.MessageUp matchEnter = Utils.packgeUpData(Protocol.TYPE_MATCH, 1,
                MatchProtocol.ENTER_CREQ, matchUpBody.build());
        ctx.writeAndFlush(matchEnter);

        logger.info("请求寻找1V1匹配2...");
        MessageUpProto.MatchUpBody.Builder matchUpBody2 = MessageUpProto.MatchUpBody.newBuilder();
        matchUpBody2.setTeamMax(3);
        MessageUpProto.MessageUp matchEnter2 = Utils.packgeUpData(Protocol.TYPE_MATCH, 1,
                MatchProtocol.ENTER_CREQ, matchUpBody2.build());
        ctx.writeAndFlush(matchEnter2);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        MessageDownProto.MessageDown resp = (MessageDownProto.MessageDown) msg;
        if(resp.getHeader().getMsgType() == Protocol.TYPE_MATCH){
            switch (resp.getHeader().getCmd()){
                case MatchProtocol.MATCH_COMPLETED:
                    logger.info("匹配已找到。");
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("未知异常 from downstream : {}",cause.getMessage(),cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }

}