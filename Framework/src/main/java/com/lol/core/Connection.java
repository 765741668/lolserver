package com.lol.core;

import com.lol.buffer.GameDownBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端连接类
 *
 * @author Randy
 *         2015-1-30
 */
public class Connection {

    /**
     * 连接用户名
     */
    private String acount;

    /**
     * 连接上下文
     */
    private ChannelHandlerContext ctx;

    private volatile boolean isClosed = false;

    /**
     * 是否是被踢下线
     */
    private boolean isKick = false;

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
        ctx.writeAndFlush(buffer.getBuffer());
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (!isClosed) {
            isClosed = true;
            ctx.close();
        }
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isKick() {
        return isKick;
    }

    public void setKick(boolean isKick) {
        this.isKick = isKick;
    }
}
