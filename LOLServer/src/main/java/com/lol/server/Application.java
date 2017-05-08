package com.lol.server;/**
 * Description : 
 * Created by YangZH on 2017/4/20
 *  17:53
 */

import com.lol.db.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description :
 * Created by YangZH on 2017/4/20
 * 17:53
 */

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    public static ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/env/spring/config.xml");

    public static void main(String[] args) throws ServiceException {

        //打印已注入的Bean
//        String[] s = context.getBeanDefinitionNames();
//        for (String value : s) {
//            logger.info(value);
//        }
        Server server = context.getBean(Server.class);
        try {
            server.init();
            server.run();
        } catch (Exception e) {
            logger.error("启动服务器失败", e);
        }
    }

}
