package com.lol.constans;

public enum  BuffEnum {

    RED(1,"红BUFF"),
    BLUE(2,"蓝BUFF"),
    RIVER(3,"河蟹"),
    WIND_GRAGON(4,"风龙"),
    FIRE_GRAGON(5,"火龙"),
    WATER_GRAGON(6,"水龙"),
    SOIL_GRAGON(7,"土龙"),
    ANCIENT_DRAGON(8,"远古巨龙"),
    PIONEER_VALLEY(9,"峡谷先锋"),
    NASH_BARON(10,"纳什男爵");

    private int code;
    private String description;

    BuffEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BuffEnum fromCode(int code) {
        for (BuffEnum c : BuffEnum.values()) {
            if (c.code == (code)) {
                return c;
            }
        }
        throw new IllegalArgumentException(code+"");
    }

    public static BuffEnum fromDescription(String description) {
        for (BuffEnum c : BuffEnum.values()) {
            if (c.description.equals(description)) {
                return c;
            }
        }
        throw new IllegalArgumentException(description);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void main(String[] args) {
        System.out.println(fromDescription("风龙"));
        System.out.println(fromCode(4));
    }
}
