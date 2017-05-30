package com.lol.test.suanfa;/**
 * Description : 
 * Created by YangZH on 2017/5/28
 *  23:12
 */

/**
 * Description : 哈希查找
 * 哈希表查找是通过对记录的关键字值进行运算，直接求出结点的地址，是关键字到地址的直接转换方法，不用反复比较。假设f包含n个结点，Ri为其中某个结点（1≤i≤n），
 * keyi是其关键字值，在keyi与Ri的地址之间建立某种函数关系，可以通过这个函数把关键字值转换成相应结点的地址，有：addr(Ri)=H(keyi)，addr(Ri)为哈希函数。
 解决冲突的方法有以下两种：　　
 (1)开放地址法　　
 如果两个数据元素的哈希值相同，则在哈希表中为后插入的数据元素另外选择一个表项。当程序查找哈希表时，如果没有在第一个对应的哈希表项中找到符合查找要求的数据元素，
 程序就会继续往后查找，直到找到一个符合查找要求的数据元素，或者遇到一个空的表项。　　
 (2)链地址法
   将哈希值相同的数据元素存放在一个链表中，在查找哈希表的过程中，当查找到这个链表时，必须采用线性查找方法。
 * Created by YangZH on 2017/5/28
 * 23:12
 */

public class HashSearch {

    public static void main(String[] args) {
        System.out.println(searchHash(new int[]{2, 5, 9, 19, 29, 60}, 29));
    }

    /****
     * Hash查找
     *
     * @param hash
     * @param key
     * @return
     */
    public static int searchHash(int[] hash, int key) {
        // 哈希函数
        int len = hash.length;
        int hashAddress = key % len;

        // 指定hashAdrress对应值存在但不是关键值，则用开放寻址法解决
        while (hash[hashAddress] != 0 && hash[hashAddress] != key) {
            hashAddress = (++hashAddress) % len;
        }

        // 查找到了开放单元，表示查找失败
        if (hash[hashAddress] == 0)
            return -1;
        return hashAddress;

    }

    /***
     * 数据插入Hash表
     *
     * @param hash 哈希表
     * @param hashLength
     * @param data
     */
    public static void insertHash(int[] hash, int hashLength, int data) {
        // 哈希函数
        int hashAddress = data % hashLength;

        // 如果key存在，则说明已经被别人占用，此时必须解决冲突
        while (hash[hashAddress] != 0) {
            // 用开放寻址法找到
            hashAddress = (++hashAddress) % hashLength;
        }

        // 将data存入字典中
        hash[hashAddress] = data;
    }

}
