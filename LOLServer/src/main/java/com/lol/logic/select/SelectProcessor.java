package com.lol.logic.select;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.Protocol;
import com.lol.SelectProtocol;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.channel.GameRoomChannelManager;
import com.lol.fwk.core.Connection;
import com.lol.fwk.core.GameBoss;
import com.lol.fwk.handler.GameProcessor;
import com.lol.fwk.protobuf.MessageUpProto;
import com.lol.fwk.util.Utils;
import com.lol.logic.BaseProcessor;
import com.lol.server.Application;
import com.lol.tool.EventUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.LongAdder;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class SelectProcessor extends BaseProcessor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(SelectProcessor.class);
    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程同步Map
     * 玩家角色所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> playerRoom = new ConcurrentHashMap<>();
    /**
     * 房间id与模型映射
     */
    private ConcurrentHashMap<Integer, SelectRoomProcessor> roomMap = new ConcurrentHashMap<>();
    /**
     * 回收利用过的房间对象再次利用，减少gc性能开销
     */
    private ConcurrentLinkedDeque<SelectRoomProcessor> cache = new ConcurrentLinkedDeque<>();
    /**
     * 房间ID自增器
     */
    private LongAdder index = new LongAdder();

    public SelectProcessor() {

        EventUtil.createSelect = this::create;
        EventUtil.destorySelect = this::destory;
    }

    @Override
    public void process(GameUpBuffer buffer) throws Exception {
        Connection connection = buffer.getConnection();
        if(connection == null){
            logger.info("客户端未登陆或已下线。忽略此次请求");
            return;
        }
        ChannelHandlerContext ctx = connection.getChannelHandlerContext();
        MessageUpProto.SelectUpBody match = buffer.getBody().getSelect();
        logger.info("收到选人模块消息: {} ({})",match,ctx.channel().remoteAddress());
        if (SelectProtocol.DESTORY_BRO == buffer.getCmd()) {
            colse(buffer);
        } else {
            int playerI = playerService.getPlayerId(connection);
            if (playerRoom.containsKey(playerI)) {
                int roomName = playerRoom.get(playerI);
                if (roomMap.containsKey(roomName)) {
                    GameUpBuffer data = Utils.packageUpData(connection, Protocol.TYPE_SELECT_ROOM,
                            buffer.getArea(), buffer.getCmd(), buffer.getBuffer());
                    GameBoss.getInstance().getProcessor().pushToServer(data);
                }
            }
        }

    }

    private void colse(GameUpBuffer buffer) {
        Connection connection = buffer.getConnection();
        int playerId = playerService.getPlayerId(connection);
        //判断当前玩家是否有房间
        if (playerRoom.containsKey(playerId)) {
            //移除并获取玩家所在房间
            int roomName = playerRoom.remove(playerId);
            if (roomMap.containsKey(roomName)) {
                //通知
                GameUpBuffer data = Utils.packageUpData(connection, Protocol.TYPE_SELECT_ROOM, buffer.getArea(),
                        buffer.getCmd(), buffer.getBuffer());
                GameBoss.getInstance().getProcessor().pushToServer(data);
            }
        }
    }

    public final void create(List<Integer> teamOne, List<Integer> teamTwo, GameUpBuffer buffer) {
        //TODO : chek logic
        //移除堆栈顶部的对象，并作为此函数的值返回该对象。
        SelectRoomProcessor room = cache.poll();
        if (room == null) {
            logger.warn("选人房间为空，初始化新房间。");
            room = new SelectRoomProcessor();
            //添加唯一ID
            index.increment();
            room.setRoomIndex(index.intValue());
//            buffer = Utils.packgeUpData(buffer.getConnection(), buffer.getMsgType(), index.intValue(),
//                    SelectProtocol.SELECT_INIT, buffer.getBody());
        }
//        GameBoss.getInstance().getProcessor().pushToServer(buffer);
        //房间数据初始化
        logger.info("开始初始化选人房间。。。");
        Application.context.getBean(SelectRoomProcessor.class).init(teamOne, teamTwo);
//        EventUtil.initSelectRoom.init(teamOne, teamTwo);
        logger.info("选人房间初始化完毕，等待玩家角色确认进入房间");
        //绑定映射关系
        for (int item : teamOne) {
            playerRoom.put(item, room.getRoomIndex());
        }

        for (int item : teamTwo) {
            playerRoom.put(item, room.getRoomIndex());
        }

        setInSelect(true);
        roomMap.put(room.getRoomIndex(), room);
    }

    public final void destory(int roomId) {
        //TODO : chek logic
        SelectRoomProcessor room = roomMap.remove(roomId);
        if (room != null) {
            //移除角色和房间之间的绑定关系
            room.getTeamOne().keySet().forEach(playerRoom::remove);
            room.getTeamTwo().keySet().forEach(playerRoom::remove);

            GameRoomChannelManager.getInstance().getRoomChannel(roomId).clearRoomConnection();

            room.getReadList().clear();
            room.getTeamOne().clear();
            room.getTeamTwo().clear();
            //将房间丢进堆栈顶部缓存队列 供下次选择使用
            cache.push(room);

            index.decrement();
            room.setRoomIndex(index.intValue());
        }
    }

}
