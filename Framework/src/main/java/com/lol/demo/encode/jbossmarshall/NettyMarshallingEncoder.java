/******************************************************************************
 *
 * Module Name:  env.netty - MarshallingEncoder.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 12, 2016
 * Last Updated By: java
 * Last Updated Date: May 12, 2016
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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

public class NettyMarshallingEncoder extends MarshallingEncoder {
    public NettyMarshallingEncoder(MarshallerProvider provider) {
        super(provider);
    }

    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception {
        super.encode(ctx, msg, out);
    }

}
