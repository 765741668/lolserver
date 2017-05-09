package com.lol.fwk.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接管理器类
 *
 * @author Randy
 *         2015-1-30
 */
public class ConnectionManager {

    /**
     * 连接存储集合
     */
    private Map<String, Connection> connections = new ConcurrentHashMap<>();

    public static ConnectionManager getInstance() {
        return ConnectionManagerHolder.instance;
    }

    /**
     * 添加一个连接到集合
     *
     * @param c
     */
    public void addConnection(String acount, Connection c) {
        connections.put(acount, c);
    }

    /**
     * 根据连接上下文添加一个连接到集合
     *
     * @param ctx
     */
    public Connection addConnection(String acount, ChannelHandlerContext ctx) {
        final Connection c = new Connection(acount, ctx);
        connections.put(acount, c);
        return c;
    }

    /**
     * 从集合中获取一个连接
     *
     * @param acount
     * @return
     */
    public Connection getConnection(String acount) {
        return connections.get(acount);
    }

    /**
     * 从集合中删除一个连接
     *
     * @param c
     */
    public void removeConnection(Connection c) {
        Connection conn = connections.remove(c.getAcount());
    }

    private static class ConnectionManagerHolder {
        private static final ConnectionManager instance = new ConnectionManager();
    }
}
