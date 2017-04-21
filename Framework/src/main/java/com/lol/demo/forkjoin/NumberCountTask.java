/******************************************************************************
 *
 * Module Name:  forkjoin - DocSearchTask.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Dec 2, 2015
 * Last Updated By: java
 * Last Updated Date: Dec 2, 2015
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

import java.util.concurrent.RecursiveTask;

/**
 * @author java
 */
public class NumberCountTask extends RecursiveTask<Integer> {

    private static final long serialVersionUID = -4380448472920900739L;

    private int arr[];
    private int start;
    private int end;

    public NumberCountTask(int arr[], int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;


        if (end - start <= 2) {
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }

            return sum;
        } else {
            System.out.println("start : " + start);
            System.out.println("end : " + end);
            int middle = (start + end) / 2;
            System.out.println("middle : " + middle);
            NumberCountTask task1 = new NumberCountTask(arr, start, middle);
            NumberCountTask task2 = new NumberCountTask(arr, middle, end);

            task1.fork();
            task2.fork();
            sum = task1.join() + task2.join();
            System.out.println("task1 : " + task1.join());
            System.out.println("task2 : " + task2.join());
            System.out.println();

            return sum;

        }
    }

}
