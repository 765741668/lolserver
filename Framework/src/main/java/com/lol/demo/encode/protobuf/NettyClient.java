package com.lol.demo.encode.protobuf;

import com.lol.demo.heartbeat.googleprotobuf.HeartBeatReqHandler;
import com.lol.demo.loginauth.googleprotobuf.LoginAuthReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public static void main(String[] args) throws Exception {
        new NettyClient().start();
    }

    public void start() throws UnknownHostException {
        int size = 1;
        ExecutorService es = Executors.newFixedThreadPool(size);
        for (int i = 1; i <= size; i++) {
            String ip = "127.0.0.1";
//            ip += i;
            SocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByName(ip), 8107);
            es.execute(() -> connect(remoteAddress));
        }
        es.shutdown();
    }

    public void connect(SocketAddress remoteAddress) {
        EventLoopGroup group = new NioEventLoopGroup();
        logger.info("start connect...");
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.SO_SNDBUF, 2048)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ReadTimeoutHandler(50))
                                    .addLast(new LoginAuthReqHandler())
                                    .addLast(new HeartBeatReqHandler())
                                    .addLast(new SubReqClientHandler());
                        }

                        ;
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
