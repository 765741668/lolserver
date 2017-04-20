package com.lol;

public class Protocol {
    /**
     * 连接模块
     */
    public static final byte TYPE_CONNECT = 1;
    /**
     * 登录模块
     */
    public static final byte TYPE_LOGIN = 2;
    /**
     * 用户模块
     */
    public static final byte TYPE_USER = 3;
    /**
     * 匹配模块
     */
    public static final byte TYPE_MATCH = 4;
    /**
     * 英雄选择模块
     */
    public static final byte TYPE_SELECT = 5;
    /**
     * 英雄选择房间模块
     */
    public static final byte TYPE_SELECT_ROOM = 6;
    /**
     * 战斗模块
     */
    public static final byte TYPE_FIGHT = 7;
    /**
     * 战斗房间模块
     */
    public static final byte TYPE_FIGHT_ROOM = 8;
    /**
     * 心跳检测模块
     */
    public static final byte TYPE_HEARTBEAT = 9;
}