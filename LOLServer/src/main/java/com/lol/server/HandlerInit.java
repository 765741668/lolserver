package com.lol.server;


import com.lol.Protocol;
import com.lol.connect.OnConnect;
import com.lol.connect.OnDisconnect;
import com.lol.fwk.handler.GameHandlerManager;
import com.lol.fwk.handler.GameProcessorManager;
import com.lol.logic.fight.FightHandler;
import com.lol.logic.login.LoginHandler;
import com.lol.logic.match.MatchHandler;
import com.lol.logic.player.PlayerHandler;
import com.lol.logic.select.SelectHandler;
import com.lol.procesors.*;

/**
 * handler初始化
 *
 * @author Randy
 */
public class HandlerInit {


    public static void initProcessor() throws Exception {

        //连接事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(OnConnect.class),
                Protocol.TYPE_CONNECT);
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(OnDisconnect.class),
                Protocol.TYPE_CONNECT);
        //登陆事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(LoginProcesor.class),
                Protocol.TYPE_LOGIN);
        //玩家事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(PlayerProcesor.class),
                Protocol.TYPE_PLYAER);
        //匹配事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(MatchProcesor.class),
                Protocol.TYPE_MATCH);
        //预选择事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(SelectProcesor.class),
                Protocol.TYPE_SELECT);
        //房间内选择事件
        GameProcessorManager.getInstance().registerProcessor(SelectRoomProcesor.class.newInstance(),
                Protocol.TYPE_SELECT_ROOM);
        //预战斗事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(FightProcesor.class),
                Protocol.TYPE_FIGHT);
        //房间内战斗事件
        GameProcessorManager.getInstance().registerProcessor(FightRoomProcesor.class.newInstance(),
                Protocol.TYPE_FIGHT_ROOM);
        //心跳链路检测事件
//        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(HeartBeatProcesor.class),
// Protocol.TYPE_HEARTBEAT);
    }

    public static void initHandler() throws Exception {
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_LOGIN, new LoginHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_PLYAER, new PlayerHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_MATCH, new MatchHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_SELECT, new SelectHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_FIGHT, new FightHandler());
//        GameHandlerManager.getInstance().registerProcessor(Protocol.TYPE_HEARTBEAT,new H());

    }
}
