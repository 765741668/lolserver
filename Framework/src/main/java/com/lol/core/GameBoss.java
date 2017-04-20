package com.lol.core;

/**
 * 服务器设置类
 *
 * @author Randy
 *         2015-2-3
 */
public class GameBoss {

    /**
     * 逻辑处理器接口
     */
    private GameUpProcessor processor;

    public static GameBoss getInstance() {
        return GameBossHolder.instance;
    }

    /**
     * 取得逻辑处理器
     *
     * @return
     */
    public GameUpProcessor getProcessor() {
        return processor;
    }

    /**
     * 启动服务
     *
     * @param processor
     * @throws Exception
     */
    public void boot(GameUpProcessor processor) throws Exception {
        this.processor = processor;
        new Bootstrap().run();
    }

    private static final class GameBossHolder {
        private static final GameBoss instance = new GameBoss();
    }
}
