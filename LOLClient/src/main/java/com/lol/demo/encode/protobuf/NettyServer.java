package com.lol.demo.encode.protobuf;

import com.lol.demo.heartbeat.googleprotobuf.HeartBeatRespHandler;
import com.lol.demo.loginauth.googleprotobuf.LoginAuthServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public static void main(String[] args) throws Exception {
        new NettyServer().bind(8107);
    }

    public void bind(int port) {
        // handle server receive client link thread group
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // handle io thread group
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.SO_SNDBUF, 2048)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
//                                    .addLast(new ReadTimeoutHandler(50))
                                    .addLast(new LoginAuthServerHandler())
                                    .addLast(new HeartBeatRespHandler())
                                    .addLast(new SubReqServerHandler());
                        }
                    });

            ChannelFuture f = serverBootstrap.bind(port).sync();
            logger.info("server start up...");
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
