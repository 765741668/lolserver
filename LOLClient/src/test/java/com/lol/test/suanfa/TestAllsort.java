package com.lol.test.suanfa; /**
 * Description : 
 * Created by YangZH on 2017/2/21
 *  19:44
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 2017/2/21
 * 19:44
 */

public class TestAllsort {
    static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
        TestAllsort ts = new TestAllsort();
        String a = "abcdefghi";
        Date d1 = new Date();
        ts.degui(a, "");
        Date d2 = new Date();
//        list.forEach(System.out::println);
        System.out.println("Time : " + (d2.getTime() - d1.getTime()));
        System.out.println(list.size());
//
        System.out.println(ts.jiecheng(5));

        String str[] = {"a", "b", "c"};
        ts.arrange(str, 0, str.length);
        System.out.println(128>>2);
        System.out.println(-128>>>1);


    }

    private void degui(String source, String temp) {
        if (source.length() == 0) {
            list.add(temp);
        }
        for (int i = 0; i < source.length(); i++) {
            degui(new StringBuffer(source).deleteCharAt(i).toString(), temp + source.charAt(i));
        }
    }

    private void performance() {

    }

    private int jiecheng(int n) {
        int result = 1;
        if (n < 0) {
            return -1;
        } else if (n == 1) {
            return 1;
        } else {
            for (int i = 1; i <= n; i++)
                result *= i;
        }
        return result;
    }

    public void swap(String[] str, int i, int j) {
        String temp = new String();
        temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }

    public void arrange(String[] str, int st, int len) {
        if (st == len - 1) {
            for (int i = 0; i < len; i++) {
                System.out.print(str[i] + "  ");
            }
            System.out.println();
        } else {
            for (int i = st; i < len; i++) {
                swap(str, st, i);
                arrange(str, st + 1, len);
                swap(str, st, i);
            }
        }

    }
}