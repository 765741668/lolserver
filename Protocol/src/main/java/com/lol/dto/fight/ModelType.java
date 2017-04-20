package com.lol.dto.fight;

/**
 * 战斗模型类型
 */
public enum ModelType {
    BUILD(0, "建筑"),
    HUMAN(1, "生物");

    private int code;
    private String desc;

    ModelType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ModelType fromCode(int code) {
        for (ModelType c : ModelType.values()) {
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