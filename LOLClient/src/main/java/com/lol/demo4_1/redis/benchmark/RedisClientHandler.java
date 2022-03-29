/*
 * Copyright 2016 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.lol.demo4_1.redis.benchmark;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.redis.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * An example Redis client handler. This handler read input from STDIN and write output to STDOUT.
 */
public class RedisClientHandler extends ChannelDuplexHandler {

    public RedisClientHandler(Long counter) {
        this.counter = counter;
    }

    private static Logger logger = LoggerFactory.getLogger(RedisClientHandler.class);

    private Long counter;
    private long start;
    private long sendCounter = 0;
    private long sendCost = 0;
    private long receiveCounter = 0;
    private long receiveCost = 0;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if(start == 0){
            start = System.currentTimeMillis();
        }
        String[] commands = ((String) msg).split("\\s+");
        List<RedisMessage> children = new ArrayList<RedisMessage>(commands.length);
        for (String cmdString : commands) {
            children.add(new FullBulkStringRedisMessage(ByteBufUtil.writeUtf8(ctx.alloc(), cmdString)));
        }
        RedisMessage request = new ArrayRedisMessage(children);
        ctx.write(request, promise);
        sendCounter++;
        if(sendCounter == counter){
            sendCost = System.currentTimeMillis() - start;
            double sec = sendCost / 1000.0;
            logger.info("send qps: {}", sec == 0 ? counter : counter/sec);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RedisMessage redisMessage = (RedisMessage) msg;
//        printAggregatedRedisResponse(redisMessage);
        ReferenceCountUtil.release(redisMessage);
        receiveCounter ++;
        if(receiveCounter == counter){
            receiveCost = System.currentTimeMillis() - start;
            double sec = receiveCost / 1000.0;
            logger.info("receive qps: {}", sec == 0 ? counter : counter/sec);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.print("exceptionCaught: ");
        cause.printStackTrace(System.err);
        ctx.close();
    }

    private static void printAggregatedRedisResponse(RedisMessage msg) {
        if (msg instanceof SimpleStringRedisMessage) {
            logger.info(((SimpleStringRedisMessage) msg).content());
        } else if (msg instanceof ErrorRedisMessage) {
            logger.info(((ErrorRedisMessage) msg).content());
        } else if (msg instanceof IntegerRedisMessage) {
            logger.info("{}",((IntegerRedisMessage) msg).value());
        } else if (msg instanceof FullBulkStringRedisMessage) {
            logger.info(getString((FullBulkStringRedisMessage) msg));
        } else if (msg instanceof ArrayRedisMessage) {
            for (RedisMessage child : ((ArrayRedisMessage) msg).children()) {
                printAggregatedRedisResponse(child);
            }
        } else {
            throw new CodecException("unknown message type: " + msg);
        }
    }

    private static String getString(FullBulkStringRedisMessage msg) {
        if (msg.isNull()) {
            return "(null)";
        }
        return msg.content().toString(CharsetUtil.UTF_8);
    }

    public void reSetCounter(Long l) {
        if(l != null){
            counter = l;
        }
        start = 0;
        sendCounter = 0;
        sendCost = 0;
        receiveCounter = 0;
        receiveCost = 0;
    }
}
