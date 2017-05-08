package com.lol.demo.encode.protobuf.lol;

import com.lol.LoginProtocol;
import com.lol.PlayerProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import com.lol.demo.encode.protobuf.MessageUpProto;
import com.lol.demo.util.Utils;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class LOLClientPlayerHandler extends ChannelHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(LOLClientLoginHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("链接到服务器：[{}]",ctx.channel().remoteAddress().toString());
        MessageUpProto.LoginUpBody.Builder loginUpBody = MessageUpProto.LoginUpBody.newBuilder();
        loginUpBody.setAcount("yzh2");
        loginUpBody.setPassword("123456");

        //登陆
        logger.info("发送登陆信息： {}",loginUpBody.build().toString());
        MessageUpProto.MessageUp messageUpLogin = Utils.packgeUpData(Protocol.TYPE_LOGIN, 1, LoginProtocol.LOGIN_CREQ, loginUpBody.build());
        ctx.writeAndFlush(messageUpLogin);

        Thread.sleep(3000);

        MessageUpProto.PlayerUpBody.Builder playerCreate = MessageUpProto.PlayerUpBody.newBuilder();
        playerCreate.setNickName("玩家角色-大蛇2");

        logger.info("发送创建新玩家角色信息: {}",playerCreate.getNickName());
        MessageUpProto.MessageUp messageUp = Utils.packgeUpData(Protocol.TYPE_PLYAER, 1,
                PlayerProtocol.CREATE_CREQ, playerCreate.build());
        ctx.writeAndFlush(messageUp);

        Thread.sleep(3000);

        logger.info("请求获取玩家角色信息。。。");
        MessageUpProto.MessageUp info = Utils.packgeUpData(Protocol.TYPE_PLYAER, 1,
                PlayerProtocol.INFO_CREQ, null);
        ctx.writeAndFlush(info);

        Thread.sleep(3000);

        logger.info("请求玩家角色上线...");
        MessageUpProto.MessageUp online = Utils.packgeUpData(Protocol.TYPE_PLYAER, 1,
                PlayerProtocol.ONLINE_CREQ, null);
        ctx.writeAndFlush(online);

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
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        MessageDownProto.MessageDown resp = (MessageDownProto.MessageDown) msg;
        String respStr;
        if(resp.getHeader().getMsgType() == Protocol.TYPE_PLYAER){
            switch (resp.getHeader().getCmd()){
                case PlayerProtocol.CREATE_SRES:
                    switch (resp.getBody().getPlayer().getCreate()){
                        case -1:
                            respStr = "帐号未登陆，创建新玩家角色失败";
                            break;
                        case -2:
                            respStr = "当前帐号已经拥有该玩家角色";
                            break;
                        default:
                            respStr = "创建新玩家角色成功";
                            break;
                    }
                    logger.info("收到服务器创建新玩家角色响应: {}",respStr);
                    break;
                case PlayerProtocol.INFO_SRES:
                    if(resp.getBody().getPlayer().getPlayerModel() == null){
                        logger.info("收到服务器获取玩家角色信息响应: 帐号未否登陆,获取失败");
                    }else{
                        logger.info("收到服务器获取玩家角色信息响应: {}",resp.getBody().getPlayer().getPlayerModel().getNickName());
                    }
                    break;
                case PlayerProtocol.ONLINE_SRES:
                    if(resp.getBody().getPlayer().getPlayerModel() == null){
                        logger.info("收到服务器玩家角色上线响应: {}","玩家角色不存在,无法获取信息，上线失败");
                    }else{
                        logger.info("收到服务器玩家角色上线响应: {}",resp.getBody().getPlayer().getPlayerModel().getNickName());
                    }
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