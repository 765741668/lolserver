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

import java.util.*;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */

public class FightRoomProcesor extends BaseProsesor implements GameProcessor {

    public Map<Integer, AbsFightModel> teamOne = new HashMap<>();
    public Map<Integer, AbsFightModel> teamTwo = new HashMap<>();

    private List<Integer> off = new ArrayList<>();

    private ArrayList<Integer> enterList = new ArrayList<>();

    private int heroCount;

    @Override
    public void process(GameUpBuffer buffer) throws Exception {

        switch (buffer.getCmd()) {
            case FightProtocol.ENTER_CREQ:
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
                leave(buffer);
                int userId = getPlayerIdByConnection(buffer.getConnection());
                if (teamOne.containsKey(userId) || teamTwo.containsKey(userId)) {
                    if (!off.contains(userId)) {
                        off.add(userId);
                    }
                }
                if (off.size() == heroCount) {
                    EventUtil.destoryFight = (x) -> buffer.getArea();
                }
                break;

        }
    }

    public final void init(SelectModel[] teamOne, SelectModel[] teamTwo, GameUpBuffer buffer) {
        super.setArea(buffer.getArea());
        heroCount = teamTwo.length + teamOne.length;

        this.teamOne.clear();
        this.teamTwo.clear();
        off.clear();
        //初始化英雄数据
        for (SelectModel item : teamOne) {
            this.teamOne.put(item.getPlayerId(), create(item, 1));
        }
        for (SelectModel item : teamTwo) {
            this.teamTwo.put(item.getPlayerId(), create(item, 2));
        }
        /**实例化队伍一的建筑
         预留 ID段 -1到-10为队伍1建筑
         */
        for (int i = -1; i >= -3; i--) {
            this.teamOne.put(i, createBuild(i, Math.abs(i), 1));
        }
        /**实例化队伍二的建筑
         预留 ID段 -11到-20为队伍2建筑
         */
        for (int i = -11; i >= -13; i--) {
            this.teamTwo.put(i, createBuild(i, Math.abs(i) - 10, 2));
        }
        enterList.clear();
    }

    private FightBuildModel createBuild(int id, int code, int team) {
        BuildDataModel data = BuildData.buildMap.get(code);
        FightBuildModel model = new FightBuildModel(id, code, data.hp, data.hp, data.atk, data.def, data.reborn, data.rebornTime, data.initiative, data.infrared, data.name);
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
        player.setHp(data.hpBase);
        //TODO : check hp or mp
        player.setMaxMp(data.mpBase);
        player.setAtk(data.atkBase);
        player.setDef(data.defBase);
        player.setaSpeed(data.aSpeed);
        player.setSpeed(data.speed);
        player.setaRange(data.range);
        player.setEyeRange(data.eyeRange);
        player.setSkills(initSkill(data.skills));
        player.setEqus(new int[3]);
        return player;
    }

    private FightSkill[] initSkill(int[] value) {
        FightSkill[] skills = new FightSkill[value.length];

        for (int i = 0; i < value.length; i++) {
            int skillCode = value[i];
            SkillDataModel data = SkillData.skillMap.get(skillCode);
            SkillLevelData levelData = data.levels[0];
            FightSkill skill = new FightSkill(skillCode, 0, levelData.level, levelData.time, data.name, levelData.range, data.info, data.target, data.type);
            skills[i] = skill;
        }
        return skills;
    }

    private void skill(GameUpBuffer buffer) throws Exception {
        MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();
        MessageDownProto.SkillAtkModel.Builder skillAtkModel = MessageDownProto.SkillAtkModel.newBuilder();

        skillAtkModel.setPlayerId(getPlayerIdByConnection(buffer.getConnection()));
        fight.setSkillAtk(skillAtkModel);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.ATTACK_BRO, fight);
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);
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
                    //判断玩家等级 是否达到技能提升等级
                    if (item.getNextLevel() != -1 && item.getNextLevel() <= player.getLevel()) {
                        player.setFree(player.getFree() - 1);
                        int level = item.getLevel() + 1;
                        SkillLevelData data = SkillData.skillMap.get(skillUp).levels[level];
                        item.setNextLevel(data.level);
                        item.setRange(data.range);
                        item.setTime(data.time);
                        item.setLevel(level);

//                        private int code; //策划编码
//                        private int level; //等级
//                        private int nextLevel; //学习需要角色等级
//                        private int time; //冷却时间--ms
//                        private String name; //技能名称
//                        private float range; //释放距离
//                        private String info; //技能描述
//                        private SkillTarget target; //技能伤害目标类型
//                        private SkillType type; //技能释放类型

                        MessageDownProto.FightSkill.Builder fightSkill = MessageDownProto.FightSkill.newBuilder();
                        MessageDownProto.FightSkill.SkillTarget skillTarget = MessageDownProto.FightSkill.SkillTarget
                                .valueOf(item.getTarget().getCode());
                        MessageDownProto.FightSkill.SkillType skillType = MessageDownProto.FightSkill.SkillType
                                .valueOf(item.getType().getCode());

                        fightSkill.setCode(item.getCode());
                        fightSkill.setLevel(level);
                        fightSkill.setNextLevel(data.level);
                        fightSkill.setTime(data.time);
                        fightSkill.setName(item.getName());
                        fightSkill.setRange(data.range);
                        fightSkill.setInfo(item.getInfo());
                        fightSkill.setTarget(skillTarget);
                        fightSkill.setType(skillType);

                        GameDownBuffer downData = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(),
                                FightProtocol.SKILL_UP_SRES, fightSkill);
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
            atkModel = getPlayer(playerId);
            if (damage.getSkill() > 0) {
                skillLevel = ((atkModel instanceof FightPlayerModel) ? atkModel : null) != null ?
                        ((FightPlayerModel) ((atkModel instanceof FightPlayerModel) ? atkModel : null))
                                .skillLevel(damage.getSkill()) : 0;
                if (skillLevel == -1) {
                    return;
                }
            }
        } else {
            //TODO:
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

        damages.stream().forEach(d -> {
            damageInfo.addTarget(MessageDownProto.Target.newBuilder().addAllTarget(Arrays.asList(d)));
        });

        fight.setDamageInfo(damageInfo);

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.DAMAGE_BRO, fight);
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);

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

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.ATTACK_BRO, fight);
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);

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

        GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.MOVE_BRO, fight);
        GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);
    }

    private void enterBattle(GameUpBuffer buffer) throws Exception {

        int userId = getPlayerIdByConnection(buffer.getConnection());
        if (isEntered(buffer)) {
            return;
        }
        super.preEnter(buffer);
        if (!enterList.contains(userId)) {
            enterList.add(userId);
        }
        //所有人准备了 发送房间信息
        if (enterList.size() == heroCount) {
            FightRoomModel room = new FightRoomModel();
            room.setTeamOne((AbsFightModel[]) teamOne.values().toArray());
            room.setTeamTwo((AbsFightModel[]) teamTwo.values().toArray());

            MessageDownProto.FightDownBody.Builder fight = MessageDownProto.FightDownBody.newBuilder();
            MessageDownProto.FightRoomModel.Builder fightRoomModel = MessageDownProto.FightRoomModel.newBuilder();

            teamOne.values().stream().forEach(model -> {
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

            teamTwo.values().stream().forEach(model -> {
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

            GameDownBuffer data = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), FightProtocol.START_BRO, fight);
            GameRoomChannelManager.getInstance().getRoomChannel(getRoomName(buffer)).broadcastRoom(data);
        }

    }

}
