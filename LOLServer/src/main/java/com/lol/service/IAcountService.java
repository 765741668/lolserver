package com.lol.service;


import com.lol.core.Connection;
import com.lol.db.ServiceException;

public interface IAcountService {
    /**
     * 账号创建
     *
     * @param connection
     * @param acount     注册的账号
     * @param password   注册的密码
     * @return 返回创建结果  0 成功 1 账号重复 2账号不合法 3 密码不合法
     */
    int create(Connection connection, String acount, String password) throws ServiceException;

    /**
     * 登陆
     *
     * @param connection
     * @param acount     账号
     * @param password   密码
     * @return 登录结果 0 成功   -1 账号不存在    -2 账号在线 -3 密码错误 -4 输入不合法
     */
    int login(Connection connection, String acount, String password);

    /**
     * 客户端断开连接（下线）
     *
     * @param connection
     */
    void close(Connection connection, String acount) throws ServiceException;

    /**
     * 获取账号ID
     *
     * @param connection
     * @return 返回用户的登陆账号ID
     */
    int getAcountId(Connection connection);
}