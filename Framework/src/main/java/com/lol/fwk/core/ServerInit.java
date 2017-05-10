package com.lol.fwk.core;

import com.lol.fwk.util.ModuleUtil;
import com.lol.fwk.util.ProReaderUtil;
import org.apache.log4j.Logger;

/**
 * 服务初始化类
 *
 * @author Randy
 *         2015-1-30
 */
public class ServerInit {

    private static Logger logger = Logger.getLogger(ServerInit.class);

    private static ServerInit instance = new ServerInit();

    public static ServerInit getInstance() {
        return instance;
    }

    /**
     * 初始化逻辑工作线程
     */
    public void initGameWorkers() {
        GameWorkerManager.getInstance().init();
    }

    /**
     * 初始化配置文件根路径
     */
    public void initConfPath(String path) {
        ProReaderUtil.getInstance().setConfPath(path);
    }

    /**
     * 初始化游戏业务模块
     */
    public void initModules() {
        ModuleUtil.getInstance().init();
        logger.info("The loaded business modules:\n" + ModuleUtil.getInstance().getModuleInfo());
    }

    /**
     * 工作线程运行状态检查
     */
    public void WorkerRunStatusCheck() {
        String[] workers = GameWorkerManager.getInstance().getWorkerSet();
        for (String worker : workers) {
            int w = Integer.valueOf(worker);
            Thread t = GameWorkerManager.getInstance().getThread(w);
            if (t == null || !t.isAlive()) {
                logger.error("work thread:" + worker + " was crashed.");
                final GameWorker gw = new GameWorker(w);
                GameWorkerManager.getInstance().setWorker(w, gw);
            }
        }
    }
}
