package com.lol.test.suanfa;

/*
 * 分块查找
 * a. 首先将查找表分成若干块，在每一块中数据元素的存放是任意的，但块与块之间必须是有序的（假设这种排序是按关键字值递增的，
 *    也就是说在第一块中任意一个数据元素的关键字都小于第二块中所有数据元素的关键字，第二块中任意一个数据元素的关键字都小于第三块中所有数据元素的关键字，依次类推）；
   b. 建立一个索引表，把每块中最大的关键字值按块的顺序存放在一个辅助数组中，这个索引表也按升序排列；
   c. 查找时先用给定的关键字值在索引表中查找，确定满足条件的数据元素存放在哪个块中，查找方法既可以是折半方法，也可以是顺序查找。
   d. 再到相应的块中顺序查找，便可以得到查找的结果。
 */
public class BlockSearch {
    public static void main(String[] arg) {
        int[] a = {0, 3, 5, 7, 8, 9, 10, 13};
        int[] index = {3,7,6, 13};
        System.out.println(blockSearch(index,a,10,2));
    }

    /**
     * 分块查找
     *
     * @param index　索引表，其中放的是各块的最大值
     * @param st 顺序表，
     * @param key 要查找的值
     * @param m 顺序表中各块的长度相等，为m
     * @return
     */
    public static int blockSearch(int[] index, int[] st, int key, int m) {
        // 在序列st数组中，用分块查找方法查找关键字为key的记录
        // 1.在index[ ] 中折半查找，确定要查找的key属于哪个块中
        int i = BinarySearch.binarySearch(index, key);
        if (i >= 0) {
            int j = i > 0 ? i * m : i;
            int len = (i + 1) * m;
            // 在确定的块中用顺序查找方法查找key
            for (int k = j; k < len; k++) {
                if (key == st[k]) {
                    System.out.println("查询成功");
                    return k;
                }
            }
        }
        System.out.println("查找失败");
        return -1;
    }
}