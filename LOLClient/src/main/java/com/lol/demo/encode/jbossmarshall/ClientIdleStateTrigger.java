/******************************************************************************
 *
 * Module Name:  com.lol.com.lol.demo.encode.jbossmarshall - IdleStateTrigger.java
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

import com.lol.demo.common.NettyMessage;
import com.lol.demo.enums.MessageType;
import com.lol.demo.game.Header;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientIdleStateTrigger extends ChannelHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ClientIdleStateTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                NettyMessage message = buildMessage();
                logger.info("Client send heartBeat message to server : ---> {} ({})",message,ctx.channel().remoteAddress());
                ctx.writeAndFlush(message);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private NettyMessage buildMessage() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();

        header.setType(MessageType.HEARTBEAT_REQ.getValue());
        message.setHeader(header);
        return message;
    }

}
