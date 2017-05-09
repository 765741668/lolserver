/**
 *
 */
/**
 * @author java
 *
 */
package com.lol.fwk.util;

import java.text.DecimalFormat;

public class NettyUtils {

    public static String get0_1Percentage(int length) {
        DecimalFormat bd = new DecimalFormat("0.00");
        ;
        bd.setMaximumFractionDigits(2);
        return bd.format(Math.random());
    }

    public static Double get0_1PercentageArea(int length, double min, double max) {
        Double random = Double.valueOf(get0_1Percentage(length));
        while (true) {
            if (random > min && random < max) {
                return random;
            } else {
                random = Double.valueOf(get0_1Percentage(length));
            }
        }
    }

    public static Long get4_5Num(Double num) {


        return 0l;
    }


    public static void main(String[] args) {
        System.out.println(get0_1PercentageArea(2, 0.8, 1));
//        System.out.println(get0_1Percentage(2));

    }


}
