package com.lol.common;/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  13:44
 */

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 13:44
 */

public enum ChannelCategory {
    ONLINE("ONLINE", "在线客户端连接"),
    ROOM("ROOM", "房间或副本客户端连接");

    private String code;
    private String desc;

    ChannelCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ChannelCategory fromCode(String code) {
        for (ChannelCategory c : ChannelCategory.values()) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        throw new IllegalArgumentException(code);
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFormattedDescptions(Object... args) {
        return String.format(desc, args);
    }
}
