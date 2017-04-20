package com.lol.demo.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public static void main(String[] args) throws Exception {
        new NettyServer().bind(3334);
    }

    public void bind(int port) {
        // handle server receive client link thread group
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // handle io thread group
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-decoder", new HttpServerCodec());
                            pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            pipeline.addLast("http-chunked", new WebSocketServerProtocolHandler("/websocket"));
                            pipeline.addLast("websocketServerHandle", new WebSocketServerHandler());
                        }
                    });

            logger.info("服务端开启等待客户端连接 ... ...");
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("server({})  start error : {}", port, e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            logger.info("Server({})  freed thread group RAM.", port);
        }
    }

}