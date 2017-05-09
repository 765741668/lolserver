package com.lol.fwk.channel;

import com.lol.fwk.buffer.GameDownBuffer;
import com.lol.fwk.core.Connection;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 游戏房间频道类
 *
 * @author Randy
 *         2015-2-5
 */
public class GameOnlineChannel {

    /**
     * 客户端 在线连接集合
     */
    private DefaultChannelGroup onlineChannel;

    public GameOnlineChannel(String name) {
        onlineChannel = new DefaultChannelGroup(name, GlobalEventExecutor.INSTANCE);
    }

    public boolean isEnteredOnline(Connection connection) {
        return onlineChannel.contains(connection.getChannel());
    }

    /**
     * 添加连接到频道
     *
     * @param c
     * @return
     */
    public boolean addOnlineConnection(Connection c) {
        return onlineChannel.add(c.getChannel());
    }

    /**
     * 从频道中删除连接
     *
     * @param c
     * @return
     */
    public boolean removeOnlineConnection(Connection c) {
        return onlineChannel.remove(c.getChannel());
    }

    /**
     * 向频道中的连接广播数据
     *
     * @param buffer
     * @throws Exception
     */
    public void broadcastOnline(GameDownBuffer buffer) throws Exception {
        onlineChannel.writeAndFlush(buffer.getBuffer());
    }

}
