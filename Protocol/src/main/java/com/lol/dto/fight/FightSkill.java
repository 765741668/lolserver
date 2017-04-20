package com.lol.dto.fight;

public class FightSkill {
    private int code; //策划编码
    private int level; //等级
    private int nextLevel; //学习需要角色等级
    private int time; //冷却时间--ms
    private String name; //技能名称
    private float range; //释放距离
    private String info; //技能描述
    private SkillTarget target; //技能伤害目标类型
    private SkillType type; //技能释放类型

    public FightSkill() {
    }

    public FightSkill(int code, int level, int nextLevel, int time, String name, float range, String info,
                      SkillTarget target, SkillType type) {
        this.code = code;
        this.level = level;
        this.nextLevel = nextLevel;
        this.time = time;
        this.name = name;
        this.range = range;
        this.info = info;
        this.target = target;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(int nextLevel) {
        this.nextLevel = nextLevel;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public SkillTarget getTarget() {
        return target;
    }

    public void setTarget(SkillTarget target) {
        this.target = target;
    }

    public SkillType getType() {
        return type;
    }

    public void setType(SkillType type) {
        this.type = type;
    }
}