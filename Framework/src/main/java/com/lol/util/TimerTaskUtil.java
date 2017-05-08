package com.lol.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 全局定时任务工具类
 *
 * @author Randy
 *         2015-2-6
 */
public class TimerTaskUtil {

    /**
     * 定时器设置
     */
    private final static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private static Logger logger = LoggerFactory.getLogger(TimerTaskUtil.class);

    public static TimerTaskUtil getInstance() {
        return TimerTaskUtilHolder.instance;
    }

    /**
     * 关闭定时器
     */
    public static void stop() {
        timer.shutdown();
    }

    /**
     * 延迟指定时间后 循环执行任务
     *
     * @param task
     * @param delay
     * @param unit
     */
    public void scheduleAtFixedRate(final Runnable task, long delay, TimeUnit unit) {
        try {
            timer.scheduleAtFixedRate(task, 0, delay, unit);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 延迟指定时间后执行任务
     *
     * @param task
     * @param delay
     * @param unit
     */
    public void schedule(final Runnable task, long delay, TimeUnit unit) {
        try {
            timer.schedule(task, delay, unit);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 延迟指定时间后 循环执行任务
     *
     * @param task
     * @param delay
     * @param unit
     */
    public void scheduleWithFixedDelay(final Runnable task, long delay, TimeUnit unit) {
        try {
            timer.scheduleWithFixedDelay(task, 0, delay, unit);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static class TimerTaskUtilHolder {
        private static TimerTaskUtil instance = new TimerTaskUtil();
    }
}
