package com.lol.server;/**
 * Description : 
 * Created by YangZH on 2017/4/20
 *  17:53
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description :
 * Created by YangZH on 2017/4/20
 * 17:53
 */

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/env/spring/config.xml");
//        Server server = context.getBean(Server.class);
        Server server = new Server();
        try {
            server.init();
            server.run();
        } catch (Exception e) {
            logger.error("启动服务器失败", e);
        }
    }

}
