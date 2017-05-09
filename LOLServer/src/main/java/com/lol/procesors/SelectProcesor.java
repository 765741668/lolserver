package com.lol.procesors;/**
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
import com.lol.fwk.util.Utils;
import com.lol.tool.EventUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class SelectProcesor extends BaseProsesor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(SelectProcesor.class);
    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程同步Map
     * 玩家角色所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> playerRoom = new ConcurrentHashMap<>();
    /**
     * 房间id与模型映射
     */
    private ConcurrentHashMap<Integer, SelectRoomProcesor> roomMap = new ConcurrentHashMap<>();
    /**
     * 回收利用过的房间对象再次利用，减少gc性能开销
     */
    private Stack<SelectRoomProcesor> cache = new Stack<>();
    /**
     * 房间ID自增器
     */
    private LongAdder index = new LongAdder();

    public SelectProcesor() {

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

        if (SelectProtocol.DESTORY_BRO == buffer.getCmd()) {
            colse(buffer);
        } else {
            int playerI = playerService.getPlayerId(connection);
            if (playerRoom.containsKey(playerI)) {
                int roomName = playerRoom.get(playerI);
                if (roomMap.containsKey(roomName)) {
                    GameUpBuffer data = Utils.packgeUpData(connection, Protocol.TYPE_SELECT_ROOM,
                            buffer.getArea(), buffer.getCmd(), buffer.getBuffer());
                    GameBoss.getInstance().getProcessor().process(data);
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
                GameUpBuffer data = Utils.packgeUpData(connection, Protocol.TYPE_SELECT_ROOM, buffer.getArea(),
                        buffer.getCmd(), buffer.getBuffer());
                GameBoss.getInstance().getProcessor().process(data);
            }
        }
    }

    public final void create(List<Integer> teamOne, List<Integer> teamTwo, GameUpBuffer buffer) {
        //TODO : chek logic
        //移除堆栈顶部的对象，并作为此函数的值返回该对象。
        SelectRoomProcesor room = cache.pop();
        if (room == null) {
            logger.warn("选人房间为空，初始化新房间。");
            room = new SelectRoomProcesor();
            //添加唯一ID
            index.increment();
            room.setRoomIndex(index.intValue());
            buffer = Utils.packgeUpData(buffer.getConnection(), buffer.getMsgType(), index.intValue(),
                    SelectProtocol.SELECT_INIT, buffer.getBody());
        }
        GameBoss.getInstance().getProcessor().process(buffer);
        //房间数据初始化
        logger.info("开始初始化选人房间。。。");
        EventUtil.initSelectRoom.init(teamOne, teamTwo);
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
        SelectRoomProcesor room = roomMap.remove(roomId);
        if (room != null) {
            //移除角色和房间之间的绑定关系
            room.teamOne.keySet().forEach(playerRoom::remove);
            room.teamTwo.keySet().forEach(playerRoom::remove);

            GameRoomChannelManager.getInstance().getRoomChannel(roomId).clearRoomConnection();

            room.readList.clear();
            room.teamOne.clear();
            room.teamTwo.clear();
            //将房间丢进堆栈顶部缓存队列 供下次选择使用
            cache.push(room);
        }
    }

}
