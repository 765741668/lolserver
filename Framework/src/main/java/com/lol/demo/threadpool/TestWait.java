/******************************************************************************
 *
 * Module Name:  com.yzh.threadpool - TestWait.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Jul 28, 2016
 * Last Updated By: randyzhyang
 * Last Updated Date: Jul 28, 2016
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
package com.lol.demo.threadpool;

public class TestWait {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Thread1()).start();
        System.out.println("main thread is sleep 1s ...");
        Thread.sleep(1000);
        new Thread(new Thread2()).start();
    }

    static class Thread1 implements Runnable {

        @Override
        public void run() {
            synchronized (TestWait.class) {
                System.out.println("enter thread1...");
                try {
                    System.out.println("thread1 is waiting...");
                    TestWait.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("thread1 is going on...");
                System.out.println("thread1 is over...");
            }

        }

    }

    static class Thread2 implements Runnable {

        @Override
        public void run() {
            synchronized (TestWait.class) {
                System.out.println("enter thread2...");
                try {
                    System.out.println("thread2 notify other thread...");
                    TestWait.class.notify();
                    System.out.println("thread2 is sleep 2s ...");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("thread2 is going on...");
                System.out.println("thread2 is over...");
            }

        }

    }
}
