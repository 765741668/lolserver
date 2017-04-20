package com.lol.dto.fight;

/**
 * 能够造成效果的单位类型
 */
public enum SkillTarget {
    SELF(0, "自身释放"),
    F_H(1, "友方英雄"),
    F_N_B(2, "友方非建筑单位"),
    F_ALL(3, "友方全体"),
    E_H(4, "敌方英雄"),
    E_N_B(5, "敌方非建筑"),
    E_S_N(6, "敌方和中立单位"),
    N_F_ALL(7, "非友方单位");

    private int code;
    private String desc;

    SkillTarget(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SkillTarget fromCode(int code) {
        for (SkillTarget c : SkillTarget.values()) {
            if (c.code == code) {
                return c;
            }
        }
        throw new IllegalArgumentException(code + "");
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFormattedDescptions(Object... args) {
        return String.format(desc, args);
    }
}