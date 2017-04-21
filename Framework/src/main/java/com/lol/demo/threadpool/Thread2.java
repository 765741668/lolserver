/******************************************************************************
 *
 * Module Name:  threadpool - thread.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Nov 27, 2015
 * Last Updated By: java
 * Last Updated Date: Nov 27, 2015
 * Description: 
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
/**
 *
 */
package com.lol.demo.threadpool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1.synchronized method
 * 2.synchronized block
 * 3.volatile variable
 * 4.ReentrantLock
 * 5.ThreadLocal variable
 *
 * @author java
 */
public class Thread2 {
    volatile static Map maps = new HashMap();

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TestBank tb = new TestBank(1000);

        MyThread3 tt = new MyThread3(tb);
        MyThread3 ttt = new MyThread3(tb);
        MyThread3 tttt = new MyThread3(new TestBank(1000));

        Thread t1 = new Thread(tt, "t1");
        Thread t2 = new Thread(ttt, "t2");
        Thread t3 = new Thread(tttt, "t3");
        t1.setName("t11");
        t2.setName("t22");
        t3.setName("t33");

        t1.start();
        // t1.join();
        t2.start();
        // t2.join();
        t3.start();

        // for (int i = 0; i < 10; i++)
        // {
        // // int a = new Random().nextInt(4);
//    	Thread.sleep(1000*(a==0?1:a));
        // System.out.println("main :" + i);
        // }
        // System.out.println("end");
        // System.out.println(Runtime.getRuntime().availableProcessors());
    }

}

class MyThread3 implements Runnable {

    private TestBank bean;

    public MyThread3(TestBank bean) {
        this.bean = bean;
    }

    public void run() {
        /** count */
//         bean.saveNone(10,Thread.currentThread().getName());
        /** count */
//         bean.getSynOnBlockWithAttr(100, Thread.currentThread().getName());
        /** scount */
//        TestBank.saveStaticSynOnMethod(10, Thread.currentThread().getName());
//         TestBank.saveSynOnBlockWithClass(10, Thread.currentThread().getName());
        /** count */
//         bean.saveSynOnMethod(10, Thread.currentThread().getName());
        /** count */
//        bean.saveSynOnBlockWithAttr(10, Thread.currentThread().getName());
        /** vcount */
//         bean.saveSynWithVolatile(10, Thread.currentThread().getName());
        /** count lock */
        bean.saveSynWithLock(10, Thread.currentThread().getName());
        /** tlcount */
//         bean.saveSynWithThreadLocal(10, Thread.currentThread().getName());
        /** condition */
//         bean.saveSynWithCondition(10, Thread.currentThread().getName());
        /**Atomic*/
//        bean.saveAtomic(100, Thread.currentThread().getName());
        /**AtomicAndVolatile*/
//        bean.saveAtomicAndVolatile(100, Thread.currentThread().getName());
//        bean.saveTest(100, Thread.currentThread().getName());
        /**ConcurrentHashMap**/
//        bean.saveConcurrentHashMap(10, Thread.currentThread().getName());
    }
}

class TestBank {
    private static Integer scount;//3
    private List list;//1
    private Integer count;//2
    private volatile Integer vcount;//4
    private ReentrantLock lock = new ReentrantLock();
    private ConcurrentMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    private ThreadLocal<Integer> tlcount;//5
    private AtomicInteger at;

    public TestBank(final Integer count) {
        list = new ArrayList();
        this.count = count;
        scount = count;
        this.vcount = count;
        this.tlcount = new ThreadLocal<Integer>() {
            protected Integer initialValue() {
                return count;
            }
        };
        at = new AtomicInteger(count);

    }

    public static synchronized void saveStaticSynOnMethod(Integer num, String str) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            try {
                int a = new Random().nextInt(4);
                Thread.sleep(1000 * (a == 0 ? 1 : a));
            } catch (InterruptedException e) {
            }
            scount += num;
            System.out.println(str + ":" + scount);
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveTest(int num, String str) {
        for (; ; ) {
            try {
                int a = new Random().nextInt(4);
                Thread.sleep(1000 * (a == 0 ? 1 : a));
            } catch (InterruptedException e) {
            }
            count += num;
            System.out.println(str + ":" + count);
        }
    }

    public List getList() {
        return list;
    }

