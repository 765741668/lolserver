package com.lol.demo.encode.jbossmarshall;

import com.lol.demo.game.GameClientChannelHandler;
import com.lol.demo.heartbeat.jbossmarshall.HeartBeatReqHandler;
import com.lol.demo.loginauth.jbossmarshall.LoginAuthReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
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
            // ip += i;
            SocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByName(ip), 8107);
            es.execute(() -> connect(remoteAddress));
        }
        es.shutdown();
    }

    public void connect(SocketAddress remoteAddress) {
        EventLoopGroup group = new NioEventLoopGroup();
        logger.info("start connect...");
        final LoginAuthReqHandler loginHandle = new LoginAuthReqHandler(this);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true) // 不延迟，消息立即发送
                    .option(ChannelOption.SO_BACKLOG, 1024) // 连接数
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.SO_SNDBUF, 2048)
                    .option(ChannelOption.SO_KEEPALIVE, true)// 长连接

                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(0, 4, 0));
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
                            ch.pipeline().addLast(new NettyMessageEncoder());
                            ch.pipeline().addLast(new ReadTimeoutHandler(50));
                            ch.pipeline().addLast(loginHandle);
                            ch.pipeline().addLast(new ClientIdleStateTrigger());
                            ch.pipeline().addLast(new HeartBeatReqHandler());
                            ch.pipeline().addLast(new GameClientChannelHandler());
                        }
                    });

            ChannelFuture f = b.connect(remoteAddress).addListener(new ClientConnectionListener(this, remoteAddress)).sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("connect exception : {}", e.getMessage(), e);
            group.shutdownGracefully();
        }
    }
}
