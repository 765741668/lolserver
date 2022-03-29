package com.lol.fwk.handler;

import com.lol.fwk.buffer.GameUpBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑管理类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameProcessorManager {

    private static Logger logger = LoggerFactory.getLogger(GameProcessorManager.class);

    private static class GameProcessorManagerHolder {
        private static GameProcessorManager instance = new GameProcessorManager();
    }

    public static GameProcessorManager getInstance() {
        return GameProcessorManagerHolder.instance;
    }

    /**
     * 业务逻辑集合
     */
    private Map<Integer, GameProcessor> processorMap = new HashMap<>();

    /**
     * 注册业务逻辑
     *
     * @param cls
     * @param msgType
     * @throws Exception
     */
    public void registerProcessor(GameProcessor cls, int msgType) throws Exception {
        processorMap.put(msgType, cls);
        logger.info("{} processor has been registed...", msgType);
    }

    /**
     * 获取业务逻辑
     *
     * @param msgType
     * @return
     */
    public GameProcessor getProcessor(int msgType) {
        return processorMap.get(msgType);
    }

    //同步分发指令(改用异步缓冲分发的方式)
    public void dispatch(GameUpBuffer buffer) {
        GameProcessor handler = processorMap.get(buffer.getMsgType());
        if (handler != null) {
            try {
                handler.process(buffer);
            } catch (Exception e) {
                logger.error("消息派发失败:{}, buffer:{}", e.getMessage(), buffer, e);
            }
        } else {
            logger.error("不支持的消息类型,忽略处理...");
        }
    }
}
