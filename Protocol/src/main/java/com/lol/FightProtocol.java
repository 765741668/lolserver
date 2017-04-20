package com.lol;

public class FightProtocol {
    /**
     * 加载完成，进入游戏，需要数据了
     */
    public static final int ENTER_CREQ = 0;
    /**
     * 所有人加载完成，开始游戏，给予数据
     */
    public static final int START_BRO = 1;
    /**
     * 申请移动
     */
    public static final int MOVE_CREQ = 2;
    /**
     * 群发移动
     */
    public static final int MOVE_BRO = 3;
    /**
     * 申请升级技能
     */
    public static final int SKILL_UP_CREQ = 4;
    /**
     * 返回升级结果
     */
    public static final int SKILL_UP_SRES = 5;
    /**
     * 申请攻击
     */
    public static final int ATTACK_CREQ = 6;
    /**
     * 群发播放攻击
     */
    public static final int ATTACK_BRO = 7;
    /**
     * 伤害事件申请
     */
    public static final int DAMAGE_CREQ = 8;
    /**
     * 群发伤害
     */
    public static final int DAMAGE_BRO = 9;
    /**
     * 申请释放技能
     */
    public static final int SKILL_CREQ = 10;
    /**
     * 群发播放技能
     */
    public static final int SKILL_BRO = 11;
    /**
     * 关闭战斗事件
     */
    public static final int DESTORY_FIGHT = 12;

    /**
     * 初始化战斗房间事件
     */
    public static final int FIGHT_INIT = 13;

}