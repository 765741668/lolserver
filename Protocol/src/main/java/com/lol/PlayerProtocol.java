package com.lol;

public class PlayerProtocol {
    /**
     * 获取自身数据
     */
    public static final int INFO_CREQ = 0;
    /**
     * 返回自身数据
     */
    public static final int INFO_SRES = 1;
    /**
     * 申请创建角色
     */
    public static final int CREATE_CREQ = 2;
    /**
     * 返回创建结果
     */
    public static final int CREATE_SRES = 3;
    /**
     * 用户上线
     */
    public static final int ONLINE_CREQ = 4;
    /**
     * 返回用户上线
     */
    public static final int ONLINE_SRES = 5;
    /**
     * 用户下线
     */
    public static final int OFFLINE_CREQ = 6;
}