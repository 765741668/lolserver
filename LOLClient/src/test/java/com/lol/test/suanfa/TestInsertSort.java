package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/29
 *  0:49
 */

/**
 * Description :
 * Created by YangZH on 2017/5/29
 * 0:49
 */

public class TestInsertSort {


    public static void insertSort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }

        int j,nextNode;
        for(int i = 1; i < array.length; i++){
            nextNode = array[i];
            j = i-1;
            while(j >= 0 && nextNode < array[j]){
                array[j+1] = array[j];
                j--;
            }

           array[j+1] = nextNode;
        }
    }

    public static void insertSort2(int[] a) {
        int j, insertNote;// 要插入的数据
        for (int i = 1; i < a.length; i++) {// 从数组的第二个元素开始循环将数组中的元素插入
            insertNote = a[i];// 设置数组中的第2个元素为第一次循环要插入的数据
            j = i - 1;
            while (j >= 0 && insertNote < a[j]) {
                a[j + 1] = a[j];// 如果要插入的元素小于第j个元素,就将第j个元素向后移动
                j--;
            }
            a[j + 1] = insertNote;// 直到要插入的元素不小于第j个元素,将insertNote插入到数组中
        }
    }

    public static void main(String[] args) {
        int[] array = new int[]{30,60,2,9, 5, 19, 29};
        SortUtil.printArray(array);
        insertSort2(array);
        SortUtil.printArray(array);
    }
}
