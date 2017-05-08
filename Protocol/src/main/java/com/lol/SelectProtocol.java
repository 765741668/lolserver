package com.lol;

public class SelectProtocol {
    /**
     * 房间已匹配到 请求进入房间
     */
    public static final int ENTER_CREQ = 0;
    /**
     * 返回请求进入结果
     */
    public static final int ENTER_SRES = 1;
    /**
     * 进入结果广播给房间内其它玩家角色
     */
    public static final int ENTER_EXBRO = 2;
    /**
     * 客户端选择了英雄
     */
    public static final int SELECT_CREQ = 3;
    /**
     * 服务器返回没有此英雄
     */
    public static final int SELECT_SRES = 4;
    /**
     * 选择成功 通知房间所有人变更数据
     */
    public static final int SELECT_BRO = 5;
    /**
     * 发起聊天
     */
    public static final int TALK_CREQ = 6;
    /**
     * 广播聊天
     */
    public static final int TALK_BRO = 7;
    /**
     *
     */
    public static final int READY_CREQ = 8;
    public static final int READY_BRO = 9;
    /**
     * 解散房间
     */
    public static final int DESTORY_BRO = 10;
    public static final int FIGHT_BRO = 11;
    /**
     * 初始化选人房间
     */
    public static final int SELECT_INIT = 12;
}