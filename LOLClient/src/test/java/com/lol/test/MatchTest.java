package com.lol.test;/**
 * Description : 
 * Created by YangZH on 2017/5/8
 *  0:58
 */

import com.lol.demo.encode.protobuf.lol.GameHandlerManager;
import com.lol.demo.encode.protobuf.lol.LOLClientMatchHandler;
import com.lol.demo.encode.protobuf.lol.LOLNettyClient;

/**
 * Description :
 * Created by YangZH on 2017/5/8
 * 0:58
 */

public class MatchTest {

    public static void main(String[] args) throws Exception {
        GameHandlerManager.getInstance().clearHandlers();
        GameHandlerManager.getInstance().registerHandler(1, new LOLClientMatchHandler());
        LOLNettyClient.main(args);
    }
}
