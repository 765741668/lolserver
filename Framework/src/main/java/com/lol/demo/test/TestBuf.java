/******************************************************************************
 *
 * Module Name:  env.netty.test - TestBuf.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Jun 2, 2016
 * Last Updated By: java
 * Last Updated Date: Jun 2, 2016
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
package com.lol.demo.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBuf {
    private final Logger logger = LoggerFactory.getLogger(TestBuf.class);

    @Test
    public void test() {
        long count = 3000000;
        byte[] test = new byte[1024];
        long start1 = System.currentTimeMillis();
        ByteBuf unpooled_directbuffer = null;
        for (int i = 0; i < count; i++) {
            unpooled_directbuffer = Unpooled.directBuffer(1024);
            unpooled_directbuffer.writeBytes(test);
            unpooled_directbuffer.release();
        }
        logger.info("unpooled_directbuffer : {}", System.currentTimeMillis() - start1);

        long start2 = System.currentTimeMillis();
        ByteBuf Unpooled_copiedBuffer = null;
        for (int i = 0; i < count; i++) {
            Unpooled_copiedBuffer = Unpooled.copiedBuffer(test);
        }
        logger.info("Unpooled_copiedBuffer : {}", (System.currentTimeMillis() - start2));

        long start3 = System.currentTimeMillis();
        ByteBuf Unpooled_buffer = null;
        for (int i = 0; i < count; i++) {
            Unpooled_buffer = Unpooled.buffer(1024);
            Unpooled_buffer.writeBytes(test);
        }
        logger.info("Unpooled_buffer : {}", (System.currentTimeMillis() - start3));

        long start4 = System.currentTimeMillis();
        ByteBuf Unpooled_compositeBuffer = null;
        for (int i = 0; i < count; i++) {
            Unpooled_compositeBuffer = Unpooled.compositeBuffer(1024);
            Unpooled_compositeBuffer.writeBytes(test);
        }
        logger.info("Unpooled_compositeBuffer : " + (System.currentTimeMillis() - start4));

        long start5 = System.currentTimeMillis();
        ByteBuf PooledByteBufAllocator_DEFAULT_buffer = null;
        for (int i = 0; i < count; i++) {
            PooledByteBufAllocator_DEFAULT_buffer = PooledByteBufAllocator.DEFAULT.buffer(1024);
            PooledByteBufAllocator_DEFAULT_buffer.writeBytes(test);
            PooledByteBufAllocator_DEFAULT_buffer.release();
        }
        logger.info("PooledByteBufAllocator_DEFAULT_buffer : {}", (System.currentTimeMillis() - start5));

        long start6 = System.currentTimeMillis();
        ByteBuf PooledByteBufAllocator_DEFAULT_compositeBuffer = null;
        for (int i = 0; i < count; i++) {
            PooledByteBufAllocator_DEFAULT_compositeBuffer = PooledByteBufAllocator.DEFAULT.compositeBuffer(1024);
            PooledByteBufAllocator_DEFAULT_compositeBuffer.writeBytes(test);
            PooledByteBufAllocator_DEFAULT_compositeBuffer.release();
        }
        logger.info("PooledByteBufAllocator_DEFAULT_compositeBuffer : {}", (System.currentTimeMillis() - start6));

        long start7 = System.currentTimeMillis();
        ByteBuf PooledByteBufAllocator_DEFAULT_directBuffer = null;
        for (int i = 0; i < count; i++) {
            PooledByteBufAllocator_DEFAULT_directBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            PooledByteBufAllocator_DEFAULT_directBuffer.writeBytes(test);
            PooledByteBufAllocator_DEFAULT_directBuffer.release();
        }
        logger.info("PooledByteBufAllocator_DEFAULT_directBuffer : {}", (System.currentTimeMillis() - start7));

    }

}