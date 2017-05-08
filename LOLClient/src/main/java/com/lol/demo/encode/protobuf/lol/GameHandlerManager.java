package com.lol.demo.encode.protobuf.lol;

import io.netty.channel.ChannelHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑管理类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameHandlerManager {

    /**
     * 业务逻辑集合
     */
    private Map<Integer, ChannelHandler> channelHandlerMap = new HashMap<>();

    public static GameHandlerManager getInstance() {
        return GameHandlerManagerHolder.instance;
    }

    /**
     * 注册业务逻辑
     *
     * @param protocol
     * @param channel
     * @throws Exception
     */
    public void registerHandler(int protocol, ChannelHandler channel) throws Exception {
        channelHandlerMap.put(protocol, channel);
    }

    /**
     * 获取业务逻辑
     *
     * @return
     */
    public ChannelHandler getHandler(int protocol) {
        return channelHandlerMap.get(protocol);
    }

    public void removeHandler(int protocol) {
        if(channelHandlerMap.containsKey(protocol)){
            channelHandlerMap.remove(protocol);
        }
    }

    public void clearHandlers() {
        channelHandlerMap.clear();
    }

    /**
     * 获取业务逻辑
     *
     * @return
     */
    public Map<Integer, ChannelHandler> getAllHandler() {
        return channelHandlerMap;
    }

    private static class GameHandlerManagerHolder {
        private static GameHandlerManager instance = new GameHandlerManager();
    }
}
