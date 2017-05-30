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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LOLNettyClient {
    private final Logger logger = LoggerFactory.getLogger(LOLNettyClient.class);

    private ChannelFutureListener channelFutureListener;
    private Bootstrap bootstrap;

    public static void main(String[] args) throws Exception {
        new LOLNettyClient().start(args[0]);
    }

    public void start(String clientCount) throws UnknownHostException {
        int size = Integer.valueOf(clientCount);
        ExecutorService es = Executors.newFixedThreadPool(size);
        for (int i = 1; i <= size; i++) {
            String ip = "127.0.0.";
            ip += i;
            SocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByName(ip), 9001);
            es.execute(() -> init(remoteAddress));
        }
        es.shutdown();
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

    private void doConnect(SocketAddress remoteAddress){
        logger.info("start connect...");
        try {
            ChannelFuture f = bootstrap.connect(remoteAddress).sync();
            f.addListener(channelFutureListener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("client({}) start up...", remoteAddress);
    }
}
