package com.lol.test;/**
 * Description : 
 * Created by YangZH on 2017/5/8
 *  0:58
 */

import com.lol.demo.encode.protobuf.lol.GameHandlerManager;
import com.lol.demo.encode.protobuf.lol.LOLClientPlayerHandler;
import com.lol.demo.encode.protobuf.lol.LOLNettyClient;

/**
 * Description :
 * Created by YangZH on 2017/5/8
 * 0:58
 */

public class PlayerTest {

    public static void main(String[] args) throws Exception {
        GameHandlerManager.getInstance().clearHandlers();
        GameHandlerManager.getInstance().registerHandler(1, new LOLClientPlayerHandler());
        LOLNettyClient.main(args);
    }
}
