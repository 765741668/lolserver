/******************************************************************************
 *
 * Module Name:  netty - NettyMessageEncoder.java
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

import com.lol.demo.common.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    private NettyMarshallingEncoder marshall;

    public NettyMessageEncoder() {
        this.marshall = MarshallingCodeFactory.buildMarshallingEncoder();
    }

    @Override
    public void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("the encode message is null...");
        }

        ByteBuf sendBuf = Unpooled.buffer();

        sendBuf.writeInt(msg.getHeader().getOrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionId());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> m : msg.getHeader().getAttachment().entrySet()) {
            key = m.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = m.getValue();
            marshall.encode(ctx, value, sendBuf);
        }
        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() != null) {
            marshall.encode(ctx, msg.getBody(), sendBuf);
        }

        int readableBytes = sendBuf.readableBytes();
        sendBuf.setInt(4, readableBytes);
        out.add(sendBuf);
    }

}
