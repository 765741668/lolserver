package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/29
 *  3:02
 */

/**
 * Description :
 * Created by YangZH on 2017/5/29
 * 3:02
 */

public class TestSelectSort {

    //选择排序
    public static void selectSort(int[] a) {
        //选择排序的优化
        for(int i = 0; i < a.length - 1; i++) {// 做第i趟排序
            int k = selectMinKey(a,i);
            //找到本轮循环的最小的数以后，再进行交换
            if(i != k){
                SortUtil.swap(a,i,k);
            }
        }

    }

    // 查找从i开始到a.length中最小的位置
    private static int selectMinKey(int[] a, int i) {
        int key = i;
        for(int j = i+1; j < a.length; j++) {
            if(a[j] < a[key]){
                key = j;
            }
        }
        return key;
    }

    public static void main(String[] args) {
        int[] array = new int[]{30,60,2,9, 5, 19, 29};
        SortUtil.printArray(array);
        selectSort(array);
        SortUtil.printArray(array);
    }
}
