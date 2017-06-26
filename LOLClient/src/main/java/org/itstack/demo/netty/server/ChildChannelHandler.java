package org.itstack.demo.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
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
        // 解码转
        ch.pipeline().addLast(new RpcDecoder(Response.class));
        // 编码器
        ch.pipeline().addLast(new RpcEncoder(Request.class));
        // 在管道中添加我们自己的接收数据实现方法
        ch.pipeline().addLast(new MyServerHandler());
    }
}
