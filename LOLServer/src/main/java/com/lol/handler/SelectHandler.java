//package com.lol.handler;
//
//
//import com.lol.Protocol;
//import com.lol.SelectProtocol;
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
//public class SelectHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {
//
//    private static Logger logger = LoggerFactory.getLogger(SelectHandler.class);
//
//    private MessageUpProto.MessageUp tempMessage = null;
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("SelectHandler-有客户端连接了 : {}", ctx.channel().remoteAddress());
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
//        if (message.getHeader().getMsgType() == Protocol.TYPE_SELECT) {
//            MessageUpProto.SelectUpBody match = message.getBody().getSelect();
//            if (match != null) {
//                logger.info("收到选人模块消息: {} ({})",match,ctx.channel().remoteAddress());
//                GameBoss.getInstance().getProcessor().process(new GameUpBuffer(message, ctx.attr(Constans.conn).get()));
//            }
//        }else {
//            ctx.fireChannelRead(message);
//        }
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("有客户端断开连接了 : {}, 准备取消选人。", ctx.channel().remoteAddress());
//        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_SELECT, SelectProtocol.DESTORY_BRO,
//                SelectProtocol.DESTORY_BRO, tempMessage);
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
//}