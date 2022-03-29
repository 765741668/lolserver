package com.lol.fwk.core;

import com.lol.fwk.util.ProReaderUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 服务启动类
 *
 * @author Randy
 *         2015-1-30
 */
public class HttpBootstrap {

    private static Logger logger = LoggerFactory.getLogger(HttpBootstrap.class);
    /**
     * 监听端口号
     */
    private int port;

    public HttpBootstrap() {
        this.port = Integer.parseInt(ProReaderUtil.getInstance().getNettyPro().get("httpPort"));
    }

    /**
     * 初始化服务器设置
     *
     * @throws Exception
     */
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024) // 连接数
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channelFactory(NioServerSocketChannel::new)//WINDOWS
//                    .channelFactory(EpollServerSocketChannel::new)//LINUX
                    .childHandler(new HttpChannelInitializer());
//            ChannelFuture cf = b.bind(port).sync();
//            logger.info("Http Server started at port: {} ......", port);
//            cf.channel().closeFuture().sync();
            b.bind(port).addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("Http Server start success on:{}", port);
                } else {
                    logger.error("Http Server start failure on:{}", port, future.cause());
                }
            });
        } catch (Exception e){
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
