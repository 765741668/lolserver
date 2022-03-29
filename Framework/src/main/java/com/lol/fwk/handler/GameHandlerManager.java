package com.lol.fwk.handler;

import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 业务逻辑管理类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameHandlerManager {
    private static Logger logger = LoggerFactory.getLogger(GameHandlerManager.class);
    /**
     * 业务逻辑集合
     */
    private Map<Integer, ChannelHandler> channelHandlerMap = new TreeMap<>();

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
        logger.info("{} handler has been registed...",protocol);
    }

    /**
     * 获取业务逻辑
     *
     * @return
     */
    public ChannelHandler getHandler(int protocol) {
        return channelHandlerMap.get(protocol);
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
