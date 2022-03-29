package com.lol.demo.encode.protobuf.lol;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑管理类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameProcessorManager {

    /**
     * 业务逻辑集合
     */
    private Map<Integer, BaseProcessor> processorMap = new HashMap<>();

    public static GameProcessorManager getInstance() {
        return GameProcessorManagerHolder.instance;
    }

    /**
     * 获取业务逻辑
     *
     * @return
     */
    public BaseProcessor getProcessor(int protocol) {
        return processorMap.get(protocol);
    }

    public void registerProcessor(int protocol, BaseProcessor processor) {
        processorMap.put(protocol, processor);
    }

    public void removeProcessor(int protocol) {
        processorMap.remove(protocol);
    }

    public void clearProcessors() {
        processorMap.clear();
    }

    /**
     * 获取业务逻辑
     *
     * @return
     */
    public Map<Integer, BaseProcessor> getAllProcessor() {
        return processorMap;
    }

    private static class GameProcessorManagerHolder {
        private static GameProcessorManager instance = new GameProcessorManager();
    }
}
