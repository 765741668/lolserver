package com.lol.util;

import com.lol.util.jdbc.DbConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Util {

    private static ComboPooledDataSource ds = null;

    static {
        try {
            DbConfig config = new DbConfig();
            // Logger env.log = Logger.getLogger("com.mchange"); // 日志
            // env.log.setLevel(Level.WARNING);
            ds = new ComboPooledDataSource();
            // 设置JDBC的Driver类
            ds.setDriverClass(config.getDriver());  // 参数由 Config 类根据配置文件读取
            // 设置JDBC的URL
            ds.setJdbcUrl(config.getUrl());
            // 设置数据库的登录用户名
            ds.setUser(config.getUserName());
            // 设置数据库的登录用户密码
            ds.setPassword(config.getPassword());
            // 设置连接池的最大连接数
            ds.setMaxPoolSize(200);
            // 设置连接池的最小连接数
            ds.setMinPoolSize(20);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    private C3P0Util() {
    }

    public static synchronized Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return con;
    }
    // C3P0 end
}

