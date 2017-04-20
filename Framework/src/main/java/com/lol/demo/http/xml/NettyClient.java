package com.lol.demo.http.xml;

import hk.hkjc.yzh.schema.test.jaxb.shiporder_v1.CTshiporder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public static void main(String[] args) throws Exception {
        new NettyClient().start();
    }

    public void start() throws UnknownHostException {
        int size = 1;
        ExecutorService es = Executors.newFixedThreadPool(size);
        for (int i = 1; i <= size; i++) {
            String ip = "127.0.0.";
            ip += i;
            SocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByName(ip), 8107);
            es.execute(() -> connect(remoteAddress));
        }
        es.shutdown();
    }

    public void connect(SocketAddress remoteAddress) {
        EventLoopGroup group = new NioEventLoopGroup();
        logger.info("start connect...");
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast("http-response-decoder", new HttpResponseDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("xml-response-decoder", new HttpXmlResponseDecoder(CTshiporder.class, true));
                            ch.pipeline().addLast("http-request-encoder", new HttpRequestEncoder());
                            ch.pipeline().addLast("xml-request-encoder", new HttpXmlRequestEncoder());
                            ch.pipeline().addLast("xmlClientHandle", new HttpXmlClientHandle());
                        }
                    });

            ChannelFuture f = b.connect(remoteAddress).sync();
            logger.info("end connect...");
            logger.info("client({}) start up...", remoteAddress);
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.info("connect exception...");
            logger.info("ScheduledExecutorService shutdown...");
            logger.info("freed ram...");
            logger.info("------------------------Reconnetct 5 seconds later------------------------");
            group.shutdownGracefully();
        } finally {
            ScheduledExecutorService ess = Executors.newScheduledThreadPool(1);
            ess.schedule(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect(remoteAddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }), 5L, TimeUnit.SECONDS);
            ess.shutdown();
        }

    }
}