package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.Protocol;
import com.lol.SelectProtocol;
import com.lol.fwk.dao.bean.player.Player;
import com.lol.dto.SelectModel;
import com.lol.fwk.buffer.GameDownBuffer;
import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.channel.GameRoomChannel;
import com.lol.fwk.channel.GameRoomChannelManager;
import com.lol.fwk.handler.GameProcessor;
import com.lol.fwk.protobuf.MessageDownProto;
import com.lol.fwk.util.Utils;
import com.lol.tool.EventUtil;
import com.lol.tool.ScheduleUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
public class SelectRoomProcesor extends BaseProsesor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(SelectProcesor.class);

    public ConcurrentMap<Integer, SelectModel> teamOne = new ConcurrentHashMap<>();
    public ConcurrentMap<Integer, SelectModel> teamTwo = new ConcurrentHashMap<>();
    public java.util.ArrayList<Integer> readList = new java.util.ArrayList<>();
    //当前进入房间的人数
    private int enterCount = 0;
    //当前定时任务ID
    private int missionId = -1;

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

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
                String acount = buffer.getConnection().getAcount();
                logger.info("开始从选人房间[{}]移除玩家角色[{}]", getRoomIndex(),acount);
                leave(buffer);
                logger.info("移除完毕。");
                //通知房间其他人 房间解散了 回主界面去
                GameDownBuffer otherPlayerData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(),
                        SelectProtocol.DESTORY_BRO, null);
                GameRoomChannel roomChannel = GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex());
                if (roomChannel != null) {
                    roomChannel.broadcastRoom(otherPlayerData);
                }
                logger.info("开始解散选人房间[{}]。。。",getRoomIndex());
                EventUtil.destorySelect.accept(getRoomIndex());
                logger.info("选人房间[{}]解散完毕。",getRoomIndex());
                logger.info("取消选人完毕。");

                if(!isInFight()){
                    logger.info("玩家角色[{}]没有进入战斗房间，直接请求最终操作。",acount);
                    EventUtil.disconnect.accept(buffer.getConnection());
                }


        }

    }

    public final void init(List<Integer> teamOne, List<Integer> teamTwo) {
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
                logger.info("30秒内队伍中有玩家角色没有进入选人房间 开始解散房间");
                destory();
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
                        logger.info("房间[{}]全员选择英雄完毕,开始战斗", getRoomIndex());
                        startFight();
                    } else {
                        logger.info("30秒内有玩家角色没选英雄，开始解散房间");
                        destory();
                        logger.info("房间解散完毕");
                    }
                }, 30 * 1000);

            }
        }, 30 * 1000);
    }

    /**
     * 解散房间
     */
    private void destory() {
        try {
            logger.info("通知房间所有人 房间解散了 回主界面去");
            GameDownBuffer data = Utils.packgeDownData(Protocol.TYPE_SELECT_ROOM, getRoomIndex(),
                    SelectProtocol.DESTORY_BRO, null);
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);
        } catch (Exception e) {
            logger.error("通知房间所有人 失败",e);
        }

        logger.info("开始清除选人房间[{}]",getRoomIndex());
        EventUtil.destorySelect.accept(getRoomIndex());
        logger.info("选人房间清除[{}]完毕",getRoomIndex());
        //当前有定时任务 则进行关闭
        logger.info("开始关闭定时任务[{}]",missionId);
        if (missionId != -1) {
            ScheduleUtil.getInstance().removeMission(missionId);
        }
        logger.info("定时任务[{}]已关闭",missionId);

    }

    private void ready(GameUpBuffer buffer) {
        //判断玩家是否在房间里
        if (!isEntered(buffer)) {
            return;
        }
        int playerId = getPlayerIdByConnection(buffer.getConnection());
        //判断玩家是否已准备
        if (readList.contains(playerId)) {
            return;
        }
        SelectModel sm;

        MessageDownProto.SelectModel.Builder selectModel = MessageDownProto.SelectModel.newBuilder();
        MessageDownProto.SelectDownBody.Builder selectData = MessageDownProto.SelectDownBody.newBuilder();
        //获取玩家选择数据模型
        if (teamOne.containsKey(playerId)) {
            sm = teamOne.get(playerId);
        } else {
            sm = teamTwo.get(playerId);
        }
        //没选择英雄 不让准备
        if (sm.getHero() != -1) {
            //设为已选状态 并通知其他人
            sm.setReady(true);
            try {

                selectModel.setEnter(sm.isEnter());
                selectModel.setHero(sm.getHero());
                selectModel.setReady(sm.isReady());
                selectModel.setNickName(sm.getNickName());
                selectModel.setPlayerId(sm.getPlayerId());

                selectData.setSelect(selectModel);

                GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(),
                        SelectProtocol.READY_BRO, selectData.build());
                GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);
            } catch (Exception e) {
                logger.error("玩家角色[{}]选择了英雄[{}] 并通知其他玩家角色失败",playerId,sm.getHero(),e);
            }
            //添加进准备列表
            readList.add(playerId);
            if (readList.size() >= teamOne.size() + teamTwo.size()) {
                logger.info("最后一名玩家角色准备就绪，开始战斗。。。");
                startFight();
            }
        }
    }

    private void startFight() {
        logger.info("战斗开始了，清除 [等等进入匹配计时] 和 [选人计时] 定时任务。");
        if (missionId != -1) {
            ScheduleUtil.getInstance().removeMission(missionId);
            missionId = -1;
        }
        logger.info("清除 [等等进入匹配计时] 和 [选人计时] 定时任务完毕。");

        //TODO::通知战斗模块 创建战斗房间
        logger.info("开始创建战斗房间。");
        EventUtil.createFight.create((SelectModel[]) teamOne.values().toArray(), (SelectModel[]) teamTwo.values().toArray(), null);
        logger.info("战斗房间创建完毕");

        GameDownBuffer data = Utils.packgeDownData(Protocol.TYPE_SELECT_ROOM, getRoomIndex(), SelectProtocol.FIGHT_BRO, null);

        try {
            logger.info("向房间[{}]内所有玩家角色广播，战斗开始通知。",getRoomIndex());
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);
        } catch (Exception e) {
            logger.info("向房间[{}]内所有玩家角色广播战斗开始通知失败",getRoomIndex());
        }
        //TODO::通知选择房间管理器 销毁当前房间
        logger.info("战斗房间已创建，开始清除选择房间[{}]。",getRoomIndex());
        EventUtil.destorySelect.accept(getRoomIndex());
        logger.info("清除选择房间[{}]完毕。",getRoomIndex());
    }

    private void talk(GameUpBuffer buffer) throws Exception {
        //判断玩家是否在房间里
        if (!isEntered(buffer)) {
            return;
        }
        Player player = getPlayerByConnection(buffer.getConnection());
        String talks = buffer.getBody().getSelect().getTalk();
        logger.info("玩家角色[{}]说：{}", player.getName(), talks);

        MessageDownProto.SelectDownBody.Builder selectData = MessageDownProto.SelectDownBody.newBuilder();
        selectData.setTalkRes(player.getName() + ":" + talks);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.TALK_BRO, selectData.build());

        //队伍聊天模式（默认）
        if (buffer.getBody().getSelect().getIsTeamTalk()) {
            List<Channel> channels;
            if (teamOne.containsKey(player.getId())) {
                channels = teamOne.keySet()
                        .stream()
                        .filter(id->id != player.getId())
                        .map(playerId ->
                                getConnectionByPlayerId(playerId).getChannel())
                        .collect(Collectors.toList());
            } else {
                channels = teamTwo.keySet()
                        .stream()
                        .map(playerId ->
                                getConnectionByPlayerId(playerId).getChannel())
                        .collect(Collectors.toList());
            }

            if (channels.isEmpty()) {
                logger.info("聊天频道列表为空");
            } else {
                GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex())
                        .broadcastRoom(data, channels);
            }
        } else {
            //全员聊天模式
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex())
                    .broadcastRoom(data, buffer.getConnection().getChannel());
        }
    }

    private void select(GameUpBuffer buffer) throws Exception {
        logger.info("玩家角色[{}]开始选人", buffer.getConnection().getAcount());
        //判断玩家是否在房间里
        if (!isEntered(buffer)) {
            return;
        }
        Player player = getPlayerByConnection(buffer.getConnection());
        //判断玩家是否拥有此英雄
        int selectedHero = buffer.getBody().getSelect().getSelect();
        List heroList = Arrays.asList(player.getHerolist().split(","));
        if (!heroList.contains(selectedHero)) {
            logger.info("没有此英雄：{}", selectedHero);
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
                    logger.info("英雄：{} 已被选", selectedHero);
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
                    logger.info("英雄：{} 已被选", selectedHero);
                    return;
                }
            }
            SelectModel selectModel = teamTwo.get(player.getId());
            selectModelPro.setPlayerId(selectModel.getPlayerId());
            selectModelPro.setNickName(selectModel.getNickName());
            selectModelPro.setReady(selectModel.isReady());
            selectModelPro.setEnter(selectModel.isEnter());
        }

        logger.info("选择了英雄：{}", selectedHero);
        //选择成功 通知房间所有人变更数据
        selectModelPro.setHero(selectedHero);
        selectData.setSelect(selectModelPro);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), SelectProtocol.SELECT_BRO, selectData.build());
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);

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
            logger.info("玩家角色[{}]确认进入选人房间", buffer.getConnection().getAcount());
            enterCount++;
        }
        //进入成功 发送房间信息给进入的玩家角色 并通知在房间内的其他玩家角色 有人进入了
        MessageDownProto.SelectRoom.Builder selectRoom = MessageDownProto.SelectRoom.newBuilder();
        MessageDownProto.SelectDownBody.Builder selectData = MessageDownProto.SelectDownBody.newBuilder();

        //TODO : 此类功能 整合到Utils工具类
        teamOne.values().parallelStream().forEach(model -> {
            MessageDownProto.SelectModel.Builder selectModel = MessageDownProto.SelectModel.newBuilder();
            selectModel.setEnter(model.isEnter());
            selectModel.setHero(model.getHero());
            selectModel.setReady(model.isReady());
            selectModel.setNickName(model.getNickName());
            selectModel.setPlayerId(model.getPlayerId());

            selectRoom.addTeamOne(selectModel);
        });

        teamTwo.values().parallelStream().forEach(model -> {
            MessageDownProto.SelectModel.Builder selectModel = MessageDownProto.SelectModel.newBuilder();
            selectModel.setEnter(model.isEnter());
            selectModel.setHero(model.getHero());
            selectModel.setReady(model.isReady());
            selectModel.setNickName(model.getNickName());
            selectModel.setPlayerId(model.getPlayerId());

            selectRoom.addTeamOne(selectModel);
        });

        selectData.setSelectRoom(selectRoom);

        //发送到时当前连接
        GameDownBuffer currentData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(),
                SelectProtocol.ENTER_SRES, selectData.build());
        buffer.getConnection().writeDown(currentData);


        //发送到除当前外的连接
        selectData.clearSelectRoom();
        selectData.setPlayerId(playerId);
        GameDownBuffer otherPlayerData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(),
                SelectProtocol.ENTER_EXBRO, selectData.build());


        GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex())
                .broadcastRoom(otherPlayerData, buffer.getConnection().getChannel());
    }

}
