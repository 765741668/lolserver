package com.lol.server;

import com.lol.fwk.core.GameBoss;
import com.lol.fwk.core.ServerInit;

/**
 * 服务启动类
 *
 * @author Randy
 */
public class Server {

    public void init() throws Exception {
//        ServerInit.getInstance().initConfPath("/env");
//        ServerInit.getInstance().initLog4j();
        ServerInit.getInstance().initGameWorkers();
//        ServerInit.getInstance().initModules();
        HandlerInit.initProcessor();
        HandlerInit.initHandler();
//        DataInit.initItemType();
//        DataInit.initMonster();
//        DataInit.initSkill();
//        DataInit.initItemAct();
//        DataInit.initSkillAct();
//        DataInit.initTask();
    }

    public void run() throws Exception {
//        TimerTaskUtil.getInstance().schedule(new AutoUpdateTest(), 10, TimeUnit.SECONDS);
        //5秒检测一次线程状态，若线程终止则重启线程
//        TimerTaskUtil.getInstance().scheduleAtFixedRate(() -> ServerInit.getInstance().WorkerRunStatusCheck(),
//                5, TimeUnit.SECONDS);
        GameBoss.getInstance().boot();
    }
}
