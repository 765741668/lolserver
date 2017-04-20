package com.lol.dto.fight;

public class FightPlayerModel extends AbsFightModel {
    private int level; //等级
    private int exp; //经验
    private int free; //剩余潜能点
    private int money; //玩家经济
    private int[] equs; //玩家装备
    private FightSkill[] skills; //玩家拥有技能
    private int mp; //当前能量
    private int maxMp; //最大能量

    public final int skillLevel(int code) {
        for (FightSkill item : skills) {
            if (item.getCode() == code) {
                return item.getLevel();
            }
        }
        return -1;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int[] getEqus() {
        return equs;
    }

    public void setEqus(int[] equs) {
        this.equs = equs;
    }

    public FightSkill[] getSkills() {
        return skills;
    }

    public void setSkills(FightSkill[] skills) {
        this.skills = skills;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }
}