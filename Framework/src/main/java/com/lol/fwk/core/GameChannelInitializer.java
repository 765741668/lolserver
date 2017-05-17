package com.lol.fwk.core;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  17:52
 */

import com.google.protobuf.ExtensionRegistry;
import com.lol.fwk.handler.GameHandlerManager;
import com.lol.fwk.protobuf.MessageUpProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;


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
        ch.pipeline().addLast(new ProtobufDecoder(MessageUpProto.MessageUp.getDefaultInstance(), registry));
        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        ch.pipeline().addLast(new ProtobufEncoder());
        //TODO : 设置心跳检测频率 指定时间内 服务器没有收到消息 则超时
//        ch.pipeline().addLast("readTimeOutHandler", new ReadTimeoutHandler(Integer.parseInt(
//                ProReaderUtil.getInstance().getNettyPro().get("heartBeatTimeOut"))));

        for (ChannelHandler channel : GameHandlerManager.getInstance().getAllHandler().values()) {
            ch.pipeline().addLast(channel);
        }
    }


}
