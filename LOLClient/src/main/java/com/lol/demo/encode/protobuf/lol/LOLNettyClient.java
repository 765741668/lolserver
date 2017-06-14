package com.lol.demo.encode.protobuf.lol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class LOLNettyClient {
    private static final Logger logger = LoggerFactory.getLogger(LOLNettyClient.class);

    private static ChannelFutureListener channelFutureListener;
    private static Bootstrap bootstrap;

    public static void main(String[] args) throws Exception {
        new LOLNettyClient().start();
        logger.info(">>>>>>>>>>>>>>>>>>>>同步等待");
    }

    public void start() throws UnknownHostException {
        SocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 9001);
        init(remoteAddress);
    }

    public void init(SocketAddress remoteAddress) {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.SO_SNDBUF, 2048)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    .handler(new GameChannelInitializer());

            channelFutureListener = channelFuture -> {

                if (channelFuture.isSuccess()) {
                    logger.info("连接服务器成功");
                } else {
                    logger.info("连接服务器失败,开始重新连接...");
                    //  5秒后重新连接
                    channelFuture.channel().eventLoop().schedule(() -> {
                        doConnect(remoteAddress);
                    }, 5, TimeUnit.SECONDS);
                }
            };

            doConnect(remoteAddress);

        } catch (Exception e) {
            logger.error("connect exception : {}", e.getMessage(), e);
            group.shutdownGracefully();
        }
    }

    public static void doConnect(SocketAddress remoteAddress){
        logger.info("start connect...");
        try {
            ChannelFuture f = bootstrap.connect(remoteAddress).sync();//异步
//            ChannelFuture f = bootstrap.connect(remoteAddress).channel().closeFuture().await();//同步等待
            f.addListener(channelFutureListener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("client({}) start up...", remoteAddress);
    }
}
