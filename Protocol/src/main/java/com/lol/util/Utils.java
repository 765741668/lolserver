package com.lol.util;

import org.apache.commons.lang3.RandomUtils;

/**
 * 工具类
 *
 * @author Randy
 */
public class Utils {
    /**
     * 取得指定范围随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min, int max) {
        return RandomUtils.nextInt(min, max + 1);
    }

    /**
     * 取得指定范围随机浮点数
     *
     * @param min
     * @param max
     * @return
     */
    public static float getRandomNum(float min, float max) {
        return RandomUtils.nextFloat(min, max);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static int getTimeStamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) {
        System.out.println(getRandomNum(1,4));
        System.out.println(getRandomNum(0.1f,4.0f));
    }
}
