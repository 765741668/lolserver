package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/29
 *  4:01
 */

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 * Created by YangZH on 2017/5/29
 * 4:01
 */

public class TestQuickSort {

    public static int getMiddle(int[] list, int low, int high) {
        int mid = list[low];    //数组的第一个作为中轴
        while (low < high) {
            while (low < high && list[high] >= mid) {
                high--;
            }
            list[low] = list[high];   //比中轴小的记录移到低端
            while (low < high && list[low] <= mid) {
                low++;
            }
            list[high] = list[low];   //比中轴大的记录移到高端
        }
        list[low] = mid;              //中轴记录到尾
        return low;                   //返回中轴的位置
    }
    public static void quickSort(int[] list, int low, int high) {
        if (list.length > 0 && low < high) {
            int middle = getMiddle(list, low, high);  //将list数组进行一分为二
            quickSort(list, low, middle - 1);        //对低字表进行递归排序
            quickSort(list, middle + 1, high);       //对高字表进行递归排序
        }
    }

    public static void main(String[] args) {
        int[] array = new int[]{30,60,2,9, 5, 19, 29};
        SortUtil.printArray(array);
        quickSort(array,0,array.length-1);
        SortUtil.printArray(array);

        Set<String> words = ConcurrentHashMap.<String> newKeySet();
        words.add("a");
        words.add("a");
        words.add("b");
        System.out.println(words);
    }
}
