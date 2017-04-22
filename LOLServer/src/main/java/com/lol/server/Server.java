package com.lol.server;

import com.lol.core.GameBoss;
import com.lol.core.GameWorkerManager;
import com.lol.util.ProReaderUtil;

/**
 * 服务启动类
 *
 * @author Randy
 */
public class Server {

    public void init() throws Exception {

//        ServerInit.getInstance().initConfPath("/env");
//        ServerInit.getInstance().initLog4j();
//        ServerInit.getInstance().initGameWorkers();
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
        System.out.println("start server ....");
//        TimerTaskUtil.getInstance().schedule(new AutoUpdateTest(), 10, TimeUnit.SECONDS);
        //5秒检测一次线程状态，若线程终止则重启线程
//        TimerTaskUtil.getInstance().scheduleAtFixedRate(() -> ServerInit.getInstance().WorkerRunStatusCheck(),
//                5, TimeUnit.SECONDS);
        int port = Integer.parseInt(ProReaderUtil.getInstance().getNettyPro().get("port"));
        GameBoss.getInstance().boot(GameWorkerManager.getInstance()::pushDataToWorker);
        System.out.println("server stating up on localhost " + port);
    }
}
