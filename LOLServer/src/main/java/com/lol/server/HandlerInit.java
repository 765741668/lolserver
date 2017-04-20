package com.lol.server;


import com.lol.ConnectProtocol;
import com.lol.Protocol;
import com.lol.connect.OnConnect;
import com.lol.connect.OnDisconnect;
import com.lol.handler.GameHandlerManager;
import com.lol.logic.fight.FightHandler;
import com.lol.logic.fight.FightRoomHandler;
import com.lol.logic.login.LoginHandler;
import com.lol.logic.match.MatchHandler;
import com.lol.logic.player.PlayerHandler;
import com.lol.logic.select.SelectHandler;
import com.lol.logic.select.SelectRoomHandler;
import com.lol.procesors.FightProcesor;
import com.lol.procesors.SelectProcesor;

/**
 * handler初始化
 *
 * @author Randy
 */
public class HandlerInit {

    //TODO : Aera 协议值
    public static void initProcessor() throws Exception {
        //连接事件
        GameHandlerManager.getInstance().registerProcessor(OnConnect.class.newInstance(), Protocol.TYPE_CONNECT, ConnectProtocol.CONNECT);
        GameHandlerManager.getInstance().registerProcessor(OnDisconnect.class.newInstance(), Protocol.TYPE_CONNECT, ConnectProtocol.DISCONNECT);
        //选择事件
        GameHandlerManager.getInstance().registerProcessor(SelectProcesor.class.newInstance(), Protocol.TYPE_SELECT, 1);
        //战斗事件
        GameHandlerManager.getInstance().registerProcessor(FightProcesor.class.newInstance(), Protocol.TYPE_FIGHT, 1);
    }

    public static void initHandler() throws Exception {
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_LOGIN, new LoginHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_USER, new PlayerHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_MATCH, new MatchHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_SELECT, new SelectHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_SELECT_ROOM, new SelectRoomHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_FIGHT, new FightHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_FIGHT_ROOM, new FightRoomHandler());
//        GameHandlerManager.getInstance().registerProcessor(Protocol.TYPE_HEARTBEAT,new H());

    }
}
