package com.lol.test.suanfa;

import java.util.Random;

/**
 * 斐波那契查找
 */
public class FibonacciSearch {

    public static void main(String[] args) {
        int[] table = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
        for (int aTable : table) {
            System.out.println("表中记录为" + aTable + "\t,查询结果为" + FibonacciSearch.fibonacciSearch(table, aTable) + "\n");
        }

        for(int i = 0;i<5;i++){
            int ran = new Random().nextInt(100)+14;
            System.out.println("关键字为："+ran+"查询结果为"+FibonacciSearch.fibonacciSearch(table, ran)+"\n");
        }

    }

    public static boolean fibonacciSearch(int[] table,int keyWord){
        //确定需要的斐波那契数
        int i = 0;
        while(getFibonacci(i)-1 == table.length){
            i++;
        }
        //开始查找
        int low = 0;
        int height = table.length-1;
        while(low<=height){
            int mid = low + getFibonacci(i-1);
            if(table[mid] == keyWord){
                return true;
            }else if(table[mid]>keyWord){
                height = mid-1;
                i--;
            }else if(table[mid]<keyWord){
                low = mid+1;
                i-=2;
            }
        }
        return false;
    }

    /**
     * 得到第n个斐波那契数
     * @return
     */
    public static int getFibonacci(int n){
        int res = 0;
        if(n == 0){
            res = 0;
        }else if(n == 1){
            res = 1;
        }else{
            int first = 0;
            int second = 1;
            for(int i = 2;i<=n;i++){
                res = first+second;
                first = second;
                second = res;
            }
        }
        return res;
    }
}