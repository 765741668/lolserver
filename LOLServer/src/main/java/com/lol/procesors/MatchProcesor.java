package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.MatchProtocol;
import com.lol.Protocol;
import com.lol.buffer.GameDownBuffer;
import com.lol.buffer.GameUpBuffer;
import com.lol.core.Connection;
import com.lol.handler.GameProcessor;
import com.lol.logic.match.MatchRoom;
import com.lol.tool.EventUtil;
import com.lol.util.Utils;
import org.slf4j.Logger;
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
public class MatchProcesor extends BaseProsesor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(MatchProcesor.class);

    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程安全数据结构Map
     * 玩家角色所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> playerRoom = new ConcurrentHashMap<>();
    /**
     * 房间id与模型映射
     */
    private ConcurrentHashMap<Integer, MatchRoom> roomMap = new ConcurrentHashMap<>();
    /**
     * 回收利用过的房间对象再次利用，减少gc性能开销
     */
    private Stack<MatchRoom> cache = new Stack<>();
    /**
     * 房间ID自增器
     */
    private LongAdder index = new LongAdder();

    @Override
    public void process(GameUpBuffer buffer) throws Exception {
        Connection connection = buffer.getConnection();
        if (connection == null) {
            logger.info("客户端未登陆或已下线。忽略此次请求");
            return;
        }

        switch (buffer.getCmd()) {
            case MatchProtocol.ENTER_CREQ:
                enter(buffer);
                break;
            case MatchProtocol.LEAVE_CREQ:
                String acount = connection.getAcount();

                logger.info("玩家角色[{}]开始取消匹配。" ,acount);
                leave(buffer.getConnection());
                logger.info("匹配已取消。");

                if(!isInSelect()){
                    logger.info("玩家角色[{}]没有进入选人阶段，直接请求最终操作。",acount);
                    EventUtil.disconnect.accept(buffer.getConnection());
                }
                break;
        }
    }

    private void leave(Connection connection) {
        //取出用户唯一ID
        int playerId = playerService.getPlayerId(connection);
        //判断用户是否有房间映射关系
        if (!playerRoom.containsKey(playerId)) {
            return;
        }
        //获取用户所在房间ID
        int roomId = playerRoom.get(playerId);
        //判断是否拥有此房间
        if (roomMap.containsKey(roomId)) {
            MatchRoom room = roomMap.get(roomId);
            //根据用户所在的队伍 进行移除
            if (room.getTeamOne().contains(playerId)) {
                room.getTeamOne().remove(playerId);
            } else if (room.getTeamTwo().contains(playerId)) {
                room.getTeamTwo().remove(playerId);
            }
            //移除用户与房间之间的映射关系
            playerRoom.remove(playerId, roomId);
            //如果当前用户为此房间最后一人 则移除房间 并丢进缓存队列
            if (room.getTeamOne().size() + room.getTeamTwo().size() == 0) {
                roomMap.remove(roomId, room);
                cache.push(room);
            }
        }
    }

    private void enter(GameUpBuffer buffer) {

        int playerId = playerService.getPlayerId(buffer.getConnection());
        logger.info("玩家角色开始寻找匹配: {}", playerId);
        //判断玩家角色当前是否正在匹配队列中
        if (!playerRoom.containsKey(playerId)) {
            MatchRoom room = null;
            boolean isEnter = false;
            //当前是否有等待中的房间
            if (roomMap.size() > 0) {
                //遍历当前所有等待中的房间
                for (MatchRoom item : roomMap.values()) {
                    //如果没满员
                    if (item.getTeamMax() * 2 > item.getTeamOne().size() + item.getTeamTwo().size()) {
                        room = item;
                        //如果队伍1没有满员 则进入队伍1 否则进入队伍2
                        if (room.getTeamOne().size() < room.getTeamMax()) {
                            room.getTeamOne().add(playerId);
                        } else {
                            room.getTeamTwo().add(playerId);
                        }
                        //添加玩家角色与房间的映射关系
                        playerRoom.put(playerId, room.getId());
                        isEnter = true;
                        break;
                    }
                }
                if (!isEnter) {
                    //走到这里 说明等待中的房间全部满员了,从缓存列表中找可用房间
                    if (cache.size() > 0 ) {
                        room = cache.pop();
                        room.setTeamMax(buffer.getBody().getMatch().getTeamMax());
                        room.getTeamOne().add(playerId);
                        roomMap.put(room.getId(), room);
                        playerRoom.put(playerId, room.getId());
                    } else {
                        room = new MatchRoom();
                        index.increment();
                        room.setId(index.intValue());
                        room.setTeamMax(buffer.getBody().getMatch().getTeamMax());
                        room.getTeamOne().add(playerId);
                        roomMap.put(room.getId(), room);
                        playerRoom.put(playerId, room.getId());
                    }
                }

            } else {
                //没有等待中的房间
                //直接从缓存列表中找出可用房间(1v1 3v3 5v5) 或者创建新房间
                if (cache.size() > 0) {
                    room = cache.pop();
                    room.setTeamMax(buffer.getBody().getMatch().getTeamMax());
                    room.getTeamOne().add(playerId);
                    roomMap.put(room.getId(), room);
                    playerRoom.put(playerId, room.getId());
                } else {
                    room = new MatchRoom();
                    index.increment();
                    room.setId(index.intValue());
                    room.setTeamMax(buffer.getBody().getMatch().getTeamMax());
                    room.getTeamOne().add(playerId);
                    roomMap.put(room.getId(), room);
                    playerRoom.put(playerId, room.getId());
                }
            }

            setInMatch(true);

            //不管什么方式进入房间 ，判断房间是否满员，满了就开始选人并将当前房间丢进缓存队列
            if (room.getTeamOne().size() == room.getTeamTwo().size() && room.getTeamOne().size() == room.getTeamMax()) {

                logger.info("寻找匹配成功，开始创建选人房间。");
                EventUtil.createSelect.create(room.getTeamOne(), room.getTeamTwo(), buffer);
                logger.info("选人房间创建完毕。");

                logger.info("开始发送匹配成功消息给对应玩家角色");
                GameDownBuffer data = Utils.packgeDownData(Protocol.TYPE_MATCH, 0, MatchProtocol.MATCH_COMPLETED, null);
                //TODO:: 检查登陆连接绑定的标识与玩家角色标识是否一致
                room.getTeamOne().parallelStream().forEach(id -> playerService.getConnection(id).writeDown(data));
                room.getTeamTwo().parallelStream().forEach(id -> playerService.getConnection(id).writeDown(data));
                logger.info("匹配成功消息发送完毕");


                logger.info("开始清除匹配数据。。。");
                //移除玩家角色与房间映射
                room.getTeamOne().forEach(playerRoom::remove);
                room.getTeamTwo().forEach(playerRoom::remove);
                //重置房间数据 供下次使用
                room.getTeamOne().clear();
                room.getTeamTwo().clear();
                room.setTeamMax(0);
                //将房间从等待房间表中移除
                roomMap.remove(room.getId());
                //将房间丢进缓存表 供下次使用
                logger.info("匹配数据清除完成。");
                cache.push(room);
            }
        }else{
            setInMatch(true);
        }
    }

}
