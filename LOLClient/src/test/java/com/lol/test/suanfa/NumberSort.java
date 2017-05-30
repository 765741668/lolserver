package com.lol.test.suanfa;

import java.util.ArrayList;
import java.util.List;

public class NumberSort {
    //私有构造方法，禁止实例化  
    private NumberSort() {   
        super();   
    }    
    //冒泡法排序 
    public static void bubbleSort(int[] numbers) {   
        int temp; // 记录临时中间值   
        int size = numbers.length; // 数组大小   
        for (int i = 0; i < size - 1; i++) {   
            for (int j = i + 1; j < size; j++) {   
                if (numbers[i] < numbers[j]) { // 交换两数的位置   
                    temp = numbers[i];   
                    numbers[i] = numbers[j];   
                    numbers[j] = temp;   
                }   
            }   
        }   
    }   
    //快速排序 基本思想：选择一个基准元素,通常选择第一个元素或者最后一个元素,通过一趟扫描，将待排序列分成两部分,
    // 一部分比基准元素小,一部分大于等于基准元素,此时基准元素在其排好序后的正确位置,然后再用同样的方法递归地排序划分的两部分。
    public void quickSort(int[] a,int low,int high){
        int start = low;
        int end = high;
        int key = a[low];

        while(end>start){
            //从后往前比较
            while(end>start&&a[end]>=key)  //如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
                end--;
            if(a[end]<=key){
                int temp = a[end];
                a[end] = a[start];
                a[start] = temp;
            }
            //从前往后比较
            while(end>start&&a[start]<=key)//如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
                start++;
            if(a[start]>=key){
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
            }
            //此时第一次循环比较结束，关键值的位置已经确定了。左边的值都比关键值小，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
        }
        //递归
        if(start>low) quickSort(a, low, start - 1);//左边序列。第一个索引位置到关键值索引-1
        if(end<high) quickSort(a, end + 1, high);//右边序列。从关键值索引+1到最后一个
    }

    //选择排序 
    public static void selectSort(int[] numbers) {   
        int size = numbers.length, temp;   
        for (int i = 0; i < size; i++) {   
            int k = i;   
            for (int j = size - 1; j > i; j--) {   
                if (numbers[j] < numbers[k])   
                    k = j;   
            }   
            temp = numbers[i];   
            numbers[i] = numbers[k];   
            numbers[k] = temp;   
        }   
    }   
    //插入排序    
    // @param numbers  
    public static void insertSort(int[] numbers) {   
        int size = numbers.length, temp, j;   
        for (int i = 1; i < size; i++) {   
            temp = numbers[i];   
            for (j = i; j > 0 && temp < numbers[j - 1]; j--)   
                numbers[j] = numbers[j - 1];   
            numbers[j] = temp;   
        }   
    }   
    //归并排序  
    public static void mergeSort(int[] numbers, int left, int right) {
        int t = 1;// 每组元素个数
        int size = right - left + 1;
        while (t < size) {
            int s = t;// 本次循环每组元素个数
            t = 2 * s;
            int i = left;
            while (i + (t - 1) < size) {
                merge(numbers, i, i + (s - 1), i + (t - 1));
                i += t;
            }
            if (i + (s - 1) < right)
                merge(numbers, i, i + (s - 1), right);
        }
    }
    //归并算法实现
    private static void merge(int[] data,  int left, int center, int right) {
        int [] tmpArr=new int[data.length];
        int mid=center+1;
        //third记录中间数组的索引
        int third=left;
        int tmp=left;
        while(left<=center&&mid<=right){

            //从两个数组中取出最小的放入中间数组
            if(data[left]<=data[mid]){
                tmpArr[third++]=data[left++];
            }else{
                tmpArr[third++]=data[mid++];
            }
        }
        //剩余部分依次放入中间数组
        while(mid<=right){
            tmpArr[third++]=data[mid++];
        }
        while(left<=center){
            tmpArr[third++]=data[left++];
        }
        //将中间数组中的内容复制回原数组
        while(tmp<=right){
            data[tmp]=tmpArr[tmp++];
        }
    }

    //8、基数排序 基本思想：将所有待比较数值（正整数）统一为同样的数位长度，数位较短的数前面补零。
    // 然后，从最低位开始，依次进行一次排序。这样从最低位排序一直到最高位排序完成以后,数列就变成一个有序序列。
    public  void radixSort(int[] array){

        //首先确定排序的趟数;
        int max=array[0];
        for(int i=1;i<array.length;i++){
            if(array[i]>max){
                max=array[i];
            }
        }

        int time=0;
        //判断位数;
        while(max>0){
            max/=10;
            time++;
        }

        //建立10个队列;
        List<ArrayList> queue=new ArrayList<ArrayList>();
        for(int i=0;i<10;i++){
            ArrayList<Integer> queue1=new ArrayList<Integer>();
            queue.add(queue1);
        }

        //进行time次分配和收集;
        for(int i=0;i<time;i++){

            //分配数组元素;
            for(int j=0;j<array.length;j++){
                //得到数字的第time+1位数;
                int x=array[j]%(int)Math.pow(10, i+1)/(int)Math.pow(10, i);
                ArrayList<Integer> queue2=queue.get(x);
                queue2.add(array[j]);
                queue.set(x, queue2);
            }
            int count=0;//元素计数器;
            //收集队列元素;
            for(int k=0;k<10;k++){
                while(queue.get(k).size()>0){
                    ArrayList<Integer> queue3=queue.get(k);
                    array[count]=queue3.get(0);
                    queue3.remove(0);
                    count++;
                }
            }
        }
    }

}