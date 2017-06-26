package org.itstack.demo.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 */
public class ClientSocket implements Runnable {

    private ChannelFuture future;

    @Override
    public void run() {

        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(workGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChildChannelHandler());
            b.option(ChannelOption.SO_KEEPALIVE, true);
            //端口可配
            future = b.connect("127.0.0.1", 7398).sync();
            if (future.isSuccess()){
                setFuture(future);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }
}
