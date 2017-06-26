package org.itstack.demo.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.itstack.demo.netty.codec.RpcDecoder;
import org.itstack.demo.netty.codec.RpcEncoder;
import org.itstack.demo.netty.msg.Request;
import org.itstack.demo.netty.msg.Response;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(20, 10, 0));
        ch.pipeline().addLast(new RpcDecoder(Request.class));
        ch.pipeline().addLast(new RpcEncoder(Response.class));
        ch.pipeline().addLast(new MyClientHandler());
    }

}
