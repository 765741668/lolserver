package com.lol.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑管理类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameProcessorManager {

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


}
