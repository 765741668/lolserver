//package com.lol.handler;
//
//
//import com.lol.FightProtocol;
//import com.lol.Protocol;
//import com.lol.fwk.buffer.GameUpBuffer;
//import com.lol.fwk.common.Constans;
//import com.lol.fwk.core.GameBoss;
//import com.lol.fwk.protobuf.MessageUpProto;
//import com.lol.fwk.util.Utils;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@ChannelHandler.Sharable
//public class FightHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {
//
//    private static Logger logger = LoggerFactory.getLogger(FightHandler.class);
//
//    private MessageUpProto.MessageUp tempMessage = null;
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("FightHandler-有客户端连接了 : {}", ctx.channel().remoteAddress());
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("有客户端断开连接了 : {}, 准备从战斗房间连接管理器移除客户端链接。", ctx.channel().remoteAddress());
//
//        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_FIGHT, FightProtocol.DESTORY_FIGHT,
//                FightProtocol.DESTORY_FIGHT, tempMessage);
//        GameBoss.getInstance().getProcessor().process(msg);
//
//        super.channelInactive(ctx);
//        ctx.close();
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
////        logger.error("服务器异常!",cause);
//        ctx.fireExceptionCaught(cause);
//        ctx.close();
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, MessageUpProto.MessageUp messageUp) throws Exception {
//        if (messageUp.getHeader().getMsgType() == Protocol.TYPE_FIGHT) {
//            MessageUpProto.FightUpBody fight = messageUp.getBody().getFight();
//            if (fight != null) {
//                logger.info("收到战斗模块消息: {} ({})", fight, ctx.channel().remoteAddress());
//                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(messageUp, ctx.channel().attr(Constans.conn).get()));
//            }
//        } else {
//            ctx.fireChannelRead(messageUp);
//        }
//    }
//}