package com.lol.buffer;

import com.lol.core.Connection;
import com.lol.dto.AcountInfoDTO;
import com.lol.protobuf.MessageUpProto;
import org.apache.commons.lang3.StringUtils;

/**
 * 上行消息类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameUpBuffer {

    /**
     * 客户端连接
     */
    private Connection connection;

    /**
     * 接收数据包
     */
    private MessageUpProto.MessageUp buffer;

    public GameUpBuffer(MessageUpProto.MessageUp buffer, Connection connection) {
        this.connection = connection;
        this.buffer = buffer;
    }


    public Connection getConnection() {
        return connection;
    }

    public MessageUpProto.MessageUp getBuffer() {
        return this.buffer;
    }

    /**
     * 一级协议 用于区分所属模块
     *
     * @return
     */
    public int getMsgType() {
        return buffer.getHeader().getMsgType();
    }

    /**
     * 二级协议 用于区分 模块下所属子模块
     *
     * @return
     */
    public int getArea() {
        return buffer.getHeader().getArea();
    }

    /**
     * 三级协议  用于区分当前处理逻辑功能
     *
     * @return
     */
    public int getCmd() {
        return buffer.getHeader().getCmd();
    }

    /**
     * 获取登陆用户数据
     *
     * @return
     */
    public AcountInfoDTO getAcountInfo() {
        AcountInfoDTO acount = new AcountInfoDTO();
        MessageUpProto.LoginUpBody acountInfo = buffer.getBody().getLogin();
        if (acountInfo == null) {
            return null;
        }
        if (!StringUtils.isEmpty(connection.getAcount())) {
            acount.setacount(connection.getAcount());
        }
        if (!StringUtils.isEmpty(acountInfo.getPassword())) {
            acount.setPassword(acountInfo.getPassword());
        }
        return acount;
    }

    /**
     * 获取玩家角色数据
     *
     * @return PlayerDTO
     */
    public String getPlayerNickName() {

        MessageUpProto.PlayerUpBody player = buffer.getBody().getPlayer();
        if (player == null) {
            return null;
        }
        return player.getNickName();
    }

    public MessageUpProto.MsgUpBody getBody() {
        return buffer.getBody();
    }

}
