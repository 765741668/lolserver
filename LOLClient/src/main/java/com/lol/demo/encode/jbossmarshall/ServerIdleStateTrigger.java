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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                throw new Exception("idle exception--->client should send hb-msg to server 5sec interval,but server 10sec received any hb-msg.");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
