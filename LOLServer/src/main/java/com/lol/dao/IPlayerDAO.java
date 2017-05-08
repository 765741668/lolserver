package com.lol.dao;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:25
 */

import com.lol.dao.bean.Player;
import com.lol.db.DAOException;

import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 2017/4/17
 * 19:25
 */

public interface IPlayerDAO {

    /**
     * 根据条件集合得到一个玩家角色实体对象
     *
     * @param condiction
     * @return
     */
    Player getPlayerByCondiction(Map condiction) throws DAOException;

    /**
     * 根据条件集合得到 玩家角色对象列表
     *
     * @param condiction
     * @return
     */
    List<Player> queryPlayersByCondiction(Map condiction) throws DAOException;

    /**
     * 保存玩家角色对象
     *
     * @param player
     */
    void savePlayer(Player player) throws DAOException;

    /**
     * 更新玩家角色对象
     *
     * @param player
     * @return
     */
    void updatePlayer(Player player) throws DAOException;

    /**
     * 根据ID删除玩家角色对象
     *
     * @param id
     * @return
     */
    void deletePlayer(int id) throws DAOException;
}
