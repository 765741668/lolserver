package com.lol.channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏房间频道管理类
 *
 * @author Randy
 *         2015-2-5
 */
public class GameOnlineChannelManager {

    private final ConcurrentHashMap<String, GameOnlineChannel> onlineChannelMap = new ConcurrentHashMap<>();

    public static GameOnlineChannelManager getInstance() {
        return GameChannelManagerHolder.instance;
    }

    public GameOnlineChannel getOnlineChannel(String name) {
        return onlineChannelMap.get(name);
    }

    public GameOnlineChannel addOnlineChannel(String name) {
        GameOnlineChannel channel = onlineChannelMap.get(name);
        if (channel != null)
            return channel;
        else {
            channel = new GameOnlineChannel(name);
            onlineChannelMap.put(name, channel);
            return channel;
        }
    }

    private static final class GameChannelManagerHolder {
        private static final GameOnlineChannelManager instance = new GameOnlineChannelManager();
    }

}
