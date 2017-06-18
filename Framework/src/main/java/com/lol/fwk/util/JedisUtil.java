package com.lol.fwk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis工具类,用于获取RedisPool.
 * 参考官网说明如下：
 * You shouldn't use the same instance from different threads because you'll have strange errors.
 * And sometimes creating lots of Jedis instances is not good enough because it means lots of sockets and connections,
 * which leads to strange errors as well. A single Jedis instance is not threadsafe!
 * To avoid these problems, you should use JedisPool, which is a threadsafe pool of network connections.
 * This way you can overcome those strange errors and achieve great performance.
 * To use it, init a pool:
 * JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
 * You can store the pool somewhere statically, it is thread-safe.
 * JedisPoolConfig includes a number of helpful Redis-specific connection pooling defaults.
 * For example, Jedis with JedisPoolConfig will close a connection after 300 seconds if it has not been returned.
 *
 * @author Randy
 *         2015-1-29
 */
public class JedisUtil {

    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class.getName());
    private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

    /**
     * 私有构造器.
     */
    private JedisUtil() {
    }

    /**
     * 获取连接池.
     *
     * @return 连接池实例
     */
    private static JedisPool getPool(String ip, int port) {
        String key = ip + ":" + port;
        JedisPool pool = null;
        //这里为了提供大多数情况下线程池Map里面已经有对应ip的线程池直接返回，提高效率
        if (maps.containsKey(key)) {
            pool = maps.get(key);
            return pool;
        }
        //这里的同步代码块防止多个线程同时产生多个相同的ip线程池
        synchronized (JedisUtil.class) {
            if (!maps.containsKey(key)) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(Integer.parseInt(ProReaderUtil.getInstance().getRedisPro().get("maxTotal")));
                config.setMaxIdle(Integer.parseInt(ProReaderUtil.getInstance().getRedisPro().get("maxIdle")));
                config.setTestOnBorrow(true);
                config.setTestOnReturn(true);
                try {
                    /**
                     * 如果你遇到 java.net.SocketTimeoutException: Read timed out
                     * exception的异常信息 请尝试在构造JedisPool的时候设置自己的超时值.
                     * JedisPool默认的超时时间是2秒(单位毫秒)
                     */
                    pool = new JedisPool(config, ip, port,
                            Integer.parseInt(ProReaderUtil.getInstance().getRedisPro().get("timeOut")));
                    maps.put(key, pool);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                pool = maps.get(key);
            }
        }
        return pool;
    }

    /**
     * 当getInstance方法第一次被调用的时候，它第一次读取
     * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
     * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
     * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
     */
    public static JedisUtil getInstance() {
        return RedisUtilHolder.instance;
    }

    /**
     * 获取Redis实例.
     *
     * @return Redis工具类实例
     */
    public Jedis getJedis(String ip, int port) {
        Jedis jedis = null;
        int count = 0;
        do {
            try {
                jedis = getPool(ip, port).getResource();
            } catch (Exception e) {
                logger.error("get redis master failed (ip: " + ip + " port: " + port + ")");
                // 销毁对象
                getPool(ip, port).returnBrokenResource(jedis);
            }
            count++;
        } while (jedis == null && count < Integer.parseInt(ProReaderUtil.getInstance().getRedisPro().get("retryNum")));
        return jedis;
    }

    /**
     * 释放redis实例到连接池.
     *
     * @param jedis redis实例
     */
    public void closeJedis(Jedis jedis, String ip, int port) {
        if (jedis != null) {
            getPool(ip, port).returnResource(jedis);
        }
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class RedisUtilHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static JedisUtil instance = new JedisUtil();
    }

    public static void main(String[] args) {
        Jedis jedis = JedisUtil.getInstance().getJedis("127.0.0.1", 6379);
        jedis.ping();
        System.out.println(jedis.get("keyid"));
        jedis.del("keyid");//该命令用于在 key 存在时删除 key。
        System.out.println(jedis.get("keyid"));
        jedis.set("keyid","yzh2");

        jedis.exists("keyid");
        jedis.dump("keyid");//序列化给定 key ，并返回被序列化的值。
        jedis.expire("keyid",3000);//为给定 key 设置过期时间。
        jedis.expireAt("keyid",new Timestamp(System.currentTimeMillis()+10000).getTime());//为 key 设置过期时间。(unix timestamp)。
        jedis.pexpire("keyid",System.currentTimeMillis()+10000);//设置 key 的过期时间以毫秒计。
        jedis.keys("key");//查找所有符合给定模式( pattern)的 key 。
        jedis.move("keyid",1);//将当前数据库的 key 移动到给定的数据库 db 当中。
        jedis.persist("keyid");//移除 key 的过期时间，key 将持久保持。
        jedis.pttl("keyid");//以毫秒为单位返回 key 的剩余的过期时间。
        jedis.ttl("keyid");//以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
        jedis.randomKey();//从当前数据库中随机返回一个 key 。
        jedis.rename("keyid","keyid_new");
        jedis.renamenx("keyid","keyid_new");//仅当 newkey 不存在时，将 key 改名为 newkey 。
        jedis.type("keyid");//返回 key 所储存的值的类型。


    }
}
