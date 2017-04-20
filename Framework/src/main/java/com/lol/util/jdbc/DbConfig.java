package com.lol.util.jdbc;


import com.lol.util.ProReaderUtil;

import java.util.HashMap;

/**
 * jdbc配置类
 */
public class DbConfig {

    private String driver;

    private String url;

    private String userName;

    private String password;

    private int initialSize;

    private int maxTotal;

    private long maxConnLifetimeMillis;

    private int maxIdle;

    private int minIdle;

    private long maxWaitMillis;

    public DbConfig() {
        HashMap<String, String> conf = ProReaderUtil.getInstance().getJdbcPro();
        this.driver = conf.get("driver");
        this.url = conf.get("url");
        this.userName = conf.get("userName");
        this.password = conf.get("password");
        this.initialSize = Integer.parseInt(conf.get("initialSize") == null ? null : conf.get("initialSize"));
        this.maxTotal = Integer.parseInt(conf.get("maxTotal") == null ? null : conf.get("maxTotal"));
        this.maxConnLifetimeMillis = Long.parseLong(conf.get("maxConnLifetimeMillis") == null ? null : conf.get("maxConnLifetimeMillis"));
        this.maxIdle = Integer.parseInt(conf.get("maxIdle") == null ? null : conf.get("maxIdle"));
        this.minIdle = Integer.parseInt(conf.get("minIdle") == null ? null : conf.get("minIdle"));
        this.maxWaitMillis = Long.parseLong(conf.get("maxWaitMillis") == null ? null : conf.get("maxWaitMillis"));
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public long getMaxConnLifetimeMillis() {
        return maxConnLifetimeMillis;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }
}
