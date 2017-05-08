package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.FightProtocol;
import com.lol.buffer.GameDownBuffer;
import com.lol.buffer.GameUpBuffer;
import com.lol.channel.GameRoomChannelManager;
import com.lol.constans.*;
import com.lol.dto.SelectModel;
import com.lol.dto.fight.*;
import com.lol.handler.GameProcessor;
import com.lol.protobuf.MessageDownProto;
import com.lol.protobuf.MessageUpProto;
import com.lol.tool.EventUtil;
import com.lol.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
public class FightRoomProcesor extends BaseProsesor implements GameProcessor {

    private Logger logger = LoggerFactory.getLogger(SelectProcesor.class);

    public Map<Integer, AbsFightModel> teamOne = new HashMap<>();
    public Map<Integer, AbsFightModel> teamTwo = new HashMap<>();

    private List<Integer> off = new ArrayList<>();

    private ArrayList<Integer> enterList = new ArrayList<>();

    private int heroCount;

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

        switch (buffer.getCmd()) {
            case FightProtocol.ENTER_CREQ:
                logger.info("加载完成，进入游戏（需要数据）。。。。");
                enterBattle(buffer);
                break;
            case FightProtocol.MOVE_CREQ:
                move(buffer);
                break;
            case FightProtocol.ATTACK_CREQ:
                attack(buffer);
                break;
            case FightProtocol.DAMAGE_CREQ:
                damage(buffer);
                break;
            case FightProtocol.SKILL_UP_CREQ:
                skillUp(buffer);
                break;
            case FightProtocol.SKILL_CREQ:
                skill(buffer);
                break;
            case FightProtocol.FIGHT_INIT:
                EventUtil.initFightRoom = this::init;
                break;
            case FightProtocol.DESTORY_FIGHT:
                logger.info("开始从战斗房间[{}]移除玩家角色[{}]", getRoomIndex(),buffer.getConnection().getAcount());
                leave(buffer);
                logger.info("移除完毕。");
                int playerId = getPlayerIdByConnection(buffer.getConnection());
                if (teamOne.containsKey(playerId) || teamTwo.containsKey(playerId)) {
                    if (!off.contains(playerId)) {
                        off.add(playerId);
                    }
                }

                //TODO: 玩家下线 自动回基地 回血
                if (off.size() == heroCount) {
                    logger.info("所有玩家角色已断开链接，开始解散战斗房间[{}]。。。",getRoomIndex());
                    EventUtil.destoryFight.accept(getRoomIndex());
                    logger.info("战斗房间[{}]解散完毕。",getRoomIndex());
                }

                EventUtil.disconnect.accept(buffer.getConnection());
                break;

        }
    }

    public final void init(SelectModel[] teamOne, SelectModel[] teamTwo) {
        heroCount = teamTwo.length + teamOne.length;

        this.teamOne.clear();
        this.teamTwo.clear();
        off.clear();
        logger.info("开始初始化英雄数据。。。");
        for (SelectModel item : teamOne) {
            this.teamOne.put(item.getPlayerId(), create(item, 1));
        }
        for (SelectModel item : teamTwo) {
            this.teamTwo.put(item.getPlayerId(), create(item, 2));
        }
        logger.info("初始化英雄数据完毕");

        logger.info("开始实例化队伍1建筑，预留 ID段 -1到-10为队伍1建筑。");
        for (int i = -1; i >= -4; i--) {
            this.teamOne.put(i, createBuild(i, Math.abs(i), 1));
        }
        logger.info("队伍1建筑实例化完毕");

        logger.info("开始实例化队伍2建筑，预留 ID段 -11到-20为队伍2建筑。");
        for (int i = -11; i >= -14; i--) {
            this.teamTwo.put(i, createBuild(i, Math.abs(i) - 10, 2));
        }
        logger.info("队伍2建筑实例化完毕");
        enterList.clear();
    }

    private FightBuildModel createBuild(int id, int code, int team) {
        BuildDataModel data = BuildData.buildMap.get(code);
        FightBuildModel model = new FightBuildModel(id, code, data.getHp(), data.getHp(), data.getAtk(),
                data.getDef(), data.isReborn(),data.getRebornTime(), data.isInitiative(), data.isInfrared(),
                data.getName());
        model.setType(ModelType.BUILD);
        model.setTeam(team);
        return model;
    }

    private FightPlayerModel create(SelectModel model, int team) {
        FightPlayerModel player = new FightPlayerModel();
        player.setId(team);
        player.setCode(model.getHero());
        player.setType(ModelType.HUMAN);
        player.setName(getPlayer(model.getPlayerId()).getName());
        player.setExp(0);
        player.setLevel(1);
        player.setFree(1);
        player.setMoney(0);
        player.setTeam(team);
        //从配置表里 去出对应的英雄数据
        HeroDataModel data = HeroData.heroMap.get(model.getHero());
        player.setHp(data.getHpBase());
        //TODO : check hp or mp
        player.setMaxHp(data.getHpBase());
        player.setAtk(data.getAtkBase());
        player.setDef(data.getDefBase());
        player.setaSpeed(data.getaSpeed());
        player.setSpeed(data.getSpeed());
        player.setaRange(data.getRange());
        player.setEyeRange(data.getEyeRange());
        player.setSkills(initSkill(data.getSkills()));
        player.setEqus(new int[3]);
        return player;
    }

    private FightSkill[] initSkill(int[] value) {
        FightSkill[] skills = new FightSkill[value.length];

        for (int i = 0; i < value.length; i++) {
            int skillCode = value[i];
            SkillDataModel data = SkillData.skillMap.get(skillCode);
            SkillLevelData levelData = data.getLevels()[0];
            FightSkill skill = new FightSkill(skillCode, 0, levelData.getLevel(), levelData.getTime(),
                    data.getName(), levelData.getRange(), data.getInfo(), data.getTarget(), data.getType());
            skills[i] = skill;
        }
        return skills;
    }

    private void skill(GameUpBuffer buffer) throws Exception {
        MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();
        MessageDownProto.SkillAtkModel.Builder skillAtkModelDown = MessageDownProto.SkillAtkModel.newBuilder();

        MessageUpProto.SkillAtkModel skillAtkModelUp = buffer.getBuffer().getBody().getFight().getSkillAtk();
        skillAtkModelDown.setSkill(skillAtkModelUp.getSkill());
        skillAtkModelDown.setTarget(skillAtkModelUp.getTarget());
        skillAtkModelDown.setType(skillAtkModelUp.getType());

        List<Float> positions = skillAtkModelUp.getPositionList();
        for (int i = 0;  i < positions.size(); i++){
            skillAtkModelDown.setPosition(i,positions.get(i));
        }

        //更新玩家角色ID
        //TODO: 是否每次释放技能都更新
        skillAtkModelDown.setPlayerId(getPlayerIdByConnection(buffer.getConnection()));
        fight.setSkillAtk(skillAtkModelDown.build());

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.ATTACK_BRO, fight.build());
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);
    }

    private void skillUp(GameUpBuffer buffer) {
        int skillUp = buffer.getBody().getFight().getAttackOrSkillUp();
        int playerId = getPlayerIdByConnection(buffer.getConnection());
        FightPlayerModel player;
        if (teamOne.containsKey(playerId)) {
            player = (FightPlayerModel) teamOne.get(playerId);
        } else {
            player = (FightPlayerModel) teamTwo.get(playerId);
        }

        if (player.getFree() > 0) {
            //遍历角色技能列表 判断是否有此技能
            for (FightSkill item : player.getSkills()) {
                if (item.getCode() == skillUp) {
                    //判断玩家角色等级 是否达到技能提升等级
                    //TODO: 并且潜能点大于0
                    if (item.getNextLevel() != -1 && item.getNextLevel() <= player.getLevel() && player.getFree() > 0) {
                        player.setFree(player.getFree() - 1);
                        int level = item.getLevel() + 1;
                        SkillLevelData data = SkillData.skillMap.get(skillUp).getLevels()[level];
                        item.setNextLevel(data.getLevel());
                        item.setRange(data.getRange());
                        item.setTime(data.getTime());
                        item.setLevel(level);

                        MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();
                        MessageDownProto.FightSkill.Builder fightSkill = MessageDownProto.FightSkill.newBuilder();
                        MessageDownProto.FightSkill.SkillTarget skillTarget = MessageDownProto.FightSkill.SkillTarget
                                .valueOf(item.getTarget().getCode());
                        MessageDownProto.FightSkill.SkillType skillType = MessageDownProto.FightSkill.SkillType
                                .valueOf(item.getType().getCode());

                        fightSkill.setCode(item.getCode());
                        fightSkill.setLevel(level);
                        fightSkill.setNextLevel(data.getLevel());
                        fightSkill.setTime(data.getTime());
                        fightSkill.setName(item.getName());
                        fightSkill.setRange(data.getRange());
                        fightSkill.setInfo(item.getInfo());
                        fightSkill.setTarget(skillTarget);
                        fightSkill.setType(skillType);

                        fight.setFightSkill(fightSkill);

                        GameDownBuffer downData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(),
                                FightProtocol.SKILL_UP_SRES, fight.build());
                        buffer.getConnection().writeDown(downData);
                    }
                    return;
                }
            }
        }
    }

    private void damage(GameUpBuffer buffer) throws Exception {
        MessageUpProto.DamageInfo damage = buffer.getBody().getFight().getDamage();
        MessageDownProto.DamageInfo.Builder damageInfo = MessageDownProto.DamageInfo.newBuilder();
        MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();

        int playerId = getPlayerIdByConnection(buffer.getConnection());
        AbsFightModel atkModel;
        int skillLevel = 0;
        //判断攻击者是玩家英雄 还是小兵
        if (damage.getPlayerId() >= 0) {
            if (damage.getPlayerId() != playerId) {
                return;
            }
            logger.debug("攻击者类型为玩家角色，ID：{}",playerId);
            atkModel = getPlayer(playerId);
            if (damage.getSkill() > 0) {
                skillLevel = ((atkModel instanceof FightPlayerModel) ? atkModel : null) != null ?
                        ((FightPlayerModel)atkModel).skillLevel(damage.getSkill()) : 0;
                if (skillLevel == -1) {
                    return;
                }
            }
            logger.info("技能等级：{}",skillLevel);
        } else {
            //TODO:
            logger.debug("攻击者类型为小兵或中立怪");
            atkModel = getPlayer(playerId);
        }
        //获取技能算法
        //循环获取目标数据 和攻击者数据 进行伤害计算 得出伤害数值
        if (!SkillProcessMap.has(damage.getSkill())) {
            return;
        }
        ISkill skill = SkillProcessMap.get(damage.getSkill());
        List<Integer[]> damages = new ArrayList<>();
        for (MessageUpProto.Target item : damage.getTargetList()) {
            AbsFightModel target = getPlayer(item.getTarget(0));
            skill.damage(skillLevel, atkModel, target, damages);
            if (target.getHp() == 0) {
                switch (target.getType()) {
                    case HUMAN:
                        if (target.getId() > 0) {
                            //TODO
                            //击杀英雄
                            //启动定时任务 指定时间之后发送英雄复活信息 并且将英雄数据设置为满状态
                        } else {
                            //TODO
                            //击杀小兵
                            //移除小兵数据
                        }
                        break;
                    case BUILD:
                        //打破了建筑 给钱

                        break;
                }
            }
        }

        damages.parallelStream().forEach(d -> {
            damageInfo.addTarget(MessageDownProto.Target.newBuilder().addAllTarget(Arrays.asList(d)));
        });

        fight.setDamageInfo(damageInfo);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.DAMAGE_BRO, fight.build());
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);

    }

    private AbsFightModel getPlayer(int playerId) {
        if (teamOne.containsKey(playerId)) {
            return teamOne.get(playerId);
        }
        return teamTwo.get(playerId);
    }

    private void attack(GameUpBuffer buffer) throws Exception {
        MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();
        MessageDownProto.AttackInfo.Builder attackInfo = MessageDownProto.AttackInfo.newBuilder();
        int targetId = buffer.getBody().getFight().getAttackOrSkillUp();

        attackInfo.setPlayerId(getPlayerIdByConnection(buffer.getConnection()));
        attackInfo.setTargetId(targetId);
        fight.setAttackInfo(attackInfo);

        logger.debug("广播全员，玩家角色[{}]攻击目标[{}]", buffer.getConnection().getAcount(),targetId);
        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.ATTACK_BRO, fight.build());
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);

    }

    private void move(GameUpBuffer buffer) throws Exception {
        MessageUpProto.MoveInfo move = buffer.getBody().getFight().getMove();
        MessageDownProto.MoveInfo.Builder moveInfo = MessageDownProto.MoveInfo.newBuilder();
        MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();

        moveInfo.setPlayerId(getPlayerIdByConnection(buffer.getConnection()));
        moveInfo.setX(move.getX());
        moveInfo.setY(move.getY());
        moveInfo.setZ(move.getZ());

        fight.setMoveInfo(moveInfo);

        logger.debug("广播全员，玩家角色[{}]开始移动(x,y,z): {},{},{}",
                buffer.getConnection().getAcount(), move.getX(), move.getY(), move.getZ());
        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.MOVE_BRO, fight.build());
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);
    }

    private void enterBattle(GameUpBuffer buffer) throws Exception {
        int playerId = getPlayerIdByConnection(buffer.getConnection());
        boolean isEntered = isEntered(buffer);
        if (isEntered) {
            logger.info("玩家角色已经进入战斗房间,忽略此次请求");
            return;
        }

        super.preEnter(buffer);
        logger.info("战斗房间[{}]新增玩家角色[{}]",getRoomIndex(),buffer.getConnection().getAcount());

        if (!enterList.contains(playerId)) {
            enterList.add(playerId);
        }

        if (enterList.size() == heroCount) {
            logger.info("所有人准备了 广播战斗房间[{}]信息",getRoomIndex());
            FightRoomModel room = new FightRoomModel();
            room.setTeamOne((AbsFightModel[]) teamOne.values().toArray());
            room.setTeamTwo((AbsFightModel[]) teamTwo.values().toArray());

            MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();
            MessageDownProto.FightRoomModel.Builder fightRoomModel = MessageDownProto.FightRoomModel.newBuilder();

            teamOne.values().parallelStream().forEach(model -> {
                MessageDownProto.FightModel.Builder fightModel = MessageDownProto.FightModel.newBuilder();
                MessageDownProto.FightModel.ModelType modelType = MessageDownProto.FightModel.ModelType
                        .valueOf(model.getType().getCode());

                fightModel.setARange(model.getaRange());
                fightModel.setASpeed(model.getaSpeed());
                fightModel.setAtk(model.getAtk());
                fightModel.setCode(model.getCode());
                fightModel.setDef(model.getDef());
                fightModel.setEyeRange(model.getEyeRange());
                fightModel.setHp(model.getHp());
                fightModel.setId(model.getId());
                fightModel.setMaxHp(model.getMaxHp());
                fightModel.setName(model.getName());
                fightModel.setSpeed(model.getSpeed());
                fightModel.setTeam(model.getTeam());
                fightModel.setType(modelType);

                fightRoomModel.addTeamOne(fightModel);
            });

            teamTwo.values().parallelStream().forEach(model -> {
                MessageDownProto.FightModel.Builder fightModel = MessageDownProto.FightModel.newBuilder();
                MessageDownProto.FightModel.ModelType modelType = MessageDownProto.FightModel.ModelType
                        .valueOf(model.getType().getCode());
                fightModel.setARange(model.getaRange());
                fightModel.setASpeed(model.getaSpeed());
                fightModel.setAtk(model.getAtk());
                fightModel.setCode(model.getCode());
                fightModel.setDef(model.getDef());
                fightModel.setEyeRange(model.getEyeRange());
                fightModel.setHp(model.getHp());
                fightModel.setId(model.getId());
                fightModel.setMaxHp(model.getMaxHp());
                fightModel.setName(model.getName());
                fightModel.setSpeed(model.getSpeed());
                fightModel.setTeam(model.getTeam());
                fightModel.setType(modelType);

                fightRoomModel.addTeamTwo(fightModel);
            });

            fight.setFightRoom(fightRoomModel);

            GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.START_BRO, fight.build());
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomIndex()).broadcastRoom(data);
        }

    }

}
