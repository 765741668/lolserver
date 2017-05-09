package com.lol.fwk.service.player.impl;


import com.lol.fwk.anotation.Cache;
import com.lol.fwk.anotation.Flush;
import com.lol.fwk.common.CacheConstant;
import com.lol.fwk.core.Connection;
import com.lol.fwk.dao.bean.player.Player;
import com.lol.fwk.dao.player.IPlayerDAO;
import com.lol.fwk.db.DAOException;
import com.lol.fwk.db.ServiceErrorCode;
import com.lol.fwk.db.ServiceException;
import com.lol.fwk.service.acount.IAcountService;
import com.lol.fwk.service.player.IPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户事物处理
 */
@Service("playerServiceImpl")
public class PlayerServiceImpl implements IPlayerService {

    private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    private IAcountService acountService;
    @Autowired
    private IPlayerDAO playerDAO;

    private Logger looger = LoggerFactory.getLogger(PlayerServiceImpl.class);


    /**
     * 用户ID和模型的映射表
     */
    private Map<Integer, Player> idToModel = new HashMap<>();
    /**
     * 帐号ID和角色ID之间的绑定
     */
    private Map<Integer, Integer> accToUid = new HashMap<>();

    private Map<Integer, Connection> idToConnection = new HashMap<>();
    private Map<Connection, Integer> connectionToId = new HashMap<>();

    public final int create(Connection connection, String name) throws ServiceException {
        int acountId = acountService.getAcountId(connection);
        if (acountId == -1) {
            logger.warn("帐号未登陆，获取帐号ID失败：{}",acountId);
            return -1;
        }
        init(acountId);
        if (accToUid.containsKey(acountId)) {
            logger.warn("当前帐号已经拥有该角色");
            return -2;
        }

        Player player = new Player();
        player.setName(name);
        player.setAcountid(acountId);
        //初始化8个英雄
        //TODO:英雄表
        player.setHerolist("1,2,3,4,5,6,7,8");

        try {
            playerDAO.savePlayer(player);
            logger.info("新增玩家角色成功");
        } catch (DAOException e) {
            throw new ServiceException(ServiceErrorCode.ADD.toString(), "新增玩家角色失败", e);
        }

        //创建成功 进行帐号ID和用户ID的绑定
        accToUid.put(acountId, player.getId());
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly=true)//容器不为这个方法开启事务
    public final Player getByAcount(Connection connection) {
        //帐号是否登陆 获取帐号ID
        int acountId = acountService.getAcountId(connection);
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
        int acountId = acountService.getAcountId(connection);
        if (acountId == -1) {
            return null;
        }
        Player player = getByacountId(acountId);
        if (idToConnection.containsKey(player.getId())) {
            return null;
        }

        idToConnection.put(player.getId(), connection);
        connectionToId.put(connection, player.getId());

        return player;
    }

    @Flush(prefix= CacheConstant.PLYAER)
//    Propagation.NOT_SUPPORTED,readOnly=true 容器不为这个方法开启事务
    @Transactional
    public final void offline(Connection connection) {
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
        init(accId);
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

    public final void init(int acountId) {

        if (accToUid.containsKey(acountId)) {
            return;
        }
        Map<String, Integer> condiction = new HashMap<>();
        condiction.put("acountId", acountId);

        Player player = null;
        try {
            player = playerDAO.getPlayerByCondiction(condiction);
        } catch (DAOException e) {
            looger.error(e.getMessage(), "根据acountId查询Player对象失败", e);
        }
        if (player != null) {
            //获取成功 进行帐号ID和用户ID的绑定
            accToUid.put(acountId, player.getId());
            //获取成功 进行用户ID和用户模型的绑定
            idToModel.put(player.getId(), player);
        }
    }
}