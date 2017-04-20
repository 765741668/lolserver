package com.lol.demo.encode.jbossmarshall;

import com.lol.demo.game.GameServerChannelHandler;
import com.lol.demo.heartbeat.jbossmarshall.HeartBeatRespHandler;
import com.lol.demo.loginauth.jbossmarshall.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final int port = 8107;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
    private boolean closed;

    public static void main(String[] args) throws Exception {
        new NettyServer().start();
    }

    public void close() {
        closed = true;

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        logger.info("Server({})  shut down.", port);

    }

    public void init() {
        closed = false;
        // handle server receive client link thread group
        bossGroup = new NioEventLoopGroup();
        // handle io thread group
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(5, 0, 0));
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
                            ch.pipeline().addLast(new NettyMessageEncoder());
                            ch.pipeline().addLast(new ReadTimeoutHandler(50));
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                            //client 5sec send, server if 10sec not receive msg,think it disconnect
                            ch.pipeline().addLast(new ServerIdleStateTrigger());
                            ch.pipeline().addLast(new HeartBeatRespHandler());
                            ch.pipeline().addLast(new GameServerChannelHandler());
                        }
                    });
        } catch (Exception e) {
            logger.error("server({})  init error : {}", port, e.getMessage(), e);
        }
    }

    public void doBind() {
        if (closed) {
            return;
        }

        try {
            ChannelFuture f = serverBootstrap.bind(port).addListener(new ServerConnectionListener(this)).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("server({})  start error : {}", port, e.getMessage(), e);
        }
    }

    public void start() {
        init();
        doBind();
    }

}