    public void saveNone(Integer num, String str) {
        System.out.println("saveNone...");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            try {
                int a = new Random().nextInt(4);
                Thread.sleep(1000 * (a == 0 ? 1 : a));
            } catch (InterruptedException e) {
            }
            count += num;
            System.out.println(str + ":" + count);
        }

        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time :  " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void getSynOnBlockWithAttr(Integer num, String str) {
        long start = System.currentTimeMillis();
        if (count < 0) {
            System.out.println("remain money < 0");
        } else if (num > count) {
            System.out.println("get money > remain money");
        } else {
            synchronized (count) {
                for (int i = 0; i < 10; i++) {
                    try {
                        int a = new Random().nextInt(4);
                        Thread.sleep(1000 * (a == 0 ? 1 : a));
                    } catch (InterruptedException e) {
                    }
                    count -= num;
                    System.out.println(str + ":" + count);
                }
            }

            System.out.println(str + ":" + count);
            System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
        }
    }

    public synchronized void saveSynOnMethod(Integer num, String str) {
        System.out.println("saveSynOnMethod...");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            try {
                int a = new Random().nextInt(4);
                Thread.sleep(1000 * (a == 0 ? 1 : a));
            } catch (InterruptedException e) {
            }
            count += num;
            System.out.println(str + ":" + count);
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveSynOnBlockWithClass(Integer num, String str) {
        long start = System.currentTimeMillis();
        synchronized (TestBank.class) {
            for (int i = 0; i < 10; i++) {
                try {
                    int a = new Random().nextInt(4);
                    Thread.sleep(1000 * (a == 0 ? 1 : a));
                } catch (InterruptedException e) {
                }
                count += num;
                System.out.println(str + ":" + count);
            }
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveSynOnBlockWithAttr(Integer num, String str) {
        long start = System.currentTimeMillis();
        synchronized (count) {
            for (int i = 0; i < 10; i++) {
                try {
                    int a = new Random().nextInt(4);
                    Thread.sleep(1000 * (a == 0 ? 1 : a));
                } catch (InterruptedException e) {
                }
                count += num;
                System.out.println(str + ":" + count);
            }
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveSynWithVolatile(Integer num, String str) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            try {
                int a = new Random().nextInt(4);
                Thread.sleep(1000 * (a == 0 ? 1 : a));
            } catch (InterruptedException e) {
            }
            vcount += num;
            System.out.println(str + ":" + vcount);
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveSynWithLock(Integer num, String str) {
        long start = System.currentTimeMillis();
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                try {
                    int a = new Random().nextInt(4);
                    Thread.sleep(1000 * (a == 0 ? 1 : a));
                } catch (InterruptedException e) {
                }
                count += num;
                System.out.println(str + ":" + count);
            }
        } finally {
            lock.unlock();
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveSynWithCondition(Integer num, String str) {
        long start = System.currentTimeMillis();
        lock.lock();
        Condition notempty = lock.newCondition();
        Condition notfull = lock.newCondition();
        try {
            for (int i = 0; i < 10; i++) {
                try {
                    int a = new Random().nextInt(4);
                    Thread.sleep(1000 * (a == 0 ? 1 : a));
                } catch (InterruptedException e) {
                }
                if (list.size() == 0) {
                    try {
                        notempty.await();
                    } catch (InterruptedException e) {
                        notempty.signal();
                    }
                }
                list.add(i);
                notfull.signal();
                System.out.println(str + ":" + list.size());
            }
        } finally {
            lock.unlock();
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveSynWithThreadLocal(Integer num, String str) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            try {
                int a = new Random().nextInt(4);
                Thread.sleep(1000 * (a == 0 ? 1 : a));
            } catch (InterruptedException e) {
            }
            tlcount.set(tlcount.get() + num);
            System.out.println(str + ":" + tlcount.get());
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");

    }

    public void saveAtomic(Integer num, String str) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            count = at.addAndGet(num);
            System.out.println(str + ":" + count);
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }

    public void saveConcurrentHashMap(Integer num, String str) {
        long start = System.currentTimeMillis();
        ReentrantLock lock = lockMap.computeIfAbsent(str, k -> new ReentrantLock());

//        if (!lock.isHeldByCurrentThread())
//        {
//            synchronized (lock)
//            {
        try {
            boolean locked = lock.tryLock(5, TimeUnit.SECONDS);
            if (locked) {
                try {
                    for (int i = 0; i < 10; i++) {
                        try {
                            int a = new Random().nextInt(4);
                            Thread.sleep(1000 * 1);
                        } catch (InterruptedException e) {
                        }
                        count += num;
                        System.out.println(str + ":" + count);
                    }
                } finally {
                    lock.unlock();
                }
                lockMap.put(str, lock);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~ " + str + " time : " + (System.currentTimeMillis() - start) + " ~~~~~~~~~~~~~~ ~~~~~~~~~~~~~~~");
    }
//        }
//    }

}
