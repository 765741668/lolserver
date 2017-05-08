/******************************************************************************
 *
 * Module Name:  forkjoin - Test.java
 * Version: 1.0.0
 * Original Author: java稀少的
 * Created Date: Dec 1, 2015
 * Last Updated By: java
 * Last Updated Date: Dec 1, 2015
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
package com.lol.demo.forkjoin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author java
 */
public class Test {
    /**
     * @param args
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.util.concurrent.ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        int arr[] = new int[10];
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            total += (arr[i] = i);

            System.out.println(arr[i]);
        }

        System.out.println("total : " + total + "\n");

        long start = System.currentTimeMillis();
        ForkJoinPool pool = ForkJoinPool.commonPool();
        ForkJoinTask<Integer> result = pool.submit(new NumberCountTask(arr, 0, arr.length));

        System.out.println("result : " + result.get());
        System.out.println((System.currentTimeMillis() - start));

        pool.shutdown();
    }
}
