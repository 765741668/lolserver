package com.lol.constans;


import com.lol.dto.fight.SkillTarget;
import com.lol.dto.fight.SkillType;

public class SkillDataModel {
    private int code;
    private SkillLevelData[] levels; //技能升级等级
    private String name; // 技能名称
    private String info; //技能说明信息
    private SkillTarget target; //技能目标类型
    private SkillType type; //技能释放方式

    public SkillDataModel() {
    }

    public SkillDataModel(int code, String name, String info, SkillType type, SkillTarget target,
                          SkillLevelData[] levels) {
        this.code = code;
        this.levels = levels;
        this.name = name;
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

    public SkillLevelData[] getLevels() {
        return levels;
    }

    public void setLevels(SkillLevelData[] levels) {
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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