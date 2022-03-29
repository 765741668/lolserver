package com.lol.fwk.core;


import com.lol.fwk.buffer.GameUpBuffer;

/**
 * 上行消息解析接口
 *
 * @author Randy
 *         2015-2-3
 */
public interface GameUpProcessor {

    /**
     * 执行具体业务逻辑
     *
     * @param buffer
     */
    public abstract void pushToServer(final GameUpBuffer buffer);
}
