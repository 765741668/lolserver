package com.lol.fwk.channel;

import com.lol.fwk.buffer.GameDownBuffer;
import com.lol.fwk.core.Connection;
import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.List;

/**
 * 游戏房间频道类
 *
 * @author Randy
 * 2015-2-5
 */
public class GameRoomChannel {

    /**
     * 客户端 房间连接集合
     */
    private DefaultChannelGroup roomChannel;

    public GameRoomChannel(Integer name) {
        roomChannel = new DefaultChannelGroup(name + "", GlobalEventExecutor.INSTANCE);
    }

    public boolean isEnteredRoom(Connection connection) {
        return roomChannel.contains(connection.getChannel());
    }

    public void clearRoomConnection() {
        roomChannel.clear();
    }

    /**
     * 添加连接到频道
     *
     * @param c
     * @return
     */
    public boolean addRoomConnection(Connection c) {
        return roomChannel.add(c.getChannel());
    }

    /**
     * 从频道中删除连接
     *
     * @param c
     * @return
     */
    public boolean removeRoomConnection(Connection c) {
        //默认房间号自减
        return roomChannel.remove(c.getChannel());
    }

    /**
     * 向频道中的连接广播数据
     *
     * @param buffer
     * @throws Exception
     */
    public void broadcastRoom(GameDownBuffer buffer) throws Exception {
        roomChannel.writeAndFlush(buffer.getBuffer());
    }

    /**
     * 向频道中的连接广播数据,除了channelExcept
     *
     * @param buffer
     * @param channelExcept
     * @throws Exception
     */
    public void broadcastRoom(GameDownBuffer buffer, Channel channelExcept) throws Exception {
        roomChannel.parallelStream()
                .filter(channel ->
                        channel != channelExcept
                ).forEach(channel ->
                        channel.writeAndFlush(buffer.getBuffer())
        );
    }

    /**
     * 向指定频道中的连接广播数据
     *
     * @param buffer
     * @param channels
     * @throws Exception
     */
    public void broadcastRoom(GameDownBuffer buffer, List<Channel> channels) throws Exception {
        roomChannel.parallelStream().filter(channels::contains).forEach(channel -> channel.writeAndFlush(buffer.getBuffer()));
    }
}
