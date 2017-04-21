package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  19:25
 */

import com.lol.buffer.GameUpBuffer;
import com.lol.channel.GameRoomChannelManager;
import com.lol.core.Connection;
import com.lol.dao.bean.Player;
import com.lol.protobuf.MessageUpProto;
import com.lol.service.IPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 19:25
 */

public class BaseProsesor {

    private Logger logger = LoggerFactory.getLogger(BaseProsesor.class);

    @Autowired
    private IPlayerService playerService;

    private int area;

    /**
     * 用户进入当前子模块 添加连接
     *
     * @param buffer
     * @return
     */
    public boolean preEnter(GameUpBuffer buffer) {

        return GameRoomChannelManager.getInstance().addRoomChannel(getArea())
                .addRoomConnection(buffer.getConnection());
    }

    /**
     * 通过连接对象获取用户
     *
     * @param connection
     * @return
     */
    public final Player getPlayerByConnection(Connection connection) {
        return playerService.getPlayerByConnection(connection);
    }

    /**
     * 通过ID获取用户
     *
     * @param id
     * @return
     */
    public final Player getPlayerById(int id) {
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

    //获取房间名
    public long getRoomName(GameUpBuffer buffer) {
        long name = 0;
        MessageUpProto.SelectUpBody select = buffer.getBody().getSelect();
        MessageUpProto.FightUpBody fight = buffer.getBody().getFight();
        if (select != null) {
            name = select.getRoomName();
        } else if (fight != null) {
            name = fight.getRoomName();
        }
        return name;
    }

    /**
     * 用户是否在此子模块
     *
     * @param buffer
     * @return
     */
    protected boolean isEntered(GameUpBuffer buffer) {
        //TODO:: check logic
        return GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer))
                .isEnteredRoom(buffer.getConnection());
    }

    /**
     * 用户离开当前子模块
     *
     * @param buffer
     * @return
     */
    public final boolean leave(GameUpBuffer buffer) {
        return GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer))
                .removeRoomConnection(buffer.getConnection());
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }
}
