package com.lol.fwk.core;

import com.lol.fwk.buffer.GameDownBuffer;
import com.lol.fwk.protobuf.MessageDownProto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端连接类
 *
 * @author Randy
 *         2015-1-30
 */
public class Connection {
    private Logger logger = LoggerFactory.getLogger(Connection.class);
    /**
     * 连接用户名
     */
    private String acount;

    /**
     * 连接上下文
     */
    private ChannelHandlerContext ctx;

    public Connection(String acount, ChannelHandlerContext ctx) {
        this.acount = acount;
        this.ctx = ctx;
    }

    public String getAcount() {
        return acount;
    }

    public Channel getChannel() {
        return ctx.channel();
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return ctx;
    }

    /**
     * 往连接写入数据
     *
     * @param buffer
     */
    public void writeDown(GameDownBuffer buffer) {
        //水位流控 TODO
        if(ctx.channel().isWritable()){
          ctx.writeAndFlush(buffer.getBuffer());
        }else{
            logger.warn("服务器处于高水位状态,拒绝消息写出");
            ctx.writeAndFlush(buffer.getBuffer());
        }
    }

    public void writeDown(MessageDownProto.MessageDown buffer) {
        ctx.writeAndFlush(buffer);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "acount='" + acount + '\'' +
                '}';
    }
}
