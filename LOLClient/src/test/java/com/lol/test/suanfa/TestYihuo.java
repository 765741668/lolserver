package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/19
 *  0:44
 */

/**
 * 二进制：两个操作数的位中，相同则结果为0，不同则结果为1
 *
 * Description : 一个数异或另一个数两次: 等于自己
 * 一个数异或0：还是自己
 * 一个数异或自己: 0
 * Created by YangZH on 2017/5/19
 * 0:44
 */

public class TestYihuo {
    public static void main(String[] args) {
        int a = 3; int b =9;
        System.out.println(a^b^b^a^b);
        System.out.println(a^b^b);
        a = a ^ b;
        System.out.println(a);
        b = a ^ b;
        System.out.println(b);
        a = a ^ b;
        System.out.println(a);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        int a1 = 5; int b1 =10;
        a1 = a1 + b1;
        System.out.println(a1);
        b1 = a1 - b1;
        System.out.println(b1);
        a1 = a1 - b1;
        System.out.println(a1);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println(3^9);
        System.out.println(3^9^9);
        System.out.println(3^9^9^3^9);

    }
}
