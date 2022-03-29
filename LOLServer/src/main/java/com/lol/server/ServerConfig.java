package com.lol.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务启动类
 *
 * @author Randy
 */
@Configuration
public class ServerConfig {

    @Bean("server")
    public Server server() {
        return new Server();
    }
}
