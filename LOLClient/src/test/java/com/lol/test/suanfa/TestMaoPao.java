package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/29
 *  2:48
 */

/**
 * Description :
 * Created by YangZH on 2017/5/29
 * 2:48
 */

public class TestMaoPao {

    private static void maoPao(int[] a) {
        for(int i=0;i<a.length-1;i++){
            for(int j=0;j<a.length-1-i;j++){
                if(a[j+1] < a[j]){
                    SortUtil.swap(a,j+1,j);
                }
           }
       }
    }

    public static void main(String[] args) {
        int[] array = new int[]{30,60,2,9, 5, 19, 29};
        SortUtil.printArray(array);
        maoPao(array);
        SortUtil.printArray(array);
    }

}
