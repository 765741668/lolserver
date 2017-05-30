package com.lol.demo.encode.protobuf.lol;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  17:52
 */

import com.google.protobuf.ExtensionRegistry;
import com.lol.demo.encode.protobuf.MessageDownProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 17:52
 */

public class GameChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static ExtensionRegistry registry = ExtensionRegistry.newInstance();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        ch.pipeline().addLast(new ProtobufDecoder(MessageDownProto.MessageDown.getDefaultInstance(), registry));
        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        ch.pipeline().addLast(new ProtobufEncoder());

        ch.pipeline().addLast("PING",new IdleStateHandler(10,0,0, TimeUnit.SECONDS));

        for (ChannelHandler channel : GameHandlerManager.getInstance().getAllHandler().values()) {
            ch.pipeline().addLast(channel);
        }
    }


}
