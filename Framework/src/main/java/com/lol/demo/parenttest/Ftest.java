/******************************************************************************
 *
 * Module Name:  parenttest - ftest.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Aug 4, 2015
 * Last Updated By: randyzhyang
 * Last Updated Date: Aug 4, 2015
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
package com.lol.demo.parenttest;

import com.lol.demo.test2.TestLists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RANDYZHY
 *         Aug 4, 2015 4:43:27 PM
 */
public class Ftest {
    private String id;
    private String name;
    private List<TestLists> list;

    public String getDefaultName() {
        return this.getName() + "(DEFAULT)";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public List<TestLists> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }

    public void setList(List<TestLists> list) {
        this.list = list;
    }

}
