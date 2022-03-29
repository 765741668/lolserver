package com.lol.fwk.core;

import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.handler.GameProcessor;
import com.lol.fwk.handler.GameProcessorManager;
import com.lol.fwk.util.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 逻辑处理工作线程类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameWorker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(GameWorker.class);
    /**
     * 消息队列
     */
    final Queue<GameUpBuffer> msgQueue = new ConcurrentLinkedQueue<>();
    private Integer name;
    /**
     * 线程运行标志
     */
    private volatile boolean isRunning = true;

    /**
     * 初始化线程名称
     *
     * @param name
     */
    public GameWorker(Integer name) {
        this.name = name;
    }

    /**
     * 停止线程
     */
    public void closeWorker() {
        isRunning = false;
    }

    /**
     * 放置消息到队列中
     *
     * @param data
     */
    public void pushUpstreamBuffer(GameUpBuffer data) {
        if (isRunning) {
            synchronized (msgQueue) {
                msgQueue.add(data);
                msgQueue.notifyAll();
            }
        }
    }

    /**
     * 队列消息是否处理完毕
     *
     * @return
     */
    public boolean isDone() {
        return msgQueue.isEmpty();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (msgQueue) {
                if (msgQueue.isEmpty()) {
                    try {
                        msgQueue.wait();
                    } catch (InterruptedException e) {
                        logger.error("receive an interrupted msg, now the state is " + isRunning);
                        if (!isRunning) {
                            return;
                        }
                    }
                }
            }

            GameUpBuffer c;
            while ((c = msgQueue.poll()) != null) {
                logger.info("开始处理消息...");
                int cmd = c.getCmd();
                try {
                    logger.info("处理模块(msgType-cmd) : {}-{} ", name, cmd);
                    GameProcessor handler = GameProcessorManager.getInstance().getProcessor(name);
                    if (handler != null) {
                        //记录执行时间过长的指令
                        long startTime = System.currentTimeMillis();
                        handler.process(c);
                        long endTime = System.currentTimeMillis();

                        if ((startTime - endTime) > 500) {
                            logger.error("处理时间过长,大于500毫秒({}), msgType-cmd : {}-{} ", (endTime - startTime),name,cmd);
                        }else{
                            logger.info("消息(msgType-cmd)处理时间: {} ({}-{}) ", (endTime - startTime), name, cmd);
                        }
                    } else {
                        logger.warn("未知模块消息，不处理。 msgType-cmd : {}-{} ", name, cmd);
                    }
                } catch (Exception e) {
                    logger.error(StackTraceUtil.getStackTrace(e));
                }
            }
        }
    }
}
