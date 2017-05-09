package com.lol.fwk.core;

import com.lol.fwk.buffer.GameUpBuffer;
import com.lol.fwk.handler.GameProcessor;
import com.lol.fwk.handler.GameProcessorManager;
import com.lol.fwk.util.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 逻辑处理工作类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameProcessorWorker {

    private  static  Logger logger = LoggerFactory.getLogger(GameProcessorWorker.class);

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
        logger.info("开始处理消息...");
        int msgType = data.getMsgType();
        int cmd = data.getCmd();
        try {
            logger.info("处理模块(msgType-cmd) : {}-{} ", msgType,cmd);
            GameProcessor processor = GameProcessorManager.getInstance().getProcessor(msgType);
            if (processor != null) {
                //记录执行时间过长的指令
                long startTime = System.currentTimeMillis();
                processor.process(data);
                long endTime = System.currentTimeMillis();
                if ((endTime - startTime) > 500) {
                    logger.error("处理时间过长,大于500毫秒({}), msgType-cmd : {}-{} ", (endTime - startTime),msgType,cmd);
                }else{
                    logger.info("消息(msgType-cmd)处理时间: {} ({}-{}) ", (endTime - startTime),msgType,cmd);
                }
            } else {
                logger.warn("未知模块消息，不处理。 msgType-cmd : {}-{} ", msgType,cmd);
            }
        } catch (Exception e) {
            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }

}
