package com.lol.buffer;

import com.lol.protobuf.MessageDownProto;

/**
 * 下行消息类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameDownBuffer {

    /**
     * 接收数据包
     */
    private MessageDownProto.MessageDown buffer;

    public MessageDownProto.MessageDown getBuffer() {
        return buffer;
    }

    public void setBuffer(MessageDownProto.MessageDown buffer) {
        this.buffer = buffer;
    }


}
