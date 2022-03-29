package com.lol.server;/**
 * Description :
 * Created by YangZH on 2017/4/20
 *  17:53
 */

import com.lol.fwk.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Description :
 * Created by YangZH on 2017/4/20
 * 17:53
 */

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static AbstractApplicationContext context = new AnnotationConfigApplicationContext("com.lol.*");

    public static void main(String[] args) throws ServiceException {
        Server server = context.getBean(Server.class);
        try {
            server.init();
            server.run();
        } catch (Exception e) {
            logger.error("启动服务器失败", e);
            System.exit(2);
        }
    }

}
