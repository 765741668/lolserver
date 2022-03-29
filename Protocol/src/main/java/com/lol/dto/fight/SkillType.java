package com.lol.dto.fight;

/**
 * 技能释放方式
 */
public enum SkillType {
    SELF_CENTER(0, "以自身为中心进行释放"),
    TARGET_CENTER(1, "以目标为中心进行释放"),
    POSITION(2, "以鼠标点击位置为目标释放技能"),
    PASSIVE(3, "被动技能");

    private int code;
    private String desc;

    SkillType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SkillType fromCode(int code) {
        for (SkillType c : SkillType.values()) {
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