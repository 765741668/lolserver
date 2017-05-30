package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/29
 *  3:13
 */

/**
 * Description :
 * Created by YangZH on 2017/5/29
 * 3:13
 */

public class SortUtil {

    public static void printArray(int[] array) {
        System.out.print("{");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    //交换
    public static void swap(int[] a, int i, int j) {
        a[i] ^= a[j];
        a[j] ^= a[i];
        a[i] ^= a[j];
    }

    public static void swap2(int[] a, int i, int j) {
        a[i] += a[j];
        a[j] = a[i] - a[j];
        a[i] -= a[j];
    }

    public static void swap3(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[i] = a[i];
        a[j] = temp;
    }
}
