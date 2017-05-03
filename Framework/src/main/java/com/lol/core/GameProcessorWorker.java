package com.lol.core;

import com.lol.buffer.GameUpBuffer;
import com.lol.handler.GameProcessor;
import com.lol.handler.GameProcessorManager;
import com.lol.util.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 逻辑处理工作类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameProcessorWorker {

    private static Logger logger = LoggerFactory.getLogger(GameProcessorWorker.class);

    private static class GameProcessorWorkerHolder{
        private static GameProcessorWorker instance = new GameProcessorWorker();
    }

    public static GameProcessorWorker getInstance(){
        return GameProcessorWorkerHolder.instance;
    }

    /**
     * 处理消息
     *
     * @param data
     */
    public void work(GameUpBuffer data) {
        int msgType = data.getMsgType();
        int cmd = data.getCmd();
        try {
            logger.info("work for msgType-cmd : {}-{} ", msgType,cmd);
//                    GameProcessor handler = GameProcessorManager.getInstance().getProcessor(cmd + "-" + name);
            GameProcessor processor = GameProcessorManager.getInstance().getProcessor(msgType);
            if (processor != null) {
                //记录执行时间过长的指令
                long startTime = System.currentTimeMillis();
                processor.process(data);
                long endTime = System.currentTimeMillis();
                if ((startTime - endTime) > 500) {
                    logger.error("process too long time, msgType-cmd : {}-{} ", msgType,cmd);
                }
            } else {
                logger.error("error msgType-cmd : {}-{} ", msgType,cmd);
            }
        } catch (Exception e) {
            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }

}
