package com.lol;

public class MatchProtocol {
    /**
     * 申请进入匹配
     */
    public static final int ENTER_CREQ = 0;
    /**
     * 返回申请匹配结果
     */
    public static final int ENTER_SRES = 1;
    /**
     * 申请离开匹配
     */
    public static final int LEAVE_CREQ = 2;
    /**
     * 返回离开结果
     */
    public static final int LEAVE_SRES = 3;
    /**
     * 匹配完毕，发送通知
     */
    public static final int MATCH_COMPLETED = 4;
}