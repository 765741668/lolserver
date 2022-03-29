package com.lol.fwk.service;


import com.lol.fwk.core.Connection;
import com.lol.fwk.entity.player.Player;

public interface IPlayerService {
    /**
     * 创建召唤师
     *
     * @param connection
     * @param name
     * @return
     */
    int create(Connection connection, String name);

    /**
     * 过连接对象 获取玩家ID
     *
     * @param connection
     * @return
     */
    int getPlayerId(Connection connection);

    /**
     * 获取连接对应的用户信息
     *
     * @param connection
     * @return
     */
    Player getPlayerByConnection(Connection connection);

    /**
     * 通过ID获取用户信息
     *
     * @param playerId
     * @return
     */
    Player getPlayerById(int playerId);

    /**
     * 用户上线
     *
     * @param connection
     * @return
     */
    Player online(Connection connection);

    /**
     * 用户下线
     *
     * @param connection
     */
    void offline(Connection connection);

    /**
     * 通过playerId获取连接对象
     *
     * @param playerId
     * @return
     */
    Connection getConnection(int playerId);

    /**
     * 通过帐号的连接对象获取 仅在初始登录验证角色时有效
     *
     * @param connection
     * @return
     */
    Player getByAcount(Connection connection);

}