package com.lol;

public class Protocol {
    /**
     * 连接模块
     */
    public static final byte TYPE_CONNECT = 0;
    /**
     * 登录模块
     */
    public static final byte TYPE_LOGIN = 1;
    /**
     * 用户模块
     */
    public static final byte TYPE_PLYAER = 2;
    /**
     * 匹配模块
     */
    public static final byte TYPE_MATCH = 3;
    /**
     * 英雄选择模块
     */
    public static final byte TYPE_SELECT = 4;
    /**
     * 英雄选择房间模块
     */
    public static final byte TYPE_SELECT_ROOM = 5;
    /**
     * 战斗模块
     */
    public static final byte TYPE_FIGHT = 6;
    /**
     * 战斗房间模块
     */
    public static final byte TYPE_FIGHT_ROOM = 7;
    /**
     * 心跳检测模块
     */
    public static final byte TYPE_HEARTBEAT = 8;
    /**
     * Http->tcp
     */
    public static final byte TYPE_HTTP2TCP = 9;
}