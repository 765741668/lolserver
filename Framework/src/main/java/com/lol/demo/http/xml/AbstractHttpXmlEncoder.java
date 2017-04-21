/******************************************************************************
 *
 * Module Name:  env.netty.http - AbstractHttpXmlEncoder.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 25, 2016
 * Last Updated By: java
 * Last Updated Date: May 25, 2016
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
package com.lol.demo.http.xml;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;

public abstract class AbstractHttpXmlEncoder<I> extends MessageToMessageEncoder<I> {
    private IBindingFactory factory = null;
    private StringWriter writer = null;

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object obj) throws Exception {
        factory = BindingDirectory.getFactory(obj.getClass());
        writer = new StringWriter();
        IMarshallingContext mc = factory.createMarshallingContext();
        mc.setIndent(2);
        mc.marshalDocument(obj, "UTF-8", null, writer);

        String xml = writer.toString();
        writer.close();
        writer = null;

        return Unpooled.copiedBuffer(xml, CharsetUtil.UTF_8);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

}
