package com.lol.tool;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleUtil {
    //等待执行的任务表
    private final ConcurrentHashMap<Integer, TimeTaskModel> mission = new ConcurrentHashMap<>();
    //等待移除的任务列表
    private final CopyOnWriteArrayList<Integer> removelist = new CopyOnWriteArrayList<>();
    private AtomicInteger index = new AtomicInteger();

    private ScheduleUtil() {

    }

    public static ScheduleUtil getInstance() {
        return ScheduleUtilHolder.util;
    }

    private void callback() {
        synchronized (removelist) {
            removelist.forEach(mission::remove);
            removelist.clear();
            mission.values().stream().filter(item -> item.time <= new Date().getTime()).forEach(item -> {
                item.run();
                removelist.add(item.id);
            });
        }
    }

    /**
     * 任务调用  毫秒
     *
     * @param task
     * @param delay
     * @return
     */
    public final int schedule(Runnable task, long delay) {

        //毫秒转微秒
        return schedulemms(task, delay * 1000 * 1000);
    }

    /**
     * 微秒级时间轴
     *
     * @param task
     * @param delay
     * @return
     */
    private int schedulemms(Runnable task, long delay) {
        synchronized (mission) {
            int id = index.getAndIncrement();
            TimeTaskModel model = new TimeTaskModel(id, task, getMicroSeconds(new Date()) + delay);
            model.run();
            mission.put(id, model);
            return id;
        }
    }

    public final void removeMission(int id) {
        removelist.add(id);
    }

    public final int schedule(Runnable task, Date time) {
        long t = getMicroSeconds(time) - getMicroSeconds(new Date());
        t = Math.abs(t);
        return schedulemms(task, t);
    }

    public final int timeSchedule(Runnable task, long time) {
        long t = time - getMicroSeconds(new Date());
        t = Math.abs(t);
        return schedulemms(task, t);
    }

    public long getMicroSeconds(Date date) {
        return date.getTime() * 1000 * 1000;
    }

    private static class ScheduleUtilHolder {
        private static ScheduleUtil util = new ScheduleUtil();
    }

}