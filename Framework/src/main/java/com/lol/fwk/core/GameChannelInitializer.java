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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 17:52
 */

public class GameChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger logger = LoggerFactory.getLogger(GameHandlerManager.class);

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

        try {
            for (Map.Entry<Integer, ChannelHandler> channel : GameHandlerManager.getInstance().getAllHandler().entrySet()) {
                ChannelHandler handler = channel.getValue();
                if(logger.isDebugEnabled()){
                    logger.debug("{} has been registed...", handler.getClass().getName());
                }
                if(channel.getKey() != 10){
                    ch.pipeline().addLast(handler);
                }
            }
        }catch (Exception e){
            logger.error("InitChannel error:{}", e.getMessage(), e);
            throw e;
        }

    }


}
