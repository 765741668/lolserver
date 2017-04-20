package com.lol.handler;

import com.lol.buffer.GameUpBuffer;

/**
 * 消息处理接口
 *
 * @author Randy
 *         2015-1-30
 */
public interface GameProcessor {

    /**
     * 处理服务器接收到的消息
     *
     * @param buffer
     * @throws Throwable
     */
    public abstract void process(GameUpBuffer buffer) throws Exception;
}
