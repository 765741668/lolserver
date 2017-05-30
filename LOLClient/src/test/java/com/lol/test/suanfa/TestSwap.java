package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/28
 *  23:16
 */

/**
 * Description :
 * Created by YangZH on 2017/5/28
 * 23:16
 */

public class TestSwap {

    public static void main(String[] args) {
        int[] a = new int[]{11,2,3,42,5,6};
        SortUtil.swap3(a, 1, 4);
        System.out.println(a[0]);
        System.out.println(a[1]);
        System.out.println(a[2]);
        System.out.println(a[3]);
        System.out.println(a[4]);
        System.out.println(a[5]);
    }

}
