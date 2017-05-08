package com.lol.logic.select;


import com.lol.Protocol;
import com.lol.SelectProtocol;
import com.lol.buffer.GameUpBuffer;
import com.lol.common.Constans;
import com.lol.core.GameBoss;
import com.lol.protobuf.MessageUpProto;
import com.lol.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(SelectHandler.class);

    private MessageUpProto.MessageUp tempMessage = null;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        if (message.getHeader().getMsgType() == Protocol.TYPE_SELECT) {
            MessageUpProto.SelectUpBody match = message.getBody().getSelect();
            if (match != null) {
                logger.info("收到选人模块消息: {} ({})",match,ctx.channel().remoteAddress());
                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, ctx.attr(Constans.conn).get()));
            }
        }else {
            ctx.fireChannelRead(message);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端断开连接了 : {}, 准备取消选人。", ctx.channel().remoteAddress());
        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_SELECT, -1,
                SelectProtocol.DESTORY_BRO, tempMessage);
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