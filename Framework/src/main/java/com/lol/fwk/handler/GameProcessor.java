package com.lol.fwk.handler;

import com.lol.fwk.buffer.GameUpBuffer;

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
