package com.lol.logic.player;/**
 * Description : 
 * Created by YangZH on 2017/4/11
 *  0:01
 */

import com.lol.PlayerProtocol;
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
 * Description :
 * Created by YangZH on 2017/4/11
 * 0:01
 */

public class PlayerHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(PlayerHandler.class);

    private MessageUpProto.MessageUp tempMessage = null;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        if (message.getHeader().getMsgType() == Protocol.TYPE_PLYAER) {
            logger.info("收到玩家模块消息: {}",ctx.channel().remoteAddress());
            MessageUpProto.PlayerUpBody player = message.getBody().getPlayer();
            if (player != null) {
                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, ctx.attr(Constans.conn).get()));
            }
        }else {
            ctx.fireChannelRead(message);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端断开连接了 : {}, 玩家准备下线。", ctx.channel().remoteAddress());
        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_PLYAER, -1, PlayerProtocol.OFFLINE_CREQ, tempMessage);
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