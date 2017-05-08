/******************************************************************************
 *
 * Module Name:  threadpool - ThreadDoSomething.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Apr 22, 2016
 * Last Updated By: java
 * Last Updated Date: Apr 22, 2016
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

import com.lol.demo.entity.design.singleton.StaticSingleton;
import com.lol.demo.entity.design.xiangyuan.IXiangYuan;
import com.lol.demo.entity.design.xiangyuan.XiangyuanReportFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolDoSomething {
    public static void RunXiangYuanWithThreadPool(XiangyuanReportFactory factory, int threadLenght) {
        ExecutorService ex = Executors.newFixedThreadPool(threadLenght);
        for (int i = 1; i <= threadLenght; i++) {
            ex.execute(new Runnable() {
                @Override
                public void run() {
                    IXiangYuan r1 = factory.getReportManager("1");
                    System.out.println(r1.createReport());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }

        ex.shutdown();
    }

    public static void RunSingletonWithThreadPool(int threadLenght) {
        ExecutorService ex = Executors.newFixedThreadPool(threadLenght);

        List<StaticSingleton> ss = new ArrayList<>();
        for (int i = 1; i <= threadLenght; i++) {
            ex.execute(new Runnable() {
                @Override
                public void run() {
                    StaticSingleton.createString();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        ex.shutdown();
    }
}
