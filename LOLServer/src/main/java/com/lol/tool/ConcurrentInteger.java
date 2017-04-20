package com.lol.tool;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

public class ConcurrentInteger {
    private int value;
    private Mutex tex = new Mutex();

    public ConcurrentInteger() {
    }

    public ConcurrentInteger(int value) {
        synchronized (this) {
            this.value = value;
        }
    }

    /**
     * 自增并返回值
     *
     * @return
     */
    public final int getAndAdd() {
        synchronized (this) {
            try {
                tex.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value++;
            tex.release();
            return value;
        }
    }

    /**
     * 自减并返回值
     *
     * @return
     */
    public final int getAndReduce() {
        synchronized (this) {
            try {
                tex.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value--;
            tex.release();
            return value;
        }
    }

    /**
     * 重置value为0
     */
    public final void reset() {
        synchronized (this) {
            try {
                tex.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value = 0;
            tex.release();

        }
    }

    public final int get() {
        return value;
    }
}