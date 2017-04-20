package com.lol.constans;


public class SkillLevelData {
    public int level; //学习等级
    public int time; //冷却时间
    public int mp; //耗蓝
    public float range; //攻击范围

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