package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/28
 *  23:12
 */

/**
 * Description : 二分查找
 * Created by YangZH on 2017/5/28
 * 23:12
 */

public class BinarySearch {

    public static void main(String[] args) {
        System.out.println(BinarySearch.binarySearch(new int[]{2, 5, 9, 19, 29, 60}, 19));
    }

    //已经排好序的数组
    public static int binarySearch(int[] nums, int key) {
        int start = 0;
        int end = nums.length - 1;
        int mid = -1;
        while (start <= end) {
            mid = (start + end) / 2;
            if (nums[mid] == key) {
                return mid;
            } else if (nums[mid] > key) {
                end = mid - 1;
            } else if (nums[mid] < key) {
                start = mid + 1;
            }
        }
        return -1;
    }

}
