package com.lol.test;/**
 * Description : 
 * Created by YangZH on 2017/5/8
 *  0:58
 */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description :
 * Created by YangZH on 2017/5/8
 * 0:58
 */

public class WorkTest {

    public static void main(String[] args) throws Exception {
        WorkTest wt = new WorkTest();
        wt.test("abcdef",2);

        wt.test2(new Integer[]{9,8, 1, 2, 5,13});



    }

    void test(String str,int index){
        System.out.println(str.substring(index) + str.substring(0, index));
    }

    void test2(Integer[] arrays){
        Integer[] as = new Integer[arrays.length];
        int count = 0;
        for(int i = 0; i < arrays.length; i++){
            for (int j = 1; j < arrays.length; j++){
                if(arrays[i] != null && arrays[j] != null && i != j && arrays[i]+arrays[j]==10){
                    as[count] = arrays[i];
                    as[count+1] = arrays[j];
                    arrays[i] = null;
                    arrays[j] = null;
                    count+=2;
                }
            }
        }

        System.out.println(Arrays.toString(as));
        List list = Arrays.asList(arrays).stream().filter(f->f != null).sorted().collect(Collectors.toList());
        System.out.println(list);
    }
}
