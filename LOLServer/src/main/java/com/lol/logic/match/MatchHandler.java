package com.lol.logic.match;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  9:57
 */

import com.lol.MatchProtocol;
import com.lol.Protocol;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.common.Constans;
import com.lol.fwk.core.GameBoss;
import com.lol.fwk.protobuf.MessageUpProto;
import com.lol.fwk.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description :战斗匹配逻辑处理类
 * Created by YangZH on 2017/4/12
 * 9:57
 */

public class MatchHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(MatchHandler.class);

    private MessageUpProto.MessageUp tempMessage = null;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        if (message.getHeader().getMsgType() == Protocol.TYPE_MATCH) {
            MessageUpProto.MatchUpBody match = message.getBody().getMatch();
            if (match != null) {
                logger.info("收到匹配模块消息: {} ({})",match,ctx.channel().remoteAddress());
                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, ctx.attr(Constans.conn).get()));
            }
        }else {
            ctx.fireChannelRead(message);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端断开连接了 : {}, 准备取消匹配。", ctx.channel().remoteAddress());

        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_MATCH, -1, MatchProtocol.LEAVE_CREQ, tempMessage);
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