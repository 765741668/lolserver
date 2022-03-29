package com.lol.demo.encode.protobuf.lol;

import com.lol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LOLNettyClient {
    private static final Logger logger = LoggerFactory.getLogger(LOLNettyClient.class);

    private static ChannelFutureListener channelFutureListener;
    private static Bootstrap bootstrap;

    public static void main(String[] args) throws Exception {
        new LOLNettyClient().start(args[0]);
        System.out.println("异步等待服务返回结果.................");
    }

    public void start(String clientIndex) throws UnknownHostException {
        SocketAddress server = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 9001);
        SocketAddress local = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 18001);
        Consumer<Channel> control = MsgControl::sendMsg;
        switch (clientIndex) {
            case "2":
                local = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 18002);
                control = MsgControl2::sendMsg;
                break;
            case "3":
                local = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 18003);
                control = MsgControl3::sendMsg;
                break;
            case "4":
                local = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 18004);
                control = MsgControl4::sendMsg;
                break;
            case "5":
                local = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 18005);
                control = MsgControl5::sendMsg;
                break;
            case "6":
                local = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 18006);
                control = MsgControl6::sendMsg;
                break;
            default:
                break;
        }

        GameProcessorManager.getInstance().registerProcessor(Protocol.TYPE_LOGIN, new LOLClientLoginProcessor());
        GameProcessorManager.getInstance().registerProcessor(Protocol.TYPE_PLYAER, new LOLClientPlayerProcessor());
        GameProcessorManager.getInstance().registerProcessor(Protocol.TYPE_MATCH, new LOLClientMatchProcessor());
        GameProcessorManager.getInstance().registerProcessor(Protocol.TYPE_SELECT, new LOLClientSelectProcessor());
        GameProcessorManager.getInstance().registerProcessor(Protocol.TYPE_SELECT_ROOM, new LOLClientSelectRoomProcessor());
        GameProcessorManager.getInstance().registerProcessor(Protocol.TYPE_FIGHT_ROOM, new LOLClientFightRoomProcessor());

        init(server, local, control);
    }

    public void init(SocketAddress server, SocketAddress local, Consumer<Channel> control) {
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

            channelFutureListener = new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        logger.info("检测到成功连接到服务器。。。");
                    } else {
                        logger.info("连接服务器失败,开始重新连接...");
                        //  5秒后重新连接
                        channelFuture.channel().eventLoop().schedule(() -> {
                            doConnect(server, local, control);
                        }, 5, TimeUnit.SECONDS);
                    }
                }
            };

            doConnect(server, local, control);

        } catch (Exception e) {
            logger.error("connect exception : {}", e.getMessage(), e);
            group.shutdownGracefully();
        }
    }

    public static void doConnect(SocketAddress server, SocketAddress local, Consumer<Channel> control) {
        logger.info("start connect...");
        ChannelFuture f = bootstrap.connect(server, local);
        f.addListener(channelFutureListener);
        f.syncUninterruptibly();
        control.accept(f.channel());
        logger.info("client({}) start up...", local);
    }
}
