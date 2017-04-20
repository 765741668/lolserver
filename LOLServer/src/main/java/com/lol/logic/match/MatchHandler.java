package com.lol.logic.match;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  9:57
 */

import com.lol.MatchProtocol;
import com.lol.Protocol;
import com.lol.buffer.GameUpBuffer;
import com.lol.common.Constans;
import com.lol.core.GameBoss;
import com.lol.protobuf.MessageUpProto;
import com.lol.util.Utils;
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

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        if (message.getHeader().getMsgType() == Protocol.TYPE_MATCH) {
            GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, ctx.attr(Constans.conn).get()));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_MATCH, -1, MatchProtocol.LEAVE_CREQ, null);
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