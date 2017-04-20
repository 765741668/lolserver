package com.lol.server;

import com.lol.core.GameBoss;
import com.lol.core.GameWorkerManager;
import com.lol.core.ServerInit;
import com.lol.util.ModuleUtil;
import com.lol.util.TimerTaskUtil;

import java.util.concurrent.TimeUnit;

/**
 * 服务启动类
 *
 * @author Randy
 */
public class Server {

    public static void main(String[] args) throws Exception {
//        ServerInit.getInstance().initConfPath("/gameFiles");
        System.out.println("start server ....");
//        ServerInit.getInstance().initLog4j();
        ServerInit.getInstance().initGameWorkers();
//        ServerInit.getInstance().initModules();
        HandlerInit.initHandler();
//        DataInit.initItemType();
//        DataInit.initMonster();
//        DataInit.initSkill();
//        DataInit.initItemAct();
//        DataInit.initSkillAct();
//        DataInit.initTask();

//        TimerTaskUtil.getInstance().schedule(new AutoUpdateTest(), 10, TimeUnit.SECONDS);
        //5秒检测一次线程状态，若线程终止则重启线程
        TimerTaskUtil.getInstance().scheduleAtFixedRate(() -> ServerInit.getInstance().WorkerRunStatusCheck(),
                5, TimeUnit.SECONDS);
        new Server().run();

        System.out.println("server stating up....");

    }

    public void run() throws Exception {
        GameBoss.getInstance().boot(GameWorkerManager.getInstance()::pushDataToWorker);
    }
}

/**
 * 自动更新测试
 */
class AutoUpdateTest implements Runnable {

    private boolean isUpdated = true;

    private boolean isFinish = false;

    @Override
    public void run() {
        try {
            if (!isUpdated && !isFinish) {
                System.out.println("start update module...");
                ModuleUtil.getInstance().updateModule("test2");
                System.out.println("update module finished, now modules:");
                System.out.println(ModuleUtil.getInstance().getModuleInfo());
                isFinish = true;
            }
            isUpdated = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
