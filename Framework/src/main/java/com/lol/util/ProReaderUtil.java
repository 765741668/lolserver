package com.lol.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * 配置文件读取类
 *
 * @author Randy
 *         2015-1-29
 */
public class ProReaderUtil {

    private static Logger logger = Logger.getLogger(ProReaderUtil.class.getName());

    private static ProReaderUtil instance = new ProReaderUtil();

    /**
     * 配置文件路径
     */
    private String confPath = "gameFiles";

    /**
     * redis配置缓存
     */
    private HashMap<String, String> redisConf = null;

    public static ProReaderUtil getInstance() {
        return instance;
    }

    /**
     * 获取配置文件根路径
     *
     * @return
     */
    public String getConfPath() {
        return confPath;
    }

    /**
     * 设置配置文件根路径
     *
     * @param path
     */
    public void setConfPath(String path) {
        confPath = path;
    }

    /**
     * 读取配置文件内容
     *
     * @param file
     * @return
     */
    private Properties getPro(String file) {
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        logger.info("configPath : " + confPath);
        try {
            inputStream = new FileInputStream(confPath + "/conf/" + file + ".properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            logger.error(file + " config file not found.");
        }

        return properties;
    }

    /**
     * 获取log4j配置
     *
     * @return
     */
    public Properties getLog4jPro() {
        return getPro("log4j");
    }

    /**
     * 获取redis配置
     *
     * @return
     */
    public HashMap<String, String> getRedisPro() {
        if (redisConf == null) {
            redisConf = new HashMap<String, String>();
            Properties properties = getPro("redis");
            redisConf.put("host", properties.getProperty("redis.host").trim());
            redisConf.put("port", properties.getProperty("redis.port").trim());
            redisConf.put("maxTotal", properties.getProperty("redis.maxTotal").trim());
            redisConf.put("maxIdle", properties.getProperty("redis.maxIdle").trim());
            redisConf.put("timeOut", properties.getProperty("redis.timeOut").trim());
            redisConf.put("retryNum", properties.getProperty("redis.retryNum").trim());
        }

        return redisConf;
    }

    /**
     * 获取netty配置
     *
     * @return
     */
    public HashMap<String, String> getNettyPro() {
        HashMap<String, String> conf = new HashMap<String, String>();
        Properties properties = getPro("netty");
        conf.put("port", properties.getProperty("netty.port").trim());
        conf.put("host", properties.getProperty("netty.host").trim());
        conf.put("heartBeatTimeOut", properties.getProperty("netty.heartBeatTimeOut").trim());

        return conf;
    }

    /**
     * 获取逻辑工作线程配置
     *
     * @return
     */
    public String getWorkersPro() {
        Properties properties = getPro("workers");
        return properties.getProperty("workers.member").trim();
    }

    /**
     * 获取jdbc配置
     *
     * @return
     */
    public HashMap<String, String> getJdbcPro() {
        HashMap<String, String> conf = new HashMap<String, String>();
        Properties properties = getPro("jdbc");
        conf.put("driver", properties.getProperty("jdbc.driver").trim());
        conf.put("url", properties.getProperty("jdbc.url").trim());
        conf.put("userName", properties.getProperty("jdbc.userName").trim());
        conf.put("password", properties.getProperty("jdbc.password").trim());
//        conf.put("initialSize", properties.getProperty("jdbc.initialSize").trim());
//        conf.put("maxTotal", properties.getProperty("jdbc.maxTotal").trim());
//        conf.put("maxConnLifetimeMillis", properties.getProperty("jdbc.maxConnLifetimeMillis").trim());
//        conf.put("maxIdle", properties.getProperty("jdbc.maxIdle").trim());
//        conf.put("minIdle", properties.getProperty("jdbc.minIdle").trim());
//        conf.put("maxWaitMillis", properties.getProperty("jdbc.maxWaitMillis").trim());

//        <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
//        <property name="driverClass">
//        <value>${database.driver}</value>
//        </property>
//        <property name="jdbcUrl">
//        <value>${database.dbName}</value>
//        </property>
//        <property name="player">
//        <value>${database.username}</value>
//        </property>
//        <property name="password">
//        <value>${database.password}</value>
//        </property>
//        <property name="maxPoolSize">
//        <value>1</value>
//        </property>
//        <property name="maxIdleTime">
//        <value>50</value>
//        </property>
//        <property name="maxStatementsPerConnection">
//        <value>1</value>
//        </property>
//        <property name="numHelperThreads">
//        <value>1</value>
//        </property>
//        <property name="idleConnectionTestPeriod">
//        <value>30</value>
//        </property>
//        </bean>


        return conf;
    }

    /**
     * 获取MyBatis配置
     *
     * @return
     * @throws Exception
     */
    public FileInputStream getMyBatisPro() throws Exception {
        return new FileInputStream(confPath + "/conf/mybatisConf.xml");
    }

    /**
     * 获取游戏业务模块配置
     *
     * @return
     * @throws Exception
     */
    public File getModulePro() throws Exception {
        return new File(confPath + "/conf/moduleConf.xml");
    }
}