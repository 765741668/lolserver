package com.lol.fwk.core;

import com.lol.fwk.util.ProReaderUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务启动类
 *
 * @author Randy
 *         2015-1-30
 */
public class TCPBootstrap {

    private static Logger logger = LoggerFactory.getLogger(TCPBootstrap.class);
    /**
     * 监听端口号
     */
    private int port;

    public TCPBootstrap() {
        this.port = Integer.parseInt(ProReaderUtil.getInstance().getNettyPro().get("tcpPort"));
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
                    .childOption(ChannelOption.SO_SNDBUF, 2048)//发送数据包大小
                    .option(ChannelOption.SO_KEEPALIVE, true) // 是否长连接
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOW_HALF_CLOSURE, true)
                    //读写水位流控
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(
                    32, 64))
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channelFactory(NioServerSocketChannel::new)//WINDOWS
//                    .channelFactory(EpollServerSocketChannel::new)//LINUX
                    .childHandler(new GameChannelInitializer());
//            ChannelFuture cf = b.bind(port).sync();
//            logger.info("Tcp Server started at port: {} ......", port);
//            cf.channel().closeFuture().sync();
            b.bind(port).addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("Tcp Server start success on:{}", port);
                } else {
                    logger.error("Tcp Server start failure on:{}", port, future.cause());
                }
            });
        } catch (Exception e){
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
