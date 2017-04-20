package com.lol.service.impl;


import com.lol.core.Connection;
import com.lol.dao.IPlayerDAO;
import com.lol.dao.bean.Player;
import com.lol.db.DAOException;
import com.lol.db.ServiceErrorCode;
import com.lol.db.ServiceException;
import com.lol.service.IAcountService;
import com.lol.service.IPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户事物处理
 */
@Service("playerServiceImpl")
public class PlayerServiceImpl implements IPlayerService {
    private Logger looger = LoggerFactory.getLogger(PlayerServiceImpl.class);
    @Autowired
    private IAcountService acountService;
    @Autowired
    private IPlayerDAO playerDAO;

    /**
     * 用户ID和模型的映射表
     */
    private Map<Integer, Player> idToModel = new HashMap<>();
    /**
     * 帐号ID和角色ID之间的绑定
     */
    private Map<Integer, Integer> accToUid = new HashMap<>();

    private Map<Integer, Connection> idToToken = new HashMap<>();
    private Map<Connection, Integer> connectionToId = new HashMap<>();

    public final boolean create(Connection connection, String name) throws ServiceException {
        //帐号是否登陆 获取帐号ID
        int acountId = acountService.getAcountId(connection);
        if (acountId == -1) {
            return false;
        }
        //判断当前帐号是否已经拥有角色
        init(acountId);
        if (accToUid.containsKey(acountId)) {
            return false;
        }

        Player player = new Player();
        player.setName(name);
        player.setAcountid(acountId);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            list.add(i);
        }
        player.setHerolist(list.toArray(new Integer[list.size()]));

        try {
            playerDAO.savePlayer(player);
        } catch (DAOException e) {
            throw new ServiceException(ServiceErrorCode.ADD.toString(), "新增Player对象失败", e);
        }

        //创建成功 进行帐号ID和用户ID的绑定
        accToUid.put(acountId, player.getId());
        //创建成功 进行用户ID和用户模型的绑定
        idToModel.put(player.getId(), player);

        return true;
    }

    @Override
    public int getPlayerId(Connection connection) throws ServiceException {

        Player player = getPlayerByConnection(connection);
        if (player == null) {
            return -1;
        }
        return player.getId();
    }

    public final Player getByAcount(Connection connection) throws ServiceException {
        //帐号是否登陆 获取帐号ID
        int acountId = acountService.getAcountId(connection);
        if (acountId == -1) {
            return null;
        }

        return getByacountId(acountId);

    }

    public final Player getPlayerById(int id) throws ServiceException {
        return idToModel.get(id);
    }

    public final Player online(Connection connection) throws ServiceException {
        int acountId = acountService.getAcountId(connection);
        if (acountId == -1) {
            return null;
        }
        Player user = getByacountId(acountId);
        if (idToToken.containsKey(user.getId())) {
            return null;
        }

        idToToken.put(user.getId(), connection);
        connectionToId.put(connection, user.getId());

        return user;
    }

    public final void offline(Connection connection) throws ServiceException {
        if (connectionToId.containsKey(connection)) {
            if (idToToken.containsKey(connectionToId.get(connection))) {
                idToToken.remove(connectionToId.get(connection));
            }
            connectionToId.remove(connection);
        }
    }

    public final Connection getConnection(int id) throws ServiceException {
        return idToToken.get(id);
    }

    public final Player getByacountId(int accId) {
        init(accId);
        if (!accToUid.containsKey(accId)) {
            return null;
        }

        return idToModel.get(accToUid.get(accId));

    }

    public final Player getPlayerByConnection(Connection connection) throws ServiceException {
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