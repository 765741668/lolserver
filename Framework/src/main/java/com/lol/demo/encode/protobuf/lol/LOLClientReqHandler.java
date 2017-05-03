package com.lol.demo.encode.protobuf.lol;

import com.lol.LoginProtocol;
import com.lol.Protocol;
import com.lol.protobuf.MessageDownProto;
import com.lol.protobuf.MessageUpProto;
import com.lol.util.Utils;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientReqHandler extends ChannelHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(LOLClientReqHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connected -->> [{}]",ctx.channel().remoteAddress().toString());
        MessageUpProto.LoginUpBody.Builder loginUpBody = MessageUpProto.LoginUpBody.newBuilder();
//        loginUpBody.setAcount("yzh");
        loginUpBody.setPassword("123456");

        logger.info("Send data -->> {}",loginUpBody.build().toString());
        MessageUpProto.MessageUp messageUp = Utils.packgeUpData(Protocol.TYPE_LOGIN,1, LoginProtocol.LOGIN_CREQ,loginUpBody.build());
        ctx.writeAndFlush(messageUp);

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        MessageDownProto.MessageDown resp = (MessageDownProto.MessageDown) msg;
        System.out.println("receive server response: " + resp.getBody().getLogin().getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("unexpected exception from downstream: " + cause.getMessage());
        ctx.close();
    }

}