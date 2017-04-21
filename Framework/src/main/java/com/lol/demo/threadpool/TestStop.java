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

public class TestStop {
    public static void main(String[] args) throws InterruptedException {
        ThreadStop runnable = new ThreadStop();
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println(thread.getName() + " is started...");
        Thread.sleep(3000);
        System.out.println(thread.getName() + " is interrupting...");
        runnable.stop();
        System.out.println("stoping app " + thread.getName());

        System.out.println(thread.getName() + " is interrupted : " + thread.isInterrupted());
    }

}


