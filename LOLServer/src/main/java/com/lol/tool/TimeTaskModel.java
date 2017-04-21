package com.lol.tool;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimeTaskModel {
    //任务执行的时间
    public long time;
    public int id; //任务ID
    //任务逻辑
    private Runnable execut;

    public TimeTaskModel(int id, Runnable execut, long time) {
        this.id = id;
        this.execut = execut;
        this.time = time;
    }

    public final void run() {
        Executors.newScheduledThreadPool(1).schedule(execut, time, TimeUnit.MICROSECONDS);
    }

    public Runnable getExecut() {
        return execut;
    }

    public void setExecut(Runnable execut) {
        this.execut = execut;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}