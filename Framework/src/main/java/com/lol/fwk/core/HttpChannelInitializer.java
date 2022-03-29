package com.lol.fwk.core;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  17:52
 */

import com.google.protobuf.ExtensionRegistry;
import com.lol.Protocol;
import com.lol.fwk.handler.GameHandlerManager;
import com.lol.fwk.protobuf.MessageUpProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 17:52
 */

public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger logger = LoggerFactory.getLogger(GameHandlerManager.class);

    private static ExtensionRegistry registry = ExtensionRegistry.newInstance();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(GameHandlerManager.getInstance().getHandler(Protocol.TYPE_HTTP));
    }


}
