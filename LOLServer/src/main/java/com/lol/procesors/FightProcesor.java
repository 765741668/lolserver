package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.FightProtocol;
import com.lol.Protocol;
import com.lol.buffer.GameUpBuffer;
import com.lol.core.GameBoss;
import com.lol.dto.SelectModel;
import com.lol.handler.GameProcessor;
import com.lol.service.IPlayerService;
import com.lol.tool.EventUtil;
import com.lol.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */

public class FightProcesor extends BaseProsesor implements GameProcessor {

    @Autowired
    private IPlayerService playerService;
    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程安全MAP
     * 玩家所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> userRoom = new ConcurrentHashMap<>();
    /**
     * 房间id与模型映射
     */
    private ConcurrentHashMap<Integer, FightRoomProcesor> roomMap = new ConcurrentHashMap<>();
    /**
     * 回收利用过的房间对象再次利用，减少gc性能开销
     */
    private Stack<FightRoomProcesor> cache = new Stack<>();
    /**
     * 房间ID自增器
     */
    private AtomicInteger index = new AtomicInteger();

    public FightProcesor() {
        EventUtil.createFight = this::create;
        EventUtil.destoryFight = this::destory;
    }

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

        if (FightProtocol.DESTORY_FIGHT == buffer.getCmd()) {
            /**判断玩家是否在某场战斗中
             */
            if (userRoom.containsKey(playerService.getPlayerId(buffer.getConnection()))) {
                //通知
                GameUpBuffer data = Utils.packgeUpData(buffer.getConnection(), Protocol.TYPE_FIGHT_ROOM, buffer.getArea(),
                        FightProtocol.DESTORY_FIGHT, buffer.getBuffer());
                GameBoss.getInstance().getProcessor().process(data);
            }
        } else {
            //通知
            GameUpBuffer data = Utils.packgeUpData(buffer.getConnection(), Protocol.TYPE_FIGHT_ROOM, buffer.getArea(),
                    buffer.getCmd(), buffer.getBuffer());
            GameBoss.getInstance().getProcessor().process(data);
        }
    }

    /**
     * 战斗结束 房间移除
     *
     * @param roomId
     */
    public final void destory(int roomId) {
        //TODO : check logic
        FightRoomProcesor room = roomMap.remove(roomId);
        if (room != null) {
            //移除角色和房间之间的绑定关系
            room.teamOne.keySet().forEach(userRoom::remove);
            room.teamTwo.keySet().forEach(userRoom::remove);

            //将房间丢进缓存队列 供下次选择使用
            cache.push(room);
        }
    }

    /**
     * 创建战场
     *
     * @param teamOne
     * @param teamTwo
     */
    public final void create(SelectModel[] teamOne, SelectModel[] teamTwo, GameUpBuffer buffer) {
        //TODO : check logic
        FightRoomProcesor room = cache.pop();
        if (room == null) {
            room = new FightRoomProcesor();
            //添加唯一ID
            buffer = Utils.packgeUpData(buffer.getConnection(), buffer.getMsgType(), index.getAndIncrement(),
                    FightProtocol.FIGHT_INIT, buffer.getBody());
        }
        GameBoss.getInstance().getProcessor().process(buffer);
        //房间数据初始化
        EventUtil.initFightRoom.init(teamOne, teamTwo, buffer);
//        room.init(teamOne, teamTwo,buffer);
        //绑定映射关系
        for (SelectModel item : teamOne) {
            userRoom.put(item.getPlayerId(), buffer.getArea());
        }

        for (SelectModel item : teamTwo) {
            userRoom.put(item.getPlayerId(), buffer.getArea());
        }
        roomMap.put(buffer.getArea(), room);
    }

}
