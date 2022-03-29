package com.lol.logic;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  19:25
 */

import com.lol.fwk.entity.player.Player;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.channel.GameRoomChannelManager;
import com.lol.fwk.core.Connection;
import com.lol.fwk.service.IPlayerService;
import com.lol.server.Application;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 19:25
 */

public class BaseProcessor {

    @Autowired
    protected IPlayerService playerService;

    private int roomIndex;

    private boolean isInMatch;
    private boolean isInSelect;
    private boolean isInFight;

    /**
     * 用户进入当前子模块 添加连接
     *
     * @param buffer
     * @return
     */
    protected boolean preEnter(GameUpBuffer buffer) {
        return GameRoomChannelManager.getInstance().addRoomChannel(roomIndex)
                .addRoomConnection(buffer.getConnection());
    }

    /**
     * 通过连接对象获取用户
     *
     * @param connection
     * @return
     */
    protected Player getPlayerByConnection(Connection connection) {
        return Application.context.getBean(IPlayerService.class).getPlayerByConnection(connection);
    }

    /**
     * 通过ID获取用户
     *
     * @param id
     * @return
     */
    protected Player getPlayerById(int id) {
        return Application.context.getBean(IPlayerService.class).getPlayerById(id);
    }

    /**
     * 通过连接对象 获取用户ID
     *
     * @param connection
     * @return
     */
    protected int getPlayerIdByConnection(Connection connection) {
        Player player = Application.context.getBean(IPlayerService.class).getPlayerByConnection(connection);
        if (player == null) {
            return -1;
        }
        return player.getId();
    }

    /**
     * 通过playerId获取连接对象
     * @param playerId
     * @return
     */
    protected Connection getConnectionByPlayerId(int playerId){
        return Application.context.getBean(IPlayerService.class).getConnection(playerId);
    }

    /**
     * 用户是否在此子模块
     *
     * @param buffer
     * @return
     */
    protected boolean isEntered(GameUpBuffer buffer) {
        //TODO:: check logic
        return GameRoomChannelManager.getInstance().getRoomChannel(roomIndex)
                .isEnteredRoom(buffer.getConnection());
    }

    /**
     * 用户离开当前子模块
     *
     * @param buffer
     * @return
     */
    protected final void leave(GameUpBuffer buffer) {
            GameRoomChannelManager.getInstance().getRoomChannel(roomIndex)
                    .removeRoomConnection(buffer.getConnection());

    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    protected boolean isInSelect() {
        return isInSelect;
    }

    protected void setInSelect(boolean isInSelect) {
        this.isInSelect = isInSelect;
    }

    protected boolean isInMatch() {
        return isInMatch;
    }

    protected void setInMatch(boolean isInMatch) {
        this.isInMatch = isInMatch;
    }

    protected boolean isInFight() {
        return isInFight;
    }

    protected void setInFight(boolean isInFight) {
        this.isInFight = isInFight;
    }
}
