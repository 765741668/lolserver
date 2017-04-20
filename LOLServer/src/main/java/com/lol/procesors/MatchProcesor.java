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
import com.lol.service.BizFactory;
import com.lol.service.IPlayerService;
import com.lol.tool.EventUtil;
import com.lol.util.Utils;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */

public class MatchProcesor implements GameProcessor {

    private IPlayerService playerBiz = BizFactory.playerBiz;

    /**
     * 多线程处理类中  防止数据竞争导致脏数据  使用线程安全字典
     * 玩家所在匹配房间映射
     */
    private ConcurrentHashMap<Integer, Integer> userRoom = new ConcurrentHashMap<>();
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
    private AtomicInteger index = new AtomicInteger();

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

        switch (buffer.getCmd()) {
            case MatchProtocol.ENTER_CREQ:
                enter(buffer);
                break;
            case MatchProtocol.LEAVE_CREQ:
                leave(buffer.getConnection());
                break;
        }
    }

    private void leave(Connection connection) {

        //取出用户唯一ID
        int userId = playerBiz.getPlayerId(connection);
        System.out.println("用户取消匹配" + userId);
        //判断用户是否有房间映射关系
        if (!userRoom.containsKey(userId)) {
            return;
        }
        //获取用户所在房间ID
        int roomId = userRoom.get(userId);
        //判断是否拥有此房间
        if (roomMap.containsKey(roomId)) {
            MatchRoom room = roomMap.get(roomId);
            //根据用户所在的队伍 进行移除
            if (room.teamOne.contains(userId)) {
                room.teamOne.remove(userId);
            } else if (room.teamTwo.contains(userId)) {
                room.teamTwo.remove(userId);
            }
            //移除用户与房间之间的映射关系
            userRoom.remove(userId, roomId);
            //如果当前用户为此房间最后一人 则移除房间 并丢进缓存队列
            if (room.teamOne.size() + room.teamTwo.size() == 0) {
                roomMap.remove(roomId, room);
                cache.push(room);
            }
        }
    }

    private void enter(GameUpBuffer buffer) {

        int userId = playerBiz.getPlayerId(buffer.getConnection());
        System.out.println("用户开始匹配" + userId);
        //判断玩家当前是否正在匹配队列中
        if (!userRoom.containsKey(userId)) {
            MatchRoom room = null;
            boolean isenter = false;
            //当前是否有等待中的房间
            if (roomMap.size() > 0) {
                //遍历当前所有等待中的房间
                for (MatchRoom item : roomMap.values()) {
                    //如果没满员
                    if (item.teamMax * 2 > item.teamOne.size() + item.teamTwo.size()) {
                        room = item;
                        //如果队伍1没有满员 则进入队伍1 否则进入队伍2
                        if (room.teamOne.size() < room.teamMax) {
                            room.teamOne.add(userId);
                        } else {
                            room.teamTwo.add(userId);
                        }
                        //添加玩家与房间的映射关系
                        isenter = true;
                        userRoom.put(userId, room.id);
                        break;
                    }
                }
                if (!isenter) {
                    //走到这里 说明等待中的房间全部满员了
                    if (cache.size() > 0) {
                        cache.pop();
                        room.teamOne.add(userId);
                        roomMap.put(room.id, room);
                        userRoom.put(userId, room.id);
                    } else {
                        room = new MatchRoom();
                        room.id = index.getAndIncrement();
                        room.teamOne.add(userId);
                        roomMap.put(room.id, room);
                        userRoom.put(userId, room.id);
                    }
                }

            } else {
                //没有等待中的房间
                //直接从缓存列表中找出可用房间 或者创建新房间
                if (cache.size() > 0) {
                    cache.pop();
                    room.teamOne.add(userId);
                    roomMap.put(room.id, room);
                    userRoom.put(userId, room.id);
                } else {
                    room = new MatchRoom();
                    room.id = index.getAndIncrement();
                    room.teamOne.add(userId);
                    roomMap.put(room.id, room);
                    userRoom.put(userId, room.id);
                }

            }
            //不管什么方式进入房间 ，判断房间是否满员，满了就开始选人并将当前房间丢进缓存队列
            if (room.teamOne.size() == room.teamTwo.size() && room.teamOne.size() == room.teamMax) {
                // 这里通知选人模块 开始选人了
                EventUtil.createSelect.create(room.teamOne, room.teamTwo, buffer);
                GameDownBuffer data = Utils.packgeDownData(Protocol.TYPE_MATCH, 0, MatchProtocol.ENTER_SELECT_BRO, null);

                //TODO:: 检查登陆连接绑定的标识与玩家标识是否一致
                room.teamOne.stream().forEach(id -> playerBiz.getConnection(id).writeDown(data));
                room.teamTwo.stream().forEach(id -> playerBiz.getConnection(id).writeDown(data));

                //移除玩家与房间映射
                room.teamOne.forEach(userRoom::remove);
                room.teamTwo.forEach(userRoom::remove);
                //重置房间数据 供下次使用
                room.teamOne.clear();
                room.teamTwo.clear();
                //将房间从等待房间表中移除
                roomMap.remove(room.id);
                //将房间丢进缓存表 供下次使用
                cache.push(room);
            }

        }
    }

}
