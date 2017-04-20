package com.lol.core;

import com.lol.util.ProReaderUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * 服务启动类
 *
 * @author Randy
 *         2015-1-30
 */
public class Bootstrap {

    private static Logger logger = Logger.getLogger(Bootstrap.class);
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
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new GameChannelInitializer());

            ChannelFuture f = b.bind(port).sync();
            logger.info("Server started at port:" + port + "......");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
