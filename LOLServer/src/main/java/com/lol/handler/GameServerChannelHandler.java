package com.lol.handler;

import com.lol.*;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.common.Constans;
import com.lol.fwk.core.ConnectionManager;
import com.lol.fwk.core.GameBoss;
import com.lol.fwk.protobuf.MessageUpProto;
import com.lol.fwk.util.Utils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class GameServerChannelHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(GameServerChannelHandler.class);

    public GameServerChannelHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageUpProto.MessageUp message) throws Exception {
        MessageUpProto.MsgHeader header = message.getHeader();
        int msgType = header.getMsgType();
        try{
            if(msgType < 0 || msgType > 10){
                logger.warn("未知消息，不处理。 msgType: {}", msgType);
                ReferenceCountUtil.release(message);
            }else{
                GameUpBuffer buffer = new GameUpBuffer(message, ctx);
                if(msgType != 1){
                    buffer = new GameUpBuffer(message, ConnectionManager.getInstance().getConnection(header.getToken()));
                }
                GameBoss.getInstance().getProcessor().pushToServer(buffer);
            }
        }catch (Exception e){
            logger.info("读取消息异常:{}", e.getMessage(), e);
        }finally {
            ReferenceCountUtil.release(message);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("GameServerChannelHandler-有客户端连接了 : {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端断开连接了 : {}, 等待其余Handler处理Disconnect完成。", ctx.channel().remoteAddress());

        GameUpBuffer msg = Utils.packageUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_LOGIN, LoginProtocol.LOGIN_INACTIVE, LoginProtocol.LOGIN_INACTIVE, null);
        GameBoss.getInstance().getProcessor().pushToServer(msg);

        GameUpBuffer msg2 = Utils.packageUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_PLYAER, PlayerProtocol.OFFLINE_CREQ, PlayerProtocol.OFFLINE_CREQ, null);
        GameBoss.getInstance().getProcessor().pushToServer(msg2);

        GameUpBuffer msg3 = Utils.packageUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_MATCH, MatchProtocol.LEAVE_CREQ, MatchProtocol.LEAVE_CREQ, null);
        GameBoss.getInstance().getProcessor().pushToServer(msg3);

        GameUpBuffer msg4 = Utils.packageUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_SELECT, SelectProtocol.DESTORY_BRO, SelectProtocol.DESTORY_BRO, null);
        GameBoss.getInstance().getProcessor().pushToServer(msg4);

        GameUpBuffer msg5 = Utils.packageUpData(ctx.attr(Constans.conn).get(), Protocol.TYPE_FIGHT, FightProtocol.DESTORY_FIGHT, FightProtocol.DESTORY_FIGHT, null);
        GameBoss.getInstance().getProcessor().pushToServer(msg5);

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