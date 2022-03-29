package com.lol.demo.encode.protobuf.lol;

import com.lol.LoginProtocol;
import com.lol.MatchProtocol;
import com.lol.PlayerProtocol;
import com.lol.Protocol;
import com.lol.demo.encode.protobuf.MessageUpProto;
import com.lol.demo.util.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 云路供应链科技有限公司 版权所有 © Copyright 2019<br>
 * <p>
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_
 * //                         o8888888o
 * //                         88" . "88
 * //                         (| ^_^ |)
 * //                         O\  =  /O
 * //                      ____/`---'\____
 * //                    .'  \\|     |//  `.
 * //                   /  \\|||  :  |||//  \
 * //                  /  _||||| -:- |||||-  \
 * //                  |   | \\\  -  /// |   |
 * //                  | \_|  ''\---/''  |   |
 * //                  \  .-\__  `-`  ___/-. /
 * //                ___`. .'  /--.--\  `. . ___
 * //              ."" '<  `.___\_<|>_/___.'  >'"".
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /
 * //      ========`-.____`-.___\_____/___.-`____.-'========
 * //                           `=---='
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * //         佛祖保佑       永无BUG     永不修改
 * ////////////////////////////////////////////////////////////////////
 *
 * @Description:
 * @date 2021-07-12 10:29
 * @Author: <a href= "765741668@qq.com">yangzonghua</a>
 */
public class MsgControl6 {

    private static final Logger logger = LoggerFactory.getLogger(MsgControl6.class);

    private static int num = 6;
    private static MessageUpProto.LoginUpBody.Builder loginUpBody = MessageUpProto.LoginUpBody.newBuilder().setAcount("yzh" + num).setPassword("123456");

    public static void sendMsg(Channel ch) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            for (; ; ) {
                String line;
                line = in.readLine();
                if (line == null) {
                    continue;
                }
                if ("bye".equals(line.toLowerCase())) {
//                System.exit(2);
                    ch.closeFuture().sync();
                    break;
                }

                switch (line) {
                    //1-0
                    case Protocol.TYPE_LOGIN + "-" + LoginProtocol.REG_CREQ:
                        //注册新账户
                        logger.info("发送注册新账户信息{}: {}", num, loginUpBody.build().toString());
                        MessageUpProto.MessageUp messageUp = Utils.packageUpData(loginUpBody.getAcount(),Protocol.TYPE_LOGIN, 1, LoginProtocol.REG_CREQ, loginUpBody.build());
                        ch.writeAndFlush(messageUp);
                        continue;
                    //1-2
                    case Protocol.TYPE_LOGIN + "-" + LoginProtocol.LOGIN_CREQ:
                        //登陆
                        logger.info("发送登陆信息{}: {}", num, loginUpBody.build().toString());
                        MessageUpProto.MessageUp messageUpLogin = Utils.packageUpData(loginUpBody.getAcount(),Protocol.TYPE_LOGIN, 1, LoginProtocol.LOGIN_CREQ, loginUpBody.build());
                        ch.writeAndFlush(messageUpLogin);
                        continue;
                    //2-0
                    case Protocol.TYPE_PLYAER + "-" + PlayerProtocol.CREATE_CREQ:
                        MessageUpProto.PlayerUpBody.Builder playerCreate = MessageUpProto.PlayerUpBody.newBuilder();
                        playerCreate.setNickName("玩家角色-大蛇" + num);
                        logger.info("发送创建新玩家角色信息{}: {}", num, playerCreate.getNickName());
                        MessageUpProto.MessageUp messageUpPlayer = Utils.packageUpData(loginUpBody.getAcount(),Protocol.TYPE_PLYAER, 1,
                                PlayerProtocol.CREATE_CREQ, playerCreate.build());
                        ch.writeAndFlush(messageUpPlayer);
                        continue;
                        //2-2
                    case Protocol.TYPE_PLYAER + "-" + PlayerProtocol.INFO_CREQ:
                        logger.info("请求获取玩家角色信息" + num);
                        MessageUpProto.MessageUp info = Utils.packageUpData(loginUpBody.getAcount(),Protocol.TYPE_PLYAER, 1,
                                PlayerProtocol.INFO_CREQ, null);
                        ch.writeAndFlush(info);
                        continue;
                        //2-5
                    case Protocol.TYPE_PLYAER + "-" + PlayerProtocol.ONLINE_SRES:
                        logger.info("请求玩家角色上线" + num);
                        MessageUpProto.MessageUp playerOnline = Utils.packageUpData(loginUpBody.getAcount(),Protocol.TYPE_PLYAER, 1,
                                PlayerProtocol.ONLINE_CREQ, null);
                        ch.writeAndFlush(playerOnline);
                        continue;
                    case Protocol.TYPE_MATCH + "-" + MatchProtocol.ENTER_CREQ:
                        logger.info("请求寻找1V1匹配" + num);
                        MessageUpProto.MatchUpBody.Builder matchUpBody = MessageUpProto.MatchUpBody.newBuilder();
                        matchUpBody.setTeamMax(1);
                        MessageUpProto.MessageUp matchEnter = Utils.packageUpData(loginUpBody.getAcount(),Protocol.TYPE_MATCH, 1,
                                MatchProtocol.ENTER_CREQ, matchUpBody.build());
                        ch.writeAndFlush(matchEnter);
                        continue;
                    case Protocol.TYPE_MATCH + "-" + MatchProtocol.LEAVE_CREQ:
                        logger.info("离开1V1匹配" + num);
                        MessageUpProto.MessageUp leaveMatch = Utils.packageUpData(loginUpBody.getAcount(), Protocol.TYPE_MATCH, 1,
                                MatchProtocol.LEAVE_CREQ, null);
                        ch.writeAndFlush(leaveMatch);
                        continue;
                    case Protocol.TYPE_SELECT + "":
                        continue;
                    case Protocol.TYPE_SELECT_ROOM + "":
                        continue;
                    case Protocol.TYPE_FIGHT + "":
                        continue;
                    case Protocol.TYPE_FIGHT_ROOM + "":
                        continue;
                    default:
                       logger.warn("不支持的指令: {}", line);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
