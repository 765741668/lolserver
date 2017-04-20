package com.lol.channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏房间频道管理类
 *
 * @author Randy
 *         2015-2-5
 */
public class GameRoomChannelManager {

    private final ConcurrentHashMap<Long, GameRoomChannel> roomChannelMap = new ConcurrentHashMap<>();

    public static GameRoomChannelManager getInstance() {
        return GameChannelManagerHolder.instance;
    }

    public GameRoomChannel getRoomChannel(long name) {
        return roomChannelMap.get(name);
    }

    public GameRoomChannel addRoomChannel(long name) {
        GameRoomChannel channel = roomChannelMap.get(name);
        if (channel != null)
            return channel;
        else {
            channel = new GameRoomChannel(name);
            roomChannelMap.put(name, channel);
            return channel;
        }
    }

    private static final class GameChannelManagerHolder {
        private static final GameRoomChannelManager instance = new GameRoomChannelManager();
    }
}
