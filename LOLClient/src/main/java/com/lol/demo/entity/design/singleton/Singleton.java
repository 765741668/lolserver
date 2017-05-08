/******************************************************************************
 *
 * Module Name:  entity - Info.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Aug 24, 2015
 * Last Updated By: java
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
public class Singleton {
    private static Singleton instance = new Singleton();
    private int id;

    private Singleton() {
        System.out.println("Singleton is created");
    }

    public static Singleton getInstance() {
        return instance;
    }

    public static void createString() {
        System.out.println("createString in singleton");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
