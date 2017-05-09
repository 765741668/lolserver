package com.lol.fwk.core;

import com.lol.fwk.buffer.GameDownBuffer;
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

    @Override
    public String toString() {
        return "Connection{" +
                "acount='" + acount + '\'' +
                '}';
    }
}
