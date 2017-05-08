package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  19:25
 */

import com.lol.buffer.GameUpBuffer;
import com.lol.channel.GameRoomChannelManager;
import com.lol.core.Connection;
import com.lol.dao.bean.Player;
import com.lol.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 19:25
 */

public class BaseProsesor {

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
    protected final Player getPlayerByConnection(Connection connection) {
        return playerService.getPlayerByConnection(connection);
    }

    /**
     * 通过ID获取用户
     *
     * @param id
     * @return
     */
    protected final Player getPlayerById(int id) {
        return playerService.getPlayerById(id);
    }

    /**
     * 通过连接对象 获取用户ID
     *
     * @param connection
     * @return
     */
    protected final int getPlayerIdByConnection(Connection connection) {
        Player player = playerService.getPlayerByConnection(connection);
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
        return playerService.getConnection(playerId);
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

    protected void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    protected int getRoomIndex() {
        return roomIndex;
    }

    public boolean isInSelect() {
        return isInSelect;
    }

    public void setInSelect(boolean isInSelect) {
        this.isInSelect = isInSelect;
    }

    public boolean isInMatch() {
        return isInMatch;
    }

    public void setInMatch(boolean isInMatch) {
        this.isInMatch = isInMatch;
    }

    public boolean isInFight() {
        return isInFight;
    }

    public void setInFight(boolean isInFight) {
        this.isInFight = isInFight;
    }
}
