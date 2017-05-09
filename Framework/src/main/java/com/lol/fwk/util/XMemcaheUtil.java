package com.lol.fwk.util;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class XMemcaheUtil {

    public static MemcachedClient getClient(){

        int poolsize = 50;
        boolean failureMode = false;
        String hostport = "localhost:12000";
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil
                .getAddresses(hostport));

        // 设置连接池大小，即客户端个数
        builder.setConnectionPoolSize(poolsize);
        // 宕机报警
        builder.setFailureMode(failureMode);
        // 使用二进制文件
        builder.setCommandFactory(new BinaryCommandFactory());

        MemcachedClient memcachedClient;
        try {
            memcachedClient = builder.build();
            return memcachedClient;
        } catch (IOException e) {
            System.err.println("ShutdownMemcachedClientfail");
            e.printStackTrace();
        }
        return null;
    }

    public static MemcachedClient getClient2(){

        ApplicationContext ctx = new ClassPathXmlApplicationContext("env/memcached/spring-memcached.xml");
        return ctx.getBean(MemcachedClient.class);
    }

    public static void main(String[] args) {
//        MemcachedClient c = XMemcaheUtil.getClient();
        MemcachedClient c = XMemcaheUtil.getClient2();
        try {
            //单位秒
            c.set("user", 0, "test memcahed");
//            c.set("user", 1, "test memcahed2");
            System.out.println(c.get("user").toString());
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            e.printStackTrace();
        }


    }

}
