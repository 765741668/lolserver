package com.lol.fwk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 配置文件读取类
 *
 * @author Randy
 *         2015-1-29
 */
public class ProReaderUtil {

    private static Logger logger = LoggerFactory.getLogger(ProReaderUtil.class.getName());

    private static ProReaderUtil instance = new ProReaderUtil();

    /**
     * 配置文件路径
     */
    private String confPath = "/env";

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
     * @param filePath
     * @return
     */
    private ConfigurablePropertyResolver getPro(String filePath) {
        String fileAbsPath = confPath + filePath + ".properties";
        logger.info("file : " + fileAbsPath);
        MutablePropertySources propertySources = new MutablePropertySources();
        ConfigurablePropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        Properties properties = new Properties();
        try {
            properties.load(PropertiesExUtil.class.getResourceAsStream(fileAbsPath));
        } catch (IOException e) {
            logger.error(filePath + " config file not found.");
        }
        propertySources.addFirst(new PropertiesPropertySource("testProperties", properties));
        return propertyResolver;
    }

    /**
     * 获取redis配置
     *
     * @return
     */
    public HashMap<String, String> getRedisPro() {
        if (redisConf == null) {
            redisConf = new HashMap<String, String>();
            ConfigurablePropertyResolver properties = getPro("/db/redis");
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
        ConfigurablePropertyResolver properties = getPro("/netty/config/netty");
        conf.put("tcpPort", properties.getProperty("netty.tcp.port").trim());
        conf.put("tcpHost", properties.getProperty("netty.tcp.host").trim());
        conf.put("tcpHeartBeatTimeOut", properties.getProperty("netty.tcp.heartBeatTimeOut").trim());

        conf.put("httpPort", properties.getProperty("netty.http.port").trim());
        conf.put("httpHost", properties.getProperty("netty.http.host").trim());

        return conf;
    }

    /**
     * 获取逻辑工作线程配置
     *
     * @return
     */
    public String getWorkersPro() {
        ConfigurablePropertyResolver properties = getPro("/workers");
        return properties.getProperty("workers.member").trim();
    }

    /**
     * 获取jdbc配置
     *
     * @return
     */
    public HashMap<String, String> getJdbcPro() {
        HashMap<String, String> conf = new HashMap<String, String>();
        ConfigurablePropertyResolver properties = getPro("/db/jdbc");
        conf.put("driver", properties.getProperty("jdbc.driver").trim());
        conf.put("url", properties.getProperty("jdbc.url").trim());
        conf.put("userName", properties.getProperty("jdbc.userName").trim());
        conf.put("password", properties.getProperty("jdbc.password").trim());
        conf.put("initialSize", properties.getProperty("jdbc.initialSize").trim());
        conf.put("maxTotal", properties.getProperty("jdbc.maxTotal").trim());
        conf.put("maxConnLifetimeMillis", properties.getProperty("jdbc.maxConnLifetimeMillis").trim());
        conf.put("maxIdle", properties.getProperty("jdbc.maxIdle").trim());
        conf.put("minIdle", properties.getProperty("jdbc.minIdle").trim());
        conf.put("maxWaitMillis", properties.getProperty("jdbc.maxWaitMillis").trim());

        return conf;
    }

    /**
     * 获取MyBatis配置
     *
     * @return
     * @throws Exception
     */
    public FileInputStream getMyBatisPro() throws Exception {
        return new FileInputStream(confPath + "/db/mybatisConf.xml");
    }

    /**
     * 获取游戏业务模块配置
     *
     * @return
     * @throws Exception
     */
    public File getModulePro() throws Exception {
        return new File(confPath + "/moduleConf.xml");
    }
}