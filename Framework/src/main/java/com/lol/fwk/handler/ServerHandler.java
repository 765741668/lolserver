package com.lol.fwk.handler;

import com.lol.ConnectProtocol;
import com.lol.Protocol;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.channel.GameOnlineChannelManager;
import com.lol.fwk.core.Connection;
import com.lol.fwk.core.ConnectionManager;
import com.lol.fwk.core.GameBoss;
import com.lol.fwk.protobuf.MessageUpProto;
import com.lol.fwk.util.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据包处理类
 *
 * @author Randy
 *         2015-1-30
 */
public class ServerHandler extends SimpleChannelInboundHandler<MessageUpProto.MessageUp> {

    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    /**
     * 连接属性
     */
    private static final AttributeKey<Connection> conn = AttributeKey.valueOf("Conn.attr");

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageUpProto.MessageUp msg) throws Exception {
        GameBoss.getInstance().getProcessor().process(new GameUpBuffer(msg, ctx.attr(conn).get()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有客户端连接了 : {}", ctx.channel().remoteAddress());
        Connection c = ConnectionManager.getInstance().addConnection("userAcount_yzh", ctx);
        ctx.attr(conn).set(c);
        GameOnlineChannelManager.getInstance().addOnlineChannel("Online").addOnlineConnection(c);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ConnectionManager.getInstance().removeConnection(ctx.attr(conn).get());
        GameOnlineChannelManager.getInstance().getOnlineChannel("AllUser").removeOnlineConnection(ctx.attr(conn).get());

        GameUpBuffer msg = Utils.packgeUpData(ctx.attr(conn).get(), Protocol.TYPE_CONNECT, -1, ConnectProtocol.DISCONNECT, null);

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
