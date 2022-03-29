package com.lol.fwk.buffer;

import com.lol.dto.AcountInfoDTO;
import com.lol.fwk.core.Connection;
import com.lol.fwk.protobuf.MessageUpProto;
import io.netty.channel.ChannelHandlerContext;
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

    private ChannelHandlerContext ctx;

    /**
     * 接收数据包
     */
    private MessageUpProto.MessageUp buffer;

    public GameUpBuffer(MessageUpProto.MessageUp buffer, Connection connection) {
        this.connection = connection;
        this.buffer = buffer;
    }

    public GameUpBuffer(MessageUpProto.MessageUp buffer, ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.buffer = buffer;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
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

    @Override
    public String toString() {
        return "GameUpBuffer{" +
                "connection=" + connection +
                ", buffer=" + buffer +
                '}';
    }
}
