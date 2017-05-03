package com.lol.demo.encode.protobuf.lol;

import com.lol.protobuf.MessageDownProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class LOLNettyClient {
    private final Logger logger = LoggerFactory.getLogger(LOLNettyClient.class);

    public static void main(String[] args) throws Exception {
        new LOLNettyClient().start();
    }

    public void start() throws UnknownHostException {
            SocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByName("127.0.01"), 9001);
            connect(remoteAddress);
    }

    public void connect(SocketAddress remoteAddress) {
        EventLoopGroup group = new NioEventLoopGroup();
        logger.info("start connect...");
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
//                    .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
//                    .option(ChannelOption.SO_SNDBUF, 2048)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
//                    .option(ChannelOption.SO_REUSEADDR, true)
//                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(MessageDownProto.MessageDown.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new LOLClientReqHandler());
                        }
                    });

            ChannelFuture f = b.connect(remoteAddress).sync();
            logger.info("end connect...");
            logger.info("client({}) start up...", remoteAddress);
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("connect exception : {}", e.getMessage(), e);
            group.shutdownGracefully();
        }
//        finally
//        {
//            ScheduledExecutorService ess = Executors.newScheduledThreadPool(1);
//            Thread t = new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    try
//                    {
//                        logger.info("connect on exception,reconnetct 5 seconds later...");
//                        TimeUnit.SECONDS.sleep(10);
//                        connect(remoteAddress);
//                    }
//                    catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            
//            ess.schedule(t, 5L, TimeUnit.SECONDS);
//            ess.shutdown();
//            t.interrupt();
//            logger.info("ScheduledExecutorService shutdown...");
////            group.shutdownGracefully();
//            logger.info("freed ram...");
//        }

    }
}
