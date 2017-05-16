package com.lol.test;/**
 * Description : 
 * Created by YangZH on 2017/5/13
 *  19:52
 */

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description :
 * Created by YangZH on 2017/5/13
 * 19:52
 */

public class TestI_ADDADD {
    public static void main(String[] args) {
        ConcurrentHashMap map = new ConcurrentHashMap();
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        AtomicInteger integer = new AtomicInteger();
        ExecutorService es = Executors.newScheduledThreadPool(2);

//        ThreadPoolExecutor es2 = new ThreadPoolExecutor(1,1,1,TimeUnit.MINUTES,new DelayQueue<CustomDelayed>());
    }

    static class CustomDelayed implements Delayed{

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }

    static void test(){
        System.out.println("sheduling.....");
    }
}
