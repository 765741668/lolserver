package com.lol.fwk.core;

import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.util.ProReaderUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * 逻辑处理工作线程管理器类
 *
 * @author Randy
 *         2015-2-5
 */
public class GameWorkerManager {

    private static Logger logger = Logger.getLogger(GameWorkerManager.class);

    private static GameWorkerManager instance = new GameWorkerManager();
    /**
     * 工作线程名集合
     */
    private final String[] workerSet = ProReaderUtil.getInstance().getWorkersPro().split(",");
    /**
     * 工作线程对象集合
     */
    private HashMap<Integer, GameWorker> workers = new HashMap<>();
    /**
     * 工作线程集合
     */
    private HashMap<Integer, Thread> threads = new HashMap<>();

    public static GameWorkerManager getInstance() {
        return instance;
    }

    /**
     * 获取工作线程名集合
     *
     * @return
     */
    public String[] getWorkerSet() {
        return workerSet;
    }

    /**
     * 初始化工作线程
     */
    public void init() {
        for (String w : workerSet) {
            final GameWorker worker = new GameWorker(Integer.valueOf(w));
            setWorker(Integer.valueOf(w), worker);
        }
    }

    /**
     * 启动工作线程
     *
     * @param msgType
     * @param worker
     */
    public void setWorker(Integer msgType, GameWorker worker) {
        workers.put(msgType, worker);
        threads.put(msgType, new Thread(worker));
        threads.get(msgType).start();
        logger.info("work thread:" + msgType + " has been started...");
    }

    /**
     * 获取工作线程对象
     *
     * @param msgType
     * @return
     */
    private GameWorker getWorker(Integer msgType) {
        return workers.get(msgType);
    }

    /**
     * 获取工作线程
     *
     * @param msgType
     * @return
     */
    public Thread getThread(Integer msgType) {
        return threads.get(msgType);
    }

    /**
     * 放置消息到指定工作线程
     *
     * @param buffer
     */
    public void pushDataToWorker(GameUpBuffer buffer) {
        if (buffer.getArea() >= 0) {
            GameWorker worker = getWorker(buffer.getMsgType());
            if (worker != null) {
                worker.pushUpstreamBuffer(buffer);
            } else {
                logger.error("子模块工作线程: [" + buffer.getArea() + "] 没有找到.");
            }
        } else {
            logger.error("子模块 : [" + buffer.getArea() + "] 没有找到.");
        }
    }
}
