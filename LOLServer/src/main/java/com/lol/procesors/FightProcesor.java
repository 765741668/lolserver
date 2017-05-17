package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.FightProtocol;
import com.lol.Protocol;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.core.Connection;
import com.lol.fwk.core.GameBoss;
import com.lol.dto.SelectModel;
import com.lol.fwk.handler.GameProcessor;
import com.lol.tool.EventUtil;
import com.lol.fwk.util.Utils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class FightProcesor extends BaseProsesor implements GameProcessor {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SelectProcesor.class);

    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程安全MAP
     * 玩家所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> playerRoom = new ConcurrentHashMap<>();
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
    private LongAdder index = new LongAdder();

    public FightProcesor() {
        EventUtil.createFight = this::create;
        EventUtil.destoryFight = this::destory;
    }

    @Override
    public void process(GameUpBuffer buffer) {
        Connection connection = buffer.getConnection();
        if(connection == null){
            logger.info("客户端未登陆或已下线。忽略此次请求");
            return;
        }

        if (FightProtocol.DESTORY_FIGHT == buffer.getCmd()) {
            /**判断玩家角色是否在某场战斗中
             */
            boolean isInFight = playerRoom.containsKey(playerService.getPlayerId(connection));
            String acount = connection.getAcount();
            logger.info("玩家角色[{}]是否在某场战斗中: {}",acount, isInFight);
            if (isInFight) {
                //通知
                GameUpBuffer data = Utils.packgeUpData(connection, Protocol.TYPE_FIGHT_ROOM, buffer.getArea(),
                        FightProtocol.DESTORY_FIGHT, buffer.getBuffer());
                GameBoss.getInstance().getProcessor().process(data);
            }
        } else {
            //通知
            GameUpBuffer data = Utils.packgeUpData(connection, Protocol.TYPE_FIGHT_ROOM, buffer.getArea(),
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
            room.teamOne.keySet().forEach(playerRoom::remove);
            room.teamTwo.keySet().forEach(playerRoom::remove);

            //将房间丢进缓存队列 供下次选择使用
            cache.push(room);

            index.decrement();
            room.setRoomIndex(index.intValue());
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
            logger.warn("战斗房间为空，初始化新房间。");
            room = new FightRoomProcesor();
            //添加唯一ID
            index.increment();
            room.setRoomIndex(index.intValue());
            buffer = Utils.packgeUpData(buffer.getConnection(), buffer.getMsgType(), index.intValue(),
                    FightProtocol.FIGHT_INIT, buffer.getBody());
        }
        GameBoss.getInstance().getProcessor().process(buffer);
        logger.info("开始初始化战斗房间。。。");
        EventUtil.initFightRoom.init(teamOne, teamTwo);
        logger.info("战斗房间初始化完毕。");
        //绑定映射关系
        for (SelectModel item : teamOne) {
            playerRoom.put(item.getPlayerId(), room.getRoomIndex());
        }

        for (SelectModel item : teamTwo) {
            playerRoom.put(item.getPlayerId(), room.getRoomIndex());
        }

        setInFight(true);
        roomMap.put(room.getRoomIndex(), room);
    }

}
