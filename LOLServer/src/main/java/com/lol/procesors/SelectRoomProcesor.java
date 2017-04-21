package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.Protocol;
import com.lol.SelectProtocol;
import com.lol.buffer.GameDownBuffer;
import com.lol.buffer.GameUpBuffer;
import com.lol.channel.GameRoomChannelManager;
import com.lol.dao.bean.Player;
import com.lol.dto.SelectModel;
import com.lol.handler.GameProcessor;
import com.lol.protobuf.MessageDownProto;
import com.lol.tool.EventUtil;
import com.lol.tool.ScheduleUtil;
import com.lol.util.Utils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class SelectRoomProcesor extends BaseProsesor implements GameProcessor {

    public ConcurrentMap<Integer, SelectModel> teamOne = new ConcurrentHashMap<>();
    public ConcurrentMap<Integer, SelectModel> teamTwo = new ConcurrentHashMap<>();
    public java.util.ArrayList<Integer> readList = new java.util.ArrayList<>();
    //当前进入房间的人数
    private int enterCount = 0;
    //当前定时任务ID
    private int missionId = -1;

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

        long roomName = buffer.getBody().getSelect().getRoomName();
        switch (buffer.getCmd()) {
            case SelectProtocol.ENTER_CREQ:
                preEnter(buffer);
                enter(buffer);
                break;
            case SelectProtocol.SELECT_CREQ:
                select(buffer);
                break;
            case SelectProtocol.TALK_CREQ:
                talk(buffer);
                break;
            case SelectProtocol.READY_CREQ:
                ready(buffer);
                break;
            case SelectProtocol.SELECT_INIT:
                EventUtil.initSelectRoom = this::init;
                break;
            case SelectProtocol.DESTORY_BRO:
                //调用离开方法 让此连接不再接收网络消息
                leave(buffer);
                //通知房间其他人 房间解散了 回主界面去
                GameDownBuffer otherUserData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.DESTORY_BRO, null);
                GameRoomChannelManager.getInstance().getRoomChannel(roomName).broadcastRoom(otherUserData);
                //TODO:通知管理器 移除自身
                EventUtil.destorySelect.accept(buffer.getArea());
        }

    }

    public final void init(List<Integer> teamOne, List<Integer> teamTwo, GameUpBuffer buffer) {
        super.setArea(buffer.getArea());
        //初始化房间数据
        this.teamOne.clear();
        this.teamTwo.clear();
        enterCount = 0;
        for (int item : teamOne) {
            SelectModel select = new SelectModel();
            select.setPlayerId(item);
            select.setNickName(getPlayerById(item).getName());
            select.setHero(-1);
            select.setEnter(false);
            select.setReady(false);
            this.teamOne.put(item, select);
        }
        for (int item : teamTwo) {
            SelectModel select = new SelectModel();
            select.setPlayerId(item);
            select.setNickName(getPlayerById(item).getName());
            select.setHero(-1);
            select.setEnter(false);
            select.setReady(false);
            this.teamTwo.put(item, select);
        }

        //TODO : 线程30秒延迟
        //初始化完毕  开始定时任务  设定 30秒后没有进入选择界面的时候 直接解散此次匹配
        missionId = ScheduleUtil.getInstance().schedule(() ->
        {
            //30秒后判断进入情况 如果不是全员进入 解散房间
            if (enterCount < teamOne.size() + teamTwo.size()) {
                destory(buffer);
            } else {
                //再次启动定时任务 30秒内完成选人
                missionId = ScheduleUtil.getInstance().schedule(() ->
                {
                    //时间抵达30s 遍历判断 是否所有人都选择了英雄
                    boolean selectAll = true;
                    for (SelectModel item : this.teamOne.values()) {
                        if (item.getHero() == -1) {
                            selectAll = false;
                            break;
                        }
                    }
                    if (selectAll) {
                        for (SelectModel item : this.teamTwo.values()) {
                            if (item.getHero() == -1) {
                                selectAll = false;
                                break;
                            }
                        }
                    }
                    if (selectAll) {
                        //全部选了 只是有人没有点准备按钮  开始战斗
                        startFight(buffer);
                    } else {
                        //有人没选   解散房间
                        destory(buffer);
                    }
                }, 30 * 1000);

            }
        }, 30 * 1000);
    }

    /**
     * 解散房间
     */
    private void destory(GameUpBuffer buffer) {
        //通知房间所有人 房间解散了 回主界面去
        try {
            //TODO:: 二级协议 子模块 aera数值
            GameDownBuffer data = Utils.packgeDownData(Protocol.TYPE_SELECT_ROOM, -1, SelectProtocol.DESTORY_BRO, null);
            //TODO::房间名
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO:通知管理器 移除自身
        EventUtil.destorySelect.accept(buffer.getArea());
        //当前有定时任务 则进行关闭
        if (missionId != -1) {
            ScheduleUtil.getInstance().removeMission(missionId);
        }

    }

    private void ready(GameUpBuffer buffer) {
        //判断玩家是否在房间里
        if (!isEntered(buffer)) {
            return;
        }
        int userId = getPlayerIdByConnection(buffer.getConnection());
        //判断玩家是否已准备
        if (readList.contains(userId)) {
            return;
        }
        SelectModel sm = null;

        MessageDownProto.SelectModel.Builder selectModel = MessageDownProto.SelectModel.newBuilder();
        //获取玩家选择数据模型
        if (teamOne.containsKey(userId)) {
            sm = teamOne.get(userId);
        } else {
            sm = teamTwo.get(userId);
        }
        //没选择英雄 不让准备
        if (sm.getHero() == -1) {

        } else {
            //设为已选状态 并通知其他人
            sm.setReady(true);
            try {

                selectModel.setEnter(sm.isEnter());
                selectModel.setHero(sm.getHero());
                selectModel.setReady(sm.isReady());
                selectModel.setNickName(sm.getNickName());
                selectModel.setPlayerId(sm.getPlayerId());

                GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.READY_BRO, sm);
                GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);
            } catch (Exception e) {
                //TODO::env.log
                e.printStackTrace();
            }
            //添加进准备列表
            readList.add(userId);
            if (readList.size() >= teamOne.size() + teamTwo.size()) {
                //所有人都准备了 开始战斗
                startFight(buffer);
            }
        }
    }

    private void startFight(GameUpBuffer buffer) {
        if (missionId != -1) {
            ScheduleUtil.getInstance().removeMission(missionId);
            missionId = -1;
        }
        //TODO::通知战斗模块 创建战斗房间
        EventUtil.createFight.create((SelectModel[]) teamOne.values().toArray(), (SelectModel[]) teamTwo.values().toArray(), buffer);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.FIGHT_BRO, null);

        try {
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);
        } catch (Exception e) {
            //TODO: env.log
            e.printStackTrace();
        }
        //TODO::通知选择房间管理器 销毁当前房间
        EventUtil.destorySelect.accept(buffer.getArea());
    }

    private void talk(GameUpBuffer buffer) throws Exception {
        //判断玩家是否在房间里
        if (!isEntered(buffer)) {
            return;
        }
        Player player = getPlayerByConnection(buffer.getConnection());

        MessageDownProto.SelectDownBody.Builder selectData = MessageDownProto.SelectDownBody.newBuilder();
        selectData.setTalkRes(player.getName() + ":" + buffer.getBody().getSelect().getTalk());

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.TALK_BRO, selectData);

        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer))
                .broadcastRoom(data, buffer.getConnection().getChannel());


        /**队伍聊天模式
         */
