package com.lol.fwk.core;

import com.lol.fwk.util.ProReaderUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务启动类
 *
 * @author Randy
 *         2015-1-30
 */
public class Bootstrap {

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    /**
     * 监听端口号
     */
    private int port;

    public Bootstrap() {
        this.port = Integer.parseInt(ProReaderUtil.getInstance().getNettyPro().get("port"));
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
                    .option(ChannelOption.TCP_NODELAY, true) // 不延迟，消息立即发送
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) //连接超时
                    .option(ChannelOption.SO_SNDBUF, 2048)//发送数据包大小
                    .option(ChannelOption.SO_KEEPALIVE, true) // 是否长连接
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new GameChannelInitializer());
            ChannelFuture f = b.bind(port).sync();
            logger.info("Server started at port: {} ......", port);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
