package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.Protocol;
import com.lol.SelectProtocol;
import com.lol.buffer.GameUpBuffer;
import com.lol.channel.GameRoomChannelManager;
import com.lol.core.GameBoss;
import com.lol.handler.GameProcessor;
import com.lol.service.BizFactory;
import com.lol.service.IPlayerService;
import com.lol.tool.EventUtil;
import com.lol.util.Utils;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */

public class SelectProcesor extends BaseProsesor implements GameProcessor {

    private IPlayerService playerBiz = BizFactory.playerBiz;
    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程同步Map
     * 玩家所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> userRoom = new ConcurrentHashMap<>();
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
    private AtomicInteger index = new AtomicInteger(0);

    //TODO : 调用
    SelectProcesor() {

        EventUtil.createSelect = this::create;
        EventUtil.destorySelect = this::destory;
    }

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

        if (SelectProtocol.DESTORY_BRO == buffer.getCmd()) {
            colse(buffer);
        } else {
            int userId = playerBiz.getPlayerId(buffer.getConnection());
            if (userRoom.containsKey(userId)) {
                int roomId = userRoom.get(userId);
                if (roomMap.containsKey(roomId)) {
                    GameUpBuffer data = Utils.packgeUpData(buffer.getConnection(), Protocol.TYPE_SELECT_ROOM,
                            buffer.getArea(), buffer.getCmd(), buffer.getBuffer());
                    GameBoss.getInstance().getProcessor().process(data);
                }
            }
        }
    }

    private void colse(GameUpBuffer buffer) {
        int userId = playerBiz.getPlayerId(buffer.getConnection());
        //判断当前玩家是否有房间
        if (userRoom.containsKey(userId)) {
            int roomId = 0;
            //移除并获取玩家所在房间
            userRoom.remove(userId);
            if (roomMap.containsKey(roomId)) {
                //通知
                GameUpBuffer data = Utils.packgeUpData(buffer.getConnection(), Protocol.TYPE_SELECT_ROOM, buffer.getArea(),
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
            room = new SelectRoomProcesor();
            //添加唯一ID
            buffer = Utils.packgeUpData(buffer.getConnection(), buffer.getMsgType(), index.getAndIncrement(),
                    SelectProtocol.SELECT_INIT, buffer.getBody());
        }
        GameBoss.getInstance().getProcessor().process(buffer);
        //房间数据初始化
        EventUtil.initSelectRoom.init(teamOne, teamTwo, buffer);
//        room.init(teamOne, teamTwo,buffer);
        //绑定映射关系
        for (int item : teamOne) {
            userRoom.put(item, buffer.getArea());
        }

        for (int item : teamTwo) {
            userRoom.put(item, buffer.getArea());
        }
        roomMap.put(buffer.getArea(), room);
    }

    public final void destory(int roomId) {
        //TODO : chek logic
        SelectRoomProcesor room = roomMap.remove(roomId);
        if (room != null) {
            //移除角色和房间之间的绑定关系
            room.teamOne.keySet().forEach(userRoom::remove);
            room.teamTwo.keySet().forEach(userRoom::remove);

            GameRoomChannelManager.getInstance().getRoomChannel(roomId).clearRoomConnection();

            room.readList.clear();
            room.teamOne.clear();
            room.teamTwo.clear();
            //将房间丢进堆栈顶部缓存队列 供下次选择使用
            cache.push(room);
        }
    }

}
