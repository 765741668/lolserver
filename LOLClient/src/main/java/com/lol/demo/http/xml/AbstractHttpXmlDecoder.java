/******************************************************************************
 *
 * Module Name:  netty.http - AbstractHttpXmlEncoder.java
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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;

public abstract class AbstractHttpXmlDecoder<I> extends MessageToMessageDecoder<I> {
    private final Logger logger = LoggerFactory.getLogger(AbstractHttpXmlDecoder.class);
    private IBindingFactory factory = null;
    private StringReader reader = null;
    private Class<?> clazz;
    private boolean isPrint;

    public AbstractHttpXmlDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    public AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    protected Object decode0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        factory = BindingDirectory.getFactory(clazz);
        String content = buf.toString(CharsetUtil.UTF_8);

        if (isPrint)
            logger.info("the body is : {}", content);
        reader = new StringReader(content);

        IUnmarshallingContext uc = factory.createUnmarshallingContext();
        Object result = uc.unmarshalDocument(reader);
        reader.close();
        reader = null;

        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

}
