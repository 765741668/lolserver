package com.lol.demo.encode.protobuf.lol;

import com.lol.LoginProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import com.lol.demo.encode.protobuf.MessageUpProto;
import com.lol.demo.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class LOLClientLoginHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(LOLClientLoginHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("链接到服务器：[{}]",ctx.channel().remoteAddress().toString());
        MessageUpProto.LoginUpBody.Builder loginUpBody = MessageUpProto.LoginUpBody.newBuilder();
        loginUpBody.setAcount("yzh");
        loginUpBody.setPassword("123456");

        //注册新账户
        logger.info("发送注册新账户信息: {}",loginUpBody.build().toString());
        MessageUpProto.MessageUp messageUp = Utils.packgeUpData(Protocol.TYPE_LOGIN, 1, LoginProtocol.REG_CREQ, loginUpBody.build());
        ctx.writeAndFlush(messageUp);

//        Thread.sleep(3000);

        //登陆
        logger.info("发送登陆信息： {}",loginUpBody.build().toString());
        MessageUpProto.MessageUp messageUpLogin = Utils.packgeUpData(Protocol.TYPE_LOGIN, 1, LoginProtocol.LOGIN_CREQ, loginUpBody.build());
        ctx.writeAndFlush(messageUpLogin);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);

        ctx.channel().eventLoop().schedule(() -> {
            LOLNettyClient.doConnect(remoteAddress);
        }, 5, TimeUnit.SECONDS);

        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("消息读取完毕。。。");
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        MessageDownProto.MessageDown resp = (MessageDownProto.MessageDown) msg;
        String respStr = "";
        switch (resp.getHeader().getCmd()){
            case LoginProtocol.REG_SRES:
                switch (resp.getBody().getLogin().getResult()){
                    case 1:
                        respStr = "账号已存在，创建新账号失败";
                        break;
                    default:
                        respStr = "创建新账号成功";
                        break;
                }
                logger.info("收到服务器注册响应: {}",respStr);
                break;
            case LoginProtocol.LOGIN_SRES:
                switch (resp.getBody().getLogin().getResult()){
                    case -4:
                        respStr = "账号或密码为空，输入不合法";
                        break;
                    case -1:
                        respStr = "账号不存在，拒绝登陆";
                        break;
                    case -2:
                        respStr = "当前账号已经在线";
                        break;
                    case -3:
                        respStr = "账号或密码不正确";
                        break;
                    default:
                        respStr = "登陆成功";
                }

                logger.info("收到服务器登陆响应: {}",respStr);
                break;
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