//           if (teamOne.containsKey(player.id))
//           {
//               writeToUsers(teamOne.Keys.ToArray(), GetType(), GetArea(), SelectProtocol.TALK_BRO, player.name + ":" + value);
//           }
//           else {
//               writeToUsers(teamTwo.Keys.ToArray(), GetType(), GetArea(), SelectProtocol.TALK_BRO, player.name + ":" + value);
//           }
    }

    private void select(GameUpBuffer buffer) throws Exception {
        //判断玩家是否在房间里
        if (!isEntered(buffer)) {
            return;
        }
        Player player = getPlayerByConnection(buffer.getConnection());
        //判断玩家是否拥有此英雄
        int selectedHero = buffer.getBody().getSelect().getSelect();
        if (!Arrays.asList(player.getHerolist()).contains(selectedHero)) {
            GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.SELECT_SRES, null);
            buffer.getConnection().writeDown(data);
            return;
        }
        //判断英雄队友是否已选
        MessageDownProto.SelectModel.Builder selectModelPro = MessageDownProto.SelectModel.newBuilder();
        MessageDownProto.SelectDownBody.Builder selectData = MessageDownProto.SelectDownBody.newBuilder();
        if (teamOne.containsKey(player.getId())) {
            for (SelectModel item : teamOne.values()) {
                if (item.getHero() == selectedHero) {
                    return;
                }
            }
            SelectModel selectModel = teamOne.get(player.getId());
            selectModelPro.setPlayerId(selectModel.getPlayerId());
            selectModelPro.setNickName(selectModel.getNickName());
            selectModelPro.setReady(selectModel.isReady());
            selectModelPro.setEnter(selectModel.isEnter());

        } else {
            for (SelectModel item : teamTwo.values()) {
                if (item.getHero() == selectedHero) {
                    return;
                }
            }
            SelectModel selectModel = teamTwo.get(player.getId());
            selectModelPro.setPlayerId(selectModel.getPlayerId());
            selectModelPro.setNickName(selectModel.getNickName());
            selectModelPro.setReady(selectModel.isReady());
            selectModelPro.setEnter(selectModel.isEnter());
        }
        //选择成功 通知房间所有人变更数据
        selectModelPro.setHero(selectedHero);
        selectData.setSelect(selectModelPro);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.SELECT_BRO, selectData);
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);

    }

    private void enter(GameUpBuffer buffer) throws Exception {
        //判断用户所在的房间 并对其进入状态进行修改
        int playerId = getPlayerIdByConnection(buffer.getConnection());
        if (teamOne.containsKey(playerId)) {
            teamOne.get(playerId).setEnter(true);
        } else if (teamTwo.containsKey(playerId)) {
            teamTwo.get(playerId).setEnter(true);
        } else {
            return;
        }
        //判断用户是否已经在房间 不在则计算累加 否则无视
        if (isEntered(buffer)) {
            enterCount++;
        }
        //进入成功 发送房间信息给进入的玩家 并通知在房间内的其他玩家 有人进入了
        MessageDownProto.SelectRoom.Builder selectRoom = MessageDownProto.SelectRoom.newBuilder();
        MessageDownProto.SelectDownBody.Builder selectData = MessageDownProto.SelectDownBody.newBuilder();

        //TODO : 此类功能 整合到Utils工具类
        teamOne.values().stream().forEach(model -> {
            MessageDownProto.SelectModel.Builder selectModel = MessageDownProto.SelectModel.newBuilder();
            selectModel.setEnter(model.isEnter());
            selectModel.setHero(model.getHero());
            selectModel.setReady(model.isReady());
            selectModel.setNickName(model.getNickName());
            selectModel.setPlayerId(model.getPlayerId());

            selectRoom.addTeamOne(selectModel);
        });

        teamTwo.values().stream().forEach(model -> {
            MessageDownProto.SelectModel.Builder selectModel = MessageDownProto.SelectModel.newBuilder();
            selectModel.setEnter(model.isEnter());
            selectModel.setHero(model.getHero());
            selectModel.setReady(model.isReady());
            selectModel.setNickName(model.getNickName());
            selectModel.setPlayerId(model.getPlayerId());

            selectRoom.addTeamOne(selectModel);
        });

        selectData.setSelectRoom(selectRoom);

        GameDownBuffer currentData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.ENTER_SRES, selectData);

        selectData.clearSelectRoom();
        selectData.setPlayerId(playerId);
        GameDownBuffer otherUserData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.ENTER_EXBRO, playerId);

        //发送到时当前连接
        buffer.getConnection().writeDown(currentData);

        //发送到除当前外的连接
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer))
                .broadcastRoom(otherUserData, buffer.getConnection().getChannel());
    }

}
