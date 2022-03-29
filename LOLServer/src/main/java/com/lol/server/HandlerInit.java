package com.lol.server;


import com.lol.Protocol;
import com.lol.connect.OnConnect;
import com.lol.connect.OnDisconnect;
import com.lol.fwk.handler.GameHandlerManager;
import com.lol.fwk.handler.GameProcessorManager;
import com.lol.handler.GameServerChannelHandler;
import com.lol.handler.HttpServerHandler;
import com.lol.logic.fight.FightProcessor;
import com.lol.logic.fight.FightRoomProcessor;
import com.lol.logic.login.LoginProcessor;
import com.lol.logic.match.MatchProcessor;
import com.lol.logic.player.PlayerProcessor;
import com.lol.logic.select.SelectProcessor;
import com.lol.logic.select.SelectRoomProcessor;

//import com.lol.logic.login.HttpLoginHandle;

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
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(LoginProcessor.class),
                Protocol.TYPE_LOGIN);
        //玩家事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(PlayerProcessor.class),
                Protocol.TYPE_PLYAER);
        //匹配事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(MatchProcessor.class),
                Protocol.TYPE_MATCH);
        //预选择事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(SelectProcessor.class),
                Protocol.TYPE_SELECT);
        //房间内选择事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(SelectRoomProcessor.class),
                Protocol.TYPE_SELECT_ROOM);
        //预战斗事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(FightProcessor.class),
                Protocol.TYPE_FIGHT);
        //房间内战斗事件
        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(FightRoomProcessor.class),
                Protocol.TYPE_FIGHT_ROOM);
        //心跳链路检测事件
//        GameProcessorManager.getInstance().registerProcessor(Application.context.getBean(HeartBeatProcesor.class),
// Protocol.TYPE_HEARTBEAT);
    }

    public static void initHandler() throws Exception {
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_LOGIN, new GameServerChannelHandler());
        GameHandlerManager.getInstance().registerHandler(Protocol.TYPE_HTTP, new HttpServerHandler());

    }
}
