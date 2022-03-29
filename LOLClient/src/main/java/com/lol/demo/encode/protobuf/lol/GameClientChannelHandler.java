package com.lol.demo.encode.protobuf.lol;

import com.lol.*;
import com.lol.demo.encode.protobuf.MessageDownProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

@ChannelHandler.Sharable
public class GameClientChannelHandler extends SimpleChannelInboundHandler<MessageDownProto.MessageDown> {

    private static Logger logger = LoggerFactory.getLogger(GameClientChannelHandler.class);

    public GameClientChannelHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageDownProto.MessageDown message) throws Exception {
        int msgType = message.getHeader().getMsgType();
        try{
            if(msgType < 0 || msgType > 10){
                logger.warn("未知消息，不处理。 msgType: {}", msgType);
                ReferenceCountUtil.release(message);
            }else{
                GameProcessorManager.getInstance().getProcessor(msgType).process(message);
            }
        }catch (Exception e){
            logger.info("读取消息异常:{}", e.getMessage(), e);
        }finally {
            ReferenceCountUtil.release(message);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("GameClientChannelHandler-链接到服务器：[{}]",ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        logger.info("{} is inactive .....", remoteAddress);
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("未知异常 from downstream : {}",cause.getMessage(),cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }

}