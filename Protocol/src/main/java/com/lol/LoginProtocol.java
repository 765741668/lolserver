package com.lol;

/**
 * 登录协议
 */
public class LoginProtocol {
    /**
     * 客户端申请注册
     */
    public static final int REG_CREQ = 0;
    /**
     * 服务器反馈给客户端 注册结果
     */
    public static final int REG_SRES = 1;
    /**
     * 客户端申请登录
     */
    public static final int LOGIN_CREQ = 2;
    /**
     * 服务器反馈给客户端 登录结果
     */
    public static final int LOGIN_SRES = 3;
    /**
     * 登录成功
     */
    public static final int LOGIN_OK = 4;
    /**
     * 该登录IP被拉黑
     */
    public static final int LOGIN_OUT_OF_WHITE_IP = 5;
    /**
     * 登录被服务器拒绝
     */
    public static final int LOGIN_DENY = 6;
    /**
     * 连接断开
     */
    public static final int LOGIN_INACTIVE = 7;

}