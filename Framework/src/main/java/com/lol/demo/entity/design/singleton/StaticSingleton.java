/******************************************************************************
 *
 * Module Name:  entity - Info.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Aug 24, 2015
 * Last Updated By: randyzhyang
 * Last Updated Date: Aug 24, 2015
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
/**
 *
 */
package com.lol.demo.entity.design.singleton;

/**
 * @author RANDYZHY
 *         Aug 24, 2015 2:19:55 PM
 */
public class StaticSingleton {
    private String id;

    private StaticSingleton() {
        System.out.println("StaticSingleton is created");
    }

    public static StaticSingleton getInstance() {
        return SingletonHolder.instance;
    }

    public static void createString() {
        System.out.println("createString in StaticSingleton");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static class SingletonHolder {
        private static StaticSingleton instance = new StaticSingleton();
    }

}
