package com.lol.service;


import com.lol.core.Connection;
import com.lol.dao.bean.Player;
import com.lol.db.ServiceException;

public interface IPlayerService {
    /**
     * 创建召唤师
     *
     * @param connection
     * @param name
     * @return
     */
    boolean create(Connection connection, String name) throws ServiceException;

    /**
     * 过连接对象 获取玩家ID
     *
     * @param connection
     * @return
     */
    int getPlayerId(Connection connection) throws ServiceException;

    /**
     * 获取连接对应的用户信息
     *
     * @param connection
     * @return
     */
    Player getPlayerByConnection(Connection connection) throws ServiceException;

    /**
     * 通过ID获取用户信息
     *
     * @param id
     * @return
     */
    Player getPlayerById(int id) throws ServiceException;

    /**
     * 用户上线
     *
     * @param connection
     * @return
     */
    Player online(Connection connection) throws ServiceException;

    /**
     * 用户下线
     *
     * @param connection
     */
    void offline(Connection connection) throws ServiceException;

    /**
     * 通过id获取连接对象
     *
     * @param id
     * @return
     */
    Connection getConnection(int id) throws ServiceException;

    /**
     * 通过帐号的连接对象获取 仅在初始登录验证角色时有效
     *
     * @param connection
     * @return
     */
    Player getByAcount(Connection connection) throws ServiceException;

}