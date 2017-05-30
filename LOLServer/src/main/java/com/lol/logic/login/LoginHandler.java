package com.lol.logic.login;

import com.lol.LoginProtocol;
import com.lol.Protocol;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.common.Constans;
import com.lol.fwk.core.Connection;
import com.lol.fwk.core.GameBoss;
import com.lol.fwk.protobuf.MessageUpProto;
import com.lol.fwk.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private MessageUpProto.MessageUp tempMessage = null;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        if (message.getHeader().getMsgType() == Protocol.TYPE_LOGIN) {
            MessageUpProto.LoginUpBody login = message.getBody().getLogin();
            if (login != null) {
                logger.info("收到登陆模块消息: {} ({})",login,ctx.channel().remoteAddress());
                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, new Connection(login.getAcount(), ctx)));
            }
        }else {
            ctx.fireChannelRead(message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端连接了 : {}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端断开连接了 : {}, 等待其余Hanlert处理Disconnect完成。", ctx.channel().remoteAddress());

        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_LOGIN, -1, LoginProtocol.LOGIN_INACTIVE, tempMessage);
        GameBoss.getInstance().getProcessor().process(msg);

        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error("服务器异常!",cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }

}