package com.lol.constans;


public class SkillLevelData {
    private int level; //学习等级(升级的话为下一级需要学习的等级)
    private int time; //冷却时间
    private int mp; //耗蓝
    private float range; //攻击范围

    public SkillLevelData() {
    }

    public SkillLevelData(int level, int time, int mp, float range) {
        this.level = level;
        this.time = time;
        this.mp = mp;
        this.range = range;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}