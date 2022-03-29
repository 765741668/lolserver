package com.lol.fwk.service.impl;


import com.lol.fwk.anotation.Cache;
import com.lol.fwk.anotation.Flush;
import com.lol.fwk.common.CacheConstant;
import com.lol.fwk.core.Connection;
import com.lol.fwk.entity.player.Player;
import com.lol.fwk.exception.ServiceException;
import com.lol.fwk.service.IAccountService;
import com.lol.fwk.service.IPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户事物处理
 */
@Service("playerService")
public class PlayerServiceImpl implements IPlayerService {

    private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    private IAccountService accountService;

    /**
     * 角色信息表
     */
    private Map<Integer, Player> idToModel = new HashMap<>();
    /**
     * 帐号与角色关系表
     */
    private Map<Integer, Integer> accToUid = new HashMap<>();

    private Map<Integer, Connection> idToConnection = new HashMap<>();
    private Map<Connection, Integer> connectionToId = new HashMap<>();

    public final int create(Connection connection, String name) throws ServiceException {
        int accountId = accountService.getAccountId(connection);
        if (accountId == -1) {
            logger.warn("帐号未登陆，获取帐号ID失败：{}",accountId);
            return -1;
        }
        if (accToUid.containsKey(accountId)) {
            logger.warn("当前帐号已经拥有该角色");
            return -2;
        }

        Player player = new Player();
        player.setName(name);
        player.setAcountid(accountId);
        //初始化8个英雄
        //TODO:英雄表
        player.setHerolist("1,2,3,4,5,6,7,8");

        try {
            //TODO
            logger.info("新增玩家角色成功");
        } catch (Exception e) {
            throw new ServiceException("新增玩家角色失败", e);
        }

        //创建成功 进行帐号ID和用户ID的绑定
        accToUid.put(accountId, player.getId());
        //创建成功 进行用户ID和用户模型的绑定
        idToModel.put(player.getId(), player);

        return 0;
    }

    @Override
    public int getPlayerId(Connection connection) {

        Player player = getPlayerByConnection(connection);
        if (player == null) {
            return -1;
        }
        return player.getId();
    }

    @Cache(prefix= CacheConstant.PLYAER,expiration=60)
    public Player getByAcount(Connection connection) {
        //帐号是否登陆 获取帐号ID
        int acountId = accountService.getAccountId(connection);
        if (acountId == -1) {
            logger.warn("帐号未否登陆，获取帐号ID失败.");
            return null;
        }

        return getByacountId(acountId);

    }

    public final Player getPlayerById(int playerId) {
        return idToModel.get(playerId);
    }

    public final Player online(Connection connection) {
        int acountId = accountService.getAccountId(connection);
        if (acountId == -1) {
            return null;
        }
        Player player = getByacountId(acountId);
        if (idToConnection.containsKey(player.getId())) {
            //TODO
            return player;
        }

        idToConnection.put(player.getId(), connection);
        connectionToId.put(connection, player.getId());

        return player;
    }

    @Flush(prefix= CacheConstant.PLYAER)
    public void offline(Connection connection) {
        if (connectionToId.containsKey(connection)) {
            if (idToConnection.containsKey(connectionToId.get(connection))) {
                idToConnection.remove(connectionToId.get(connection));
            }
            connectionToId.remove(connection);
        }
    }

    public final Connection getConnection(int playerId) {
        return idToConnection.get(playerId);
    }

    public final Player getByacountId(int accId) {
        if (!accToUid.containsKey(accId)) {
            return null;
        }
        return idToModel.get(accToUid.get(accId));

    }

    public final Player getPlayerByConnection(Connection connection) {
        if (!connectionToId.containsKey(connection)) {
            return null;
        }
        return idToModel.get(connectionToId.get(connection));
    }

}