package com.lol.logic.login;

import com.lol.LoginProtocol;
import com.lol.Protocol;
import com.lol.buffer.GameUpBuffer;
import com.lol.common.Constans;
import com.lol.core.Connection;
import com.lol.core.GameBoss;
import com.lol.protobuf.MessageUpProto;
import com.lol.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        if (message.getHeader().getMsgType() == Protocol.TYPE_LOGIN) {
            MessageUpProto.LoginUpBody login = message.getBody().getLogin();
            if (login != null) {
                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, new Connection(login.getAcount(), ctx)));
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端连接了 : {}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端断开连接了 : {}", ctx.channel().remoteAddress());

        super.channelInactive(ctx);
        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_LOGIN, -1, LoginProtocol.LOGIN_FAIL, null);
        GameBoss.getInstance().getProcessor().process(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}