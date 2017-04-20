/******************************************************************************
 *
 * Module Name:  entity.design.proxy - Test.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Apr 29, 2016
 * Last Updated By: randyzhyang
 * Last Updated Date: Apr 29, 2016
 * Description:
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
package com.lol.demo.entity.design.proxy;

public class Test {

    public static void main(String[] args) {
        IDBQuery d = null;
        long begin = System.currentTimeMillis();
        d = ProxyUtil.createCglibProxy();
        System.out.println("createCglibProxy : " + (System.currentTimeMillis() - begin));
        System.out.println("CglibProxy class : " + d.getClass().getName());

        begin = System.currentTimeMillis();
        for (int i = 0; i < 30000000; i++) {
            d.findUserName();
        }
        System.out.println("callCglibProxy : " + (System.currentTimeMillis() - begin));
        System.out.println(d.findUserName());

    }

}
