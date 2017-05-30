package com.lol.test.suanfa;

/*
 * 顺序查找
 */
public class SequelSearch {
    public static void main(String[] arg) {
        int[] a = {4, 6, 2, 8, 1, 9, 0, 3};
        System.out.println(search(a, 1));
    }

    public static int search(int[] a, int num) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == num) {//如果数据存在
                return i;//返回数据所在的下标，也就是位置
            }
        }
        return -1;//不存在的话返回-1
    }
}