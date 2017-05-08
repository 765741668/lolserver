/******************************************************************************
 *
 * Module Name:  com.lol.com.lol.demo.encode.jbossmarshall - ServerConnectionListener.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Jan 18, 2017
 * Last Updated By: randyzhyang
 * Last Updated Date: Jan 18, 2017
 * Description:
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
package com.lol.demo.encode.jbossmarshall;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class ClientConnectionListener implements ChannelFutureListener {
    private final Logger logger = LoggerFactory.getLogger(ClientConnectionListener.class);
    NettyClient client;
    SocketAddress address;

    public ClientConnectionListener(NettyClient client, SocketAddress address) {
        this.client = client;
        this.address = address;
    }

    @Override
    public void operationComplete(ChannelFuture futrue) throws Exception {
        if (futrue.isSuccess()) {
            logger.info("cleint start up successful...");
        } else {
            logger.info("cleint start up failed...");
            futrue.channel().eventLoop().schedule(() -> client.connect(address), 10, TimeUnit.SECONDS);
        }
    }

}